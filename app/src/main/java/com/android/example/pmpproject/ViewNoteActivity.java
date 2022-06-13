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
import com.google.firebase.database.core.view.QueryParams;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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

/*        fStore.collection("notes").whereEqualTo("UID","TuvJB8xmQUY5JBojzaNNeu8mPkg1")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String a = document.getId().toString();
                                System.out.println(document.getId() + document.getData());
                                //Log.d(TAG, document.getId() + " => " + document.getData());
                                if(a == "6cixm11FqOOXRXcI9J8U"){
                                    noteTitleField.setText(document.get("title").toString());
                                    noteTextField.setText(document.get("text").toString());
                                }
                            }

                        } else {
                            Toast.makeText(ViewNoteActivity.this, getResources().getString(R.string.failed_to_get_notes), Toast.LENGTH_LONG).show();
                        }
                    }
                });*/

       /* fStore.collection("notes").document("6cixm11FqOOXRXcI9J8U")
                .get()
                .addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            *//*for (QueryDocumentSnapshot document : task.getResult()) {
                                System.out.println(document.getId() + document.getData());
                                //Log.d(TAG, document.getId() + " => " + document.getData());
                                noteTitleField.setText(document.get("title").toString());
                                noteTextField.setText(document.get("text").toString());
                            }*//*
                            Object document = task.getResult();
                            //noteTitleField.setText(document.get("title").toString());
                            //noteTextField.setText(document.get("text").toString());

                        } else {
                            Toast.makeText(ViewNoteActivity.this, getResources().getString(R.string.error), Toast.LENGTH_LONG).show();
                        }
                    }
                });*/
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
                        //Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
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
                        //Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
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