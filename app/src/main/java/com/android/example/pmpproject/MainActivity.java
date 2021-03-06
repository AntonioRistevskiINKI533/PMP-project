package com.android.example.pmpproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.example.pmpproject.db.AppDatabase;
import com.android.example.pmpproject.db.User;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int GOOGLE_SIGN_IN_CODE = 10005;
    private TextView register, savedUsers;
    private EditText editTextEmail, editTextPassword;
    private ProgressBar progressBar;
    private Button login;

    private ImageView loginAnonymously, loginWithGoogle, loginWithFacebook;

    private FirebaseAuth mAuth;

    private FirebaseAnalytics mFirebaseAnalytics;

    CallbackManager callbackManager;

    GoogleSignInOptions gso;
    GoogleSignInClient signInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        startActivity(new Intent(MainActivity.this,HomeActivity.class));
                        Toast.makeText(MainActivity.this, getResources().getString(R.string.login_success), Toast.LENGTH_LONG).show();
                        finish();
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(MainActivity.this, getResources().getString(R.string.login_failed), Toast.LENGTH_LONG).show();
                    }
                });

        register = (TextView) findViewById(R.id.register);
        register.setOnClickListener(this);

        savedUsers = (TextView) findViewById(R.id.savedUsers);
        savedUsers.setOnClickListener(this);

        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(this);

        editTextEmail = (EditText) findViewById(R.id.email);
        editTextPassword = (EditText) findViewById(R.id.password);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        loginAnonymously = (ImageView) findViewById(R.id.loginAnonymously);
        loginAnonymously.setOnClickListener(this);

        loginWithGoogle = (ImageView) findViewById(R.id.loginWithGoogle);
        loginWithGoogle.setOnClickListener(this);

        loginWithFacebook = (ImageView) findViewById(R.id.loginWithFacebook);
        loginWithFacebook.setOnClickListener(this);

        //Google signIn
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        signInClient = GoogleSignIn.getClient(this,gso);

        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        if(signInAccount != null){
            Toast.makeText(this,getResources().getString(R.string.user_is_already_logged),Toast.LENGTH_SHORT).show();

        }

        //Messaging
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            ///Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and toast
                        //String msg = getString(R.string.msg_token_fmt, token);
                        //Log.d(TAG, msg);
                        System.out.println(token);
                        //Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register:
                startActivity(new Intent(this,RegisterActivity.class));
                break;
            case R.id.login:
                login();
                break;
            case R.id.loginAnonymously:
                loginAnonymously();
                break;
            case R.id.loginWithGoogle:
                loginWithGoogle();
                break;
            case R.id.loginWithFacebook:
                loginWithFacebook();
                break;
            case R.id.savedUsers:
                startActivity(new Intent(this,SavedUsersActivity.class));
                break;
        }
    }

    private void login() {
        String email= editTextEmail.getText().toString().trim();
        String password= editTextPassword.getText().toString().trim();

        if(email.isEmpty()){
            editTextEmail.setError(getResources().getString(R.string.missing_email));
            editTextEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError(getResources().getString(R.string.invalid_email));
            editTextEmail.requestFocus();
            return;
        }

        if(password.isEmpty()){
            editTextPassword.setError(getResources().getString(R.string.missing_password));
            editTextPassword.requestFocus();
            return;
        }

/*        if(password.length() < 8){
            editTextPassword.setError(getResources().getString(R.string.invalid_password));
            editTextPassword.requestFocus();
            return;
        }*/

        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            Bundle bundle = new Bundle();
                            bundle.putString(FirebaseAnalytics.Param.METHOD, "email_password");
                            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN, bundle);

                            startActivity(new Intent(MainActivity.this,HomeActivity.class));
                            Toast.makeText(MainActivity.this, getResources().getString(R.string.login_success), Toast.LENGTH_LONG).show();
                            insertLoginData(email, password);
                        }else{
                            Toast.makeText(MainActivity.this, getResources().getString(R.string.login_failed), Toast.LENGTH_LONG).show();
                        }
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }

    private void insertLoginData(String email, String password) {
        AppDatabase db = AppDatabase.getDbInstance(this.getApplicationContext());

        User result = db.userDao().getUserByEmail(email);
        if(result == null){
            com.android.example.pmpproject.db.User user = new com.android.example.pmpproject.db.User();
            user.email = email;
            user.password = password;
            db.userDao().insertUser(user);
        }

    }

    private void loginAnonymously() {
        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    Bundle bundle = new Bundle();
                    bundle.putString(FirebaseAnalytics.Param.METHOD, "anonymously");
                    mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN, bundle);

                    startActivity(new Intent(MainActivity.this,HomeActivity.class));
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.login_success), Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.login_failed), Toast.LENGTH_LONG).show();
                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void loginWithFacebook() {

        progressBar.setVisibility(View.VISIBLE);

        LoginManager.getInstance().logInWithReadPermissions(MainActivity.this, Arrays.asList("public_profile"));

        progressBar.setVisibility(View.GONE);

    }

    private void loginWithGoogle() {

        progressBar.setVisibility(View.VISIBLE);

        Intent sign = signInClient.getSignInIntent();
        startActivityForResult(sign, GOOGLE_SIGN_IN_CODE);

        progressBar.setVisibility(View.GONE);
    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);//fb
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GOOGLE_SIGN_IN_CODE){
            Task<GoogleSignInAccount> signInTask = GoogleSignIn.getSignedInAccountFromIntent(data);
            try{
                GoogleSignInAccount signInAcc = signInTask.getResult(ApiException.class);
                //signInTask.getResult(ApiException.class);
                //finish();
                Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
                startActivity(intent);
            }catch(ApiException e){
                Toast.makeText(this,getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
            }


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