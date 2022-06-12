package com.android.example.pmpproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.example.pmpproject.db.AppDatabase;
import com.android.example.pmpproject.db.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class SavedUsersActivity extends AppCompatActivity implements View.OnClickListener {

    private UserListAdapter userListAdapter;

    List<com.android.example.pmpproject.db.User> userList;

    private ProgressBar progressBar;
    private Button login, deleteSavedUser, goBack;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_users);

        mAuth = FirebaseAuth.getInstance();

        goBack = (Button) findViewById(R.id.goBack);
        goBack.setOnClickListener(this);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        initRecyclerView();
        loadUserList();
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager((this)));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        userListAdapter =new UserListAdapter(this);
        recyclerView.setAdapter(userListAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.goBack:
                startActivity(new Intent(this,MainActivity.class));
                break;
            case R.id.login:
                login();
                break;
        }
    }

    private void loadUserList() {
        AppDatabase db = AppDatabase.getDbInstance(this.getApplicationContext());

        userList = db.userDao().getAllUsers();

        userListAdapter.setUserList(userList);
    }

    private void login() {
        String email= "dwa";//editTextEmail.getText().toString().trim();
        String password= "dwad";//editTextPassword.getText().toString().trim();

        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    startActivity(new Intent(SavedUsersActivity.this,HomeActivity.class));
                    Toast.makeText(SavedUsersActivity.this, getResources().getString(R.string.login_success), Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(SavedUsersActivity.this, getResources().getString(R.string.login_failed), Toast.LENGTH_LONG).show();
                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void deleteUser(String email, String password) {
        AppDatabase db = AppDatabase.getDbInstance(this.getApplicationContext());

        com.android.example.pmpproject.db.User user = new com.android.example.pmpproject.db.User();
        user.email = email;
        user.password = password;

        db.userDao().delete(user);
        //finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        if(requestCode == 100){
            loadUserList();
        }

        super.onActivityResult(requestCode, resultCode, data);
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