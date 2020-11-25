package com.margretcraft.simplevocabulary;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.margretcraft.simplevocabulary.BD.WordDB;
import com.margretcraft.simplevocabulary.BD.WordDao;

import java.util.ArrayList;

public class BookActivity extends AppCompatActivity {
    ArrayList<Word> wordList;
    RecyclerView recyclerViewBook;
   // WordDBHelper wordDBHelper;
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

        db = AppState.getINSTANCE().getWordDB();
        wordDao = db.worddao();

        wordList = (ArrayList<Word>) wordDao.getAll();
        WordsListAdapter wordsListAdapter = new WordsListAdapter(this, wordList);
        recyclerViewBook.setAdapter(wordsListAdapter);

        //wordDBHelper = new WordDBHelper(getApplicationContext());

        changeTab = findViewById(R.id.imageButtonTabBook2);
        changeTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(intent);
                closeThisActivity();
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

//    private void readBD() {
//        SQLiteDatabase database = wordDBHelper.getReadableDatabase();
//        Cursor cursor = database.query(WordConstract.WordEntry.TABLE_NAME, null, null, null, null, "", "");
//        while (cursor.moveToNext()) {
//            wordList.add(new Word(cursor.getString(cursor.getColumnIndex(WordConstract.WordEntry.COLUMN_WORD)),
//                    cursor.getString(cursor.getColumnIndex(WordConstract.WordEntry.COLUMN_TRANSLATE)),
//                    cursor.getString(cursor.getColumnIndex(WordConstract.WordEntry.COLUMN_EXAMPLES)),
//                    cursor.getInt(cursor.getColumnIndex(WordConstract.WordEntry.COLUMN_REPITING)),
//                    cursor.getString(cursor.getColumnIndex(WordConstract.WordEntry.COLUMN_PATHTOSOUND)),
//                    cursor.getInt(cursor.getColumnIndex(WordConstract.WordEntry._ID))));
//
//        }
//        cursor.close();
//        database.close();
//    }
}
