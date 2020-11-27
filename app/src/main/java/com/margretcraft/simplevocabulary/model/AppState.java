package com.margretcraft.simplevocabulary.model;

import android.app.Application;

import androidx.room.Room;

import com.margretcraft.simplevocabulary.model.BD.WordDB;

public class AppState extends Application {
    private static AppState INSTANCE;
    private static WordDB wordDB;

    public static AppState getINSTANCE() {
        return INSTANCE;
    }

    public static WordDB getWordDB() {
        return wordDB;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
        wordDB = Room.databaseBuilder(this, WordDB.class, "words")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build();
    }

}
