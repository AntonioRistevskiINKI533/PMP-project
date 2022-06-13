package com.android.example.pmpproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private Button logout, insertNewNote;
    private TextView userEmail;

    String email, UID;

    private ListView notesListView;
    List<String> notes_array_list;
    List<String> notes_array_list_ids;

    private FirebaseUser user;

    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        fStore = FirebaseFirestore.getInstance();

        logout = (Button) findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener(){
            @Override public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(HomeActivity.this, MainActivity.class));
            }
        });

        FloatingActionButton insertNewNote = findViewById(R.id.insertNewNote);
        insertNewNote.setOnClickListener(new View.OnClickListener(){
            @Override public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, InsertNoteActivity.class));
            }
        });

        setUserData();//Sets the email and UID

        notesListView = (ListView) findViewById(R.id.notes_list_view);
        notesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //startActivity(new Intent(HomeActivity.this, ViewNoteActivity.class));
                String docId = notes_array_list_ids.get(position);

                Intent i = new Intent(HomeActivity.this, ViewNoteActivity.class);

                Bundle bundle = new Bundle();

                bundle.putString("docId", docId);

                i.putExtras(bundle);

                startActivity(i);
            }
        });

        notes_array_list_ids = new ArrayList<String>();
        notes_array_list = new ArrayList<String>();

        //Query query = fStore.collection("notes").orderBy("title");
        fStore.collection("notes").whereEqualTo("UID",UID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                System.out.println(document.getId() + document.getData());
                                //Log.d(TAG, document.getId() + " => " + document.getData());
                                notes_array_list.add(document.getString("title").toString());
                                notes_array_list_ids.add(document.getId().toString());
                            }

                            fillListView();

                        } else {
                            Toast.makeText(HomeActivity.this, getResources().getString(R.string.failed_to_get_notes), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void fillListView() {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                notes_array_list );

        notesListView.setAdapter(arrayAdapter);
    }

    private void setUserData() {
        userEmail = (TextView) findViewById(R.id.userEmail);

        user = FirebaseAuth.getInstance().getCurrentUser();
        UID = user.getUid();
        email = user.getEmail();
        if(email != null){
            userEmail.setText(email);
        }
        else{
            userEmail.setText(getResources().getString(R.string.anonymous_user));
        }
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