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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class ViewNoteActivity extends AppCompatActivity implements View.OnClickListener {

    private Button saveNote, deleteNote;
    private EditText noteTitleField, noteTextField;

    private FirebaseUser user;

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

    }

    private void saveNote() {


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