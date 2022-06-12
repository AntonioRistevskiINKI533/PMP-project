package com.android.example.pmpproject.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User {

        @PrimaryKey(autoGenerate = true)
        public int uid;

        @ColumnInfo(name = "email")
        public String email;

        @ColumnInfo(name = "password")
        public String password;
}
