package com.example.mpip.model;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Contact.class},version = 1,exportSchema = false)
public abstract class contactDB extends RoomDatabase {

    private static final String DB_NAME = "contactdb007jb";
    private static contactDB instance;

    public static synchronized contactDB getDB(Context context){
     if (instance==null){
         instance = Room.databaseBuilder(context,contactDB.class,DB_NAME)
                 .fallbackToDestructiveMigration().allowMainThreadQueries()
                 .build();
     }
     return instance;
    }

    public abstract ContactDao contactDao();
}
