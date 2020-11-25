package com.margretcraft.simplevocabulary;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.margretcraft.simplevocabulary.BD.WordDB;
import com.margretcraft.simplevocabulary.BD.WordDao;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class SearchActivity extends AppCompatActivity {

    EditText editTextWord;
    TextView textViewTranslate;
    TextView textViewExamples;
    ImageButton changeTab;
    ImageButton buttonSound;
    ImageButton imageButtonSave;
    ImageButton imageButtonSearch;
    MediaPlayer mediaPlayer;

    ListView listViewAlt;

    ArrayAdapter<String> arrayAdapter;

    Word currentWord;
    WordDB db;
    WordDao wordDao;
    //WordDBHelper wordDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        //wordDBHelper = new WordDBHelper(getApplicationContext());

        editTextWord = findViewById(R.id.textViewWord);
        editTextWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listViewAlt.setVisibility(View.INVISIBLE);
            }
        });

        textViewTranslate = findViewById(R.id.textViewTranslate);
        textViewExamples = findViewById(R.id.textViewExamples);

        changeTab = findViewById(R.id.imageButtonTabBook);
        changeTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BookActivity.class);
                startActivity(intent);
                closeThisActivity();
            }
        });

        mediaPlayer = new MediaPlayer();
        buttonSound = findViewById(R.id.imageButton);

        imageButtonSave = findViewById(R.id.imageButtonSave);
        imageButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToList();
                editTextWord.clearFocus();
            }
        });
        imageButtonSearch = findViewById(R.id.imageButtonSearch);
        imageButtonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search();

            }
        });
        listViewAlt = findViewById(R.id.recyclerViewAlt);
        listViewAlt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                editTextWord.setText(arrayAdapter.getItem(position));
                listViewAlt.setVisibility(View.INVISIBLE);
                search();
            }

        });

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new ArrayList<String>());
        listViewAlt.setAdapter(arrayAdapter);

        db = AppState.getINSTANCE().getWordDB();
        wordDao = db.worddao();
       // wordList = new ArrayList<>();
       // readBD();

    }

    private void closeThisActivity() {
        finish();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.release();
        db.close();
        wordDao = null;

    }

    public void search() {
        InputMethodManager imm = (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow( this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        String searchWord = editTextWord.getText().toString().trim();
        currentWord = new Word(searchWord);
        //Сначала ищем в базе
        boolean findword = false;
       // for (int i = 0; i < AppState.getInstance().getWordList().size(); i++) {



            //Word wordInBase = new Thread(new Runnable(){
            //    @Override
            //    public void run() {
        Word wordInBase =  wordDao.getByName(searchWord);
            //    }}).start();

            if (wordInBase!=null) {

                textViewTranslate.setText(wordInBase.getTranslate().replace(", ", "\n"));
                textViewExamples.setText(wordInBase.getExamples());

                imageButtonSave.setImageResource(R.drawable.ok);

            }else{

            DownloadMeaningTask task = new DownloadMeaningTask();

            String meaning = null;
            try {
                meaning = task.execute(getString(R.string.search_line) + searchWord).get();
                if (meaning.length() < 10) {
                    textViewExamples.setText(R.string.empty_answer);
                    imageButtonSave.setImageResource(R.drawable.ques);
                } else {
                    StringBuilder sb = new StringBuilder();

                    JSONArray jsonArray = new JSONArray(meaning);
                    findword = false;
                    for (int j = 0; j <jsonArray.length() ; j++) {
                        JSONObject firstJSONobject = jsonArray.getJSONObject(j);
                        if (firstJSONobject.getString("text").equals(searchWord)) {//Нашлось точно такое слово в словаре


                            JSONArray jsonArrayMeanings = firstJSONobject.getJSONArray("meanings");

                            //Здесь пишем основные переводы
                            for (int i = 0; i < Math.min(jsonArrayMeanings.length(), 5); i++) {
                                sb.append(jsonArrayMeanings.getJSONObject(i).getJSONObject("translation").getString("text"));
                                sb.append("\n");

                            }

                            textViewTranslate.setText(sb.toString());
                            currentWord.setTranslate(sb.toString().replace("\n", ", "));

                            //озвучка
                            currentWord.setPathToSound(jsonArrayMeanings.getJSONObject(0).getString("soundUrl"));

                            //Здесь варианты использования - словосочетания и прочее
                            sb = new StringBuilder();
                            for (int i = 1; i < Math.min(jsonArray.length(), 6); i++) {
                                sb.append(jsonArray.getJSONObject(i).getString("text"));
                                sb.append(" (");
                                sb.append(jsonArray.getJSONObject(i).getJSONArray("meanings").getJSONObject(0).getJSONObject("translation").getString("text"));
                                sb.append(")\n");
                            }
                            textViewExamples.setText(sb.toString());
                            currentWord.setExamples(sb.toString());
                            findword = true;
                            break;

                        }




                    }
                    if (!findword) {//точно такого слова нет но skyeng предложил варианты
                        //alts = new String[jsonArray.length()];
                        arrayAdapter.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            arrayAdapter.add(jsonArray.getJSONObject(i).getString("text"));
                        }
                        arrayAdapter.notifyDataSetChanged();
                        listViewAlt.setVisibility(View.VISIBLE);

                        //arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, alts);
                        //listViewAlt.setAdapter(arrayAdapter);
                    }

                    imageButtonSave.setImageResource(R.drawable.selector_save);
                }

            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void playSound(View view) {
        //String soundStr = "//d2fmfepycn0xw0.cloudfront.net?gender=male&accent=british&text=moon";
        //(new PressButton()).start();

    }

    public void addToList() {

        if (wordDao.getByName(currentWord.getWord())==null) {
           // new Thread(new Runnable(){
           //     @Override
           //     public void run() {
                    wordDao.insert(currentWord);
           //     }}).start();

        }


//            //currentWord.setID(wordList.size() + 1);
//            //wordList.add(currentWord);
//            SQLiteDatabase database = wordDBHelper.getWritableDatabase();
//            ContentValues contentValues = new ContentValues();
//            contentValues.put(WordConstract.WordEntry.COLUMN_WORD, currentWord.getWord());
//            contentValues.put(WordConstract.WordEntry.COLUMN_TRANSLATE, currentWord.getTranslate());
//            contentValues.put(WordConstract.WordEntry.COLUMN_EXAMPLES, currentWord.getExamples());
//            contentValues.put(WordConstract.WordEntry.COLUMN_REPITING, currentWord.getRepiting());
//            contentValues.put(WordConstract.WordEntry.COLUMN_PATHTOSOUND, currentWord.getPathToSound());
//
//            database.insert(WordConstract.WordEntry.TABLE_NAME, null, contentValues);
//            database.close();

            imageButtonSave.setImageResource(R.drawable.ok);
            //recyclerView.getAdapter().notifyDataSetChanged();


    }

    private class DownloadMeaningTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            URL url = null;
            HttpURLConnection urlConnection = null;
            StringBuilder result = new StringBuilder();
            try {
                url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = urlConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(inputStreamReader);

                for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                    result.append(line);
                }
                reader.close();
                return result.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();

                }
            }
            return null;
        }

    }
}