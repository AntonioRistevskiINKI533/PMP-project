package com.android.example.pmpproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class InsertNoteActivity extends AppCompatActivity implements View.OnClickListener {

    private Button saveNote, discardNote;
    private EditText noteTitleField, noteTextField;

    private FirebaseUser user;

    FirebaseFirestore fStore;

    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_note);

        user = FirebaseAuth.getInstance().getCurrentUser();

        fStore = FirebaseFirestore.getInstance();

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        saveNote = (Button) findViewById(R.id.saveNote);
        saveNote.setOnClickListener(this);

        discardNote = (Button) findViewById(R.id.discardNote);
        discardNote.setOnClickListener(this);

        noteTitleField = (EditText) findViewById(R.id.noteTitleField);
        noteTextField = (EditText) findViewById(R.id.noteTextField);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.discardNote:
                discardNote();
                startActivity(new Intent(this,HomeActivity.class));
                break;
            case R.id.saveNote:
                saveNewNote();
                startActivity(new Intent(this,HomeActivity.class));
                break;
        }
    }

    private void saveNewNote() {
        // Create a new user with a first and last name
        Map<String, Object> note = new HashMap<>();
        note.put("UID", user.getUid().toString());
        note.put("title", noteTitleField.getText().toString());
        note.put("text", noteTextField.getText().toString());

        // Add a new document with a generated ID
        //DocumentReference db = fStore.collection("notes").document();

        fStore.collection("notes")
                .add(note)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        //FirebaseAnalytics, log a saveNote event
                        Bundle params = new Bundle();
                        params.putString("title", "test");
                        params.putString("text", "test");
                        mFirebaseAnalytics.logEvent("save_note", params);

                        Toast.makeText(InsertNoteActivity.this, getResources().getString(R.string.save_success), Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(InsertNoteActivity.this, getResources().getString(R.string.error), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void discardNote() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        LanguageManager lang = new LanguageManager(this);

        switch  (item.getItemId()) {

            case R.id.mkd_btn:
                lang.updateResource("mk");
                recreate();
                break;
            case R.id.en_btn:
                lang.updateResource("en");
                recreate();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}