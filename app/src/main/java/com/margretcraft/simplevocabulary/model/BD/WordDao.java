package com.margretcraft.simplevocabulary.model.BD;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.margretcraft.simplevocabulary.model.Word;

import java.util.List;

@Dao
public interface WordDao {
    @Query("SELECT * FROM word")
    List<Word> getAll();

    @Query("SELECT * FROM word WHERE _id = :id")
    LiveData<Word> getById(long id);

    @Query("SELECT * FROM word WHERE word = :word")
    Word getByName(String word);

    @Insert
    void insert(Word word);

    @Update
    void update(Word word);

    @Delete
    void delete(Word word);

}

