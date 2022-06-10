package com.android.example.pmpproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView goBackToLogin, registerUser;
    private EditText editTextEmail, editTextPassword;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        goBackToLogin = (TextView) findViewById(R.id.goBackToLogin);
        goBackToLogin.setOnClickListener(this);

        registerUser = (Button) findViewById(R.id.register);
        registerUser. setOnClickListener(this);

        editTextEmail = (EditText) findViewById(R.id.email);
        editTextPassword = (EditText) findViewById(R.id.password);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.goBackToLogin:
                startActivity(new Intent(this,MainActivity.class));
                break;
            case R.id.register:
                register();
                break;
        }
    }

    private void register() {
        String email= editTextEmail.getText().toString().trim();
        String password= editTextPassword.getText().toString().trim();

        if(email.isEmpty()){
            editTextEmail.setError("Email is required!");
            editTextEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Please provide valid email!");
            editTextEmail.requestFocus();
            return;
        }

        if(password.isEmpty()){
            editTextPassword.setError("Password is required!");
            editTextPassword.requestFocus();
            return;
        }

        if(password.length() < 8){
            editTextPassword.setError("Min password length should be 8 characters!");
            editTextPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful()){
                    //    User user = new User(email);

                    //    FirebaseDatabase.getInstance().getReference("Users")
                    //            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    //            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                    //                @Override
                    //                public void onComplete(@NonNull Task<Void> task) {

                    //                    if(task.isSuccessful()){
                                            Toast.makeText(RegisterActivity.this, "User has been register successfully!", Toast.LENGTH_LONG).show();
                    //                    }else{
                    //                        Toast.makeText(RegisterActivity.this, "Failed to register! try again!", Toast.LENGTH_LONG).show();
                    //                    }
                                       progressBar.setVisibility(View.GONE);
                    //                }
                    //    });
                    }else{
                        Toast.makeText(RegisterActivity.this, "Failed to register!", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }
                }
            });
    }
}