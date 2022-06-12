package com.android.example.pmpproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomeActivity extends AppCompatActivity {

    private Button logout, insertNewNote;
    private TextView userEmail;

    private FirebaseUser user;

    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        fStore = FirebaseFirestore.getInstance();

        logout = (Button) findViewById(R.id.logout);

        FloatingActionButton insertNewNote = findViewById(R.id.insertNewNote);
        insertNewNote.setOnClickListener(new View.OnClickListener(){
            @Override public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, InsertNewNote.class));
            }
        });

        logout.setOnClickListener(new View.OnClickListener(){
            @Override public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(HomeActivity.this, MainActivity.class));
            }
        });

        userEmail = (TextView) findViewById(R.id.userEmail);

        user = FirebaseAuth.getInstance().getCurrentUser();
        String email = user.getEmail();
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