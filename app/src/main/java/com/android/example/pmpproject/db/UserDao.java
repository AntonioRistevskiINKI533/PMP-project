package com.android.example.pmpproject.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserDao {

    @Query("SELECT * FROM user")
    List<User> getAllUsers();

    @Query("SELECT * FROM user WHERE user.email = :email")
    User getUserByEmail(String email);

    @Query("DELETE FROM user WHERE user.email = :email")
    void deleteByEmail(String email);

    @Insert
    void insertUser(User... users);

    @Delete
    void delete(User user);
}
