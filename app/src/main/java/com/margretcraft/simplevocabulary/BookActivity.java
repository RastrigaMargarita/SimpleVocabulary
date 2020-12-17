package com.margretcraft.simplevocabulary;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.margretcraft.simplevocabulary.model.BD.WordDB;
import com.margretcraft.simplevocabulary.model.BD.WordDao;
import com.margretcraft.simplevocabulary.model.Word;

import java.util.ArrayList;

public class BookActivity extends AppCompatActivity {
    ArrayList<Word> wordList;
    RecyclerView recyclerViewBook;
    ImageButton changeTab;
    WordDB db;
    WordDao wordDao;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        recyclerViewBook = findViewById(R.id.recyclerView);
        recyclerViewBook.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewBook.setHasFixedSize(true);

        db = AppState.getWordDB();
        wordDao = db.worddao();

        wordList = (ArrayList<Word>) wordDao.getAll();
        WordsListAdapter wordsListAdapter = new WordsListAdapter(this, wordList);
        recyclerViewBook.setAdapter(wordsListAdapter);

        changeTab = findViewById(R.id.imageButtonTabBook2);
        changeTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
               // startActivity(intent);
              //  closeThisActivity();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
        wordDao = null;
    }

    private void closeThisActivity() {
        finish();
    }

}
