package com.android.example.pmpproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.example.pmpproject.db.AppDatabase;
import com.android.example.pmpproject.db.User;
import com.android.example.pmpproject.db.UserDao;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.MyViewHolder> {

    private Context context;
    private List<User> userList;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();;

    private FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);

    public UserListAdapter(Context context) {
        this.context = context;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_row, parent,false);


        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserListAdapter.MyViewHolder holder, int position) {
        holder.btnEmail.setText(this.userList.get(position).email);
        holder.btnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppDatabase db = AppDatabase.getDbInstance(context);
                User user = new User();
                UserDao userDao = db.userDao();
                user = userDao.getUserByEmail(holder.btnEmail.getText().toString());

                //holder.progressBar.setVisibility(View.VISIBLE);
                mAuth.signInWithEmailAndPassword(user.email,user.password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Bundle bundle = new Bundle();
                            bundle.putString(FirebaseAnalytics.Param.METHOD, "email_password");
                            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN, bundle);

                            Intent intent =  new Intent(context, HomeActivity.class);
                            context.startActivity(intent);
                            Toast.makeText(view.getContext(), holder.itemView.getContext().getResources().getString(R.string.login_success), Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(view.getContext(), holder.itemView.getContext().getResources().getString(R.string.login_failed), Toast.LENGTH_LONG).show();
                        }
                        //holder.progressBar.setVisibility(View.GONE);
                    }
                });
            }
        });
        holder.btnDeleteSavedUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppDatabase db = AppDatabase.getDbInstance(context);
                UserDao userDao = db.userDao();

                userList.remove(holder.getAdapterPosition());
                userDao.deleteByEmail(holder.btnEmail.getText().toString());
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.userList.size();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder{
        Button btnEmail;
        Button btnDeleteSavedUser;
        //ProgressBar progressBar;

        public MyViewHolder(View view) {
            super(view);
            btnEmail = view.findViewById(R.id.savedUserEmail);
            btnDeleteSavedUser = view.findViewById(R.id.deleteSavedUser);
            //progressBar = view.findViewById(R.id.progressBar);
        }
    }
}
