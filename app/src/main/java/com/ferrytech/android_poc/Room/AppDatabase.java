package com.ferrytech.android_poc.DATABASE;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.ferrytech.android_poc.DAO.UserDao;
import com.ferrytech.android_poc.ENTITY.UserEntity;

@Database(entities = {UserEntity.class}, version = 2) // 👈 version increased
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public abstract UserDao userDao();

    public static synchronized AppDatabase getInstance(Context context){

        if(instance == null){
            instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "user_db"
                    )
                    .fallbackToDestructiveMigration() // 👈 important
                    .build();
        }

        return instance;
    }
}