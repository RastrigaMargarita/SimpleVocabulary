package com.margretcraft.simplevocabulary;

import android.app.Application;

import androidx.room.Room;

import com.margretcraft.simplevocabulary.BD.WordDB;

public class AppState extends Application {
    private static AppState INSTANCE;
    private static WordDB wordDB;

   // ArrayList<Word> wordList;

  //  public ArrayList<Word> getWordList() {
  //      return wordList;
  //  }

   // public void setWordList(ArrayList<Word> wordList) {
  //      this.wordList = wordList;
  //  }


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
