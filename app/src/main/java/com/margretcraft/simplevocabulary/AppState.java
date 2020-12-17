package com.margretcraft.simplevocabulary;

import android.app.Application;

import androidx.room.Room;

import com.margretcraft.simplevocabulary.model.BD.WordDB;
import com.margretcraft.simplevocabulary.model.jsonModel.ApiHolder;
import com.margretcraft.simplevocabulary.model.jsonModel.SkyEng;

public class AppState extends Application {
    private static AppState INSTANCE;
    private static WordDB wordDB;
    private static ApiHolder apiHolder;

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
        apiHolder = new ApiHolder();
    }

    public static SkyEng getSkyEngApi() {
        return apiHolder.getSkyEng();
    }

}
