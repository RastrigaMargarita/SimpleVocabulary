package com.margretcraft.simplevocabulary.BD;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.margretcraft.simplevocabulary.Word;

@Database(entities = {Word.class}, version = 1, exportSchema = false)
public abstract class WordDB extends RoomDatabase {
    public abstract WordDao worddao();

}

