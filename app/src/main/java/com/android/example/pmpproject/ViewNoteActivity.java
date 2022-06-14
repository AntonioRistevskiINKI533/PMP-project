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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ViewNoteActivity extends AppCompatActivity implements View.OnClickListener {

    private Button saveNote, deleteNote;
    private EditText noteTitleField, noteTextField;

    private FirebaseUser user;

    String docId;

    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_note);

        user = FirebaseAuth.getInstance().getCurrentUser();

        fStore = FirebaseFirestore.getInstance();

        saveNote = (Button) findViewById(R.id.saveNote);
        saveNote.setOnClickListener(this);

        deleteNote = (Button) findViewById(R.id.deleteNote);
        deleteNote.setOnClickListener(this);

        noteTitleField = (EditText) findViewById(R.id.noteTitleField);
        noteTextField = (EditText) findViewById(R.id.noteTextField);

        //Get the bundle
        Bundle bundle = getIntent().getExtras();

        //Extract the docId from HomeActivity.
        docId = bundle.getString("docId");

        DocumentReference docRef = fStore.collection("notes").document(docId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        noteTitleField.setText(document.getString("title").toString());
                        noteTextField.setText(document.getString("text").toString());
                        //Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    } else {
                        Toast.makeText(ViewNoteActivity.this, getResources().getString(R.string.failed_to_get_notes), Toast.LENGTH_LONG).show();
                        //Log.d(TAG, "No such document");
                    }
                } else {
                    Toast.makeText(ViewNoteActivity.this, getResources().getString(R.string.failed_to_get_notes), Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.deleteNote:
                deleteNote();
                startActivity(new Intent(this,HomeActivity.class));
                break;
            case R.id.saveNote:
                saveNote();
                startActivity(new Intent(this,HomeActivity.class));
                break;
        }
    }

    private void deleteNote() {

        fStore.collection("notes").document(docId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(ViewNoteActivity.this, getResources().getString(R.string.delete_success), Toast.LENGTH_LONG).show();
                        //Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ViewNoteActivity.this, getResources().getString(R.string.error), Toast.LENGTH_LONG).show();
                        //Log.w(TAG, "Error deleting document", e);
                    }
                });
    }

    private void saveNote() {

        Map<String, Object> city = new HashMap<>();
        city.put("UID", user.getUid().toString());
        city.put("title", noteTitleField.getText().toString());
        city.put("text", noteTextField.getText().toString());

        fStore.collection("notes").document(docId)
                .set(city)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(ViewNoteActivity.this, getResources().getString(R.string.save_success), Toast.LENGTH_LONG).show();
                        //Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ViewNoteActivity.this, getResources().getString(R.string.error), Toast.LENGTH_LONG).show();
                        //Log.w(TAG, "Error writing document", e);
                    }
                });
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