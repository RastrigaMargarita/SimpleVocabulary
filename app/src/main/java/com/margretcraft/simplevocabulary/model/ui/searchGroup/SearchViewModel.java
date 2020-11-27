package com.margretcraft.simplevocabulary.model.ui.searchGroup;

import android.app.Activity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.margretcraft.simplevocabulary.model.AppState;
import com.margretcraft.simplevocabulary.model.BD.WordDao;
import com.margretcraft.simplevocabulary.model.DownloadWord;
import com.margretcraft.simplevocabulary.model.Word;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Observable;
import java.util.Observer;

public class SearchViewModel extends ViewModel implements Observer {

    private Activity activity;
    private Word currentWord;
    private final MutableLiveData<Word> currentWordMLD = new MutableLiveData<>();
    private String[] alts;
    private final MutableLiveData<String[]> altsMD = new MutableLiveData<>();
    private final MutableLiveData<Integer> oneClick = new MutableLiveData<>();
    private WordDao wordDao;

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public LiveData<Word> getCurrentWord() {
        return currentWordMLD;
    }

    public MutableLiveData<Integer> getOneClick() {
        return oneClick;
    }
    public MutableLiveData<String[]> getAltsMD() {
        return altsMD;
    }
    public SearchViewModel() {
        oneClick.setValue(0);
        wordDao = AppState.getWordDB().worddao();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        wordDao = null;
    }

    public void search(String searchWord) {

        currentWord = new Word(searchWord);

        //Сначала ищем в базе
        Word wordInBase = wordDao.getByName(searchWord);

        if (wordInBase != null) {
            currentWord = wordInBase;
            currentWordMLD.setValue(currentWord);
            oneClick.setValue(1);
        } else { //Если не нашли, скачиваем с сайта
            new Thread(new DownloadWord(searchWord, this)).start();
        }
    }

    public void addToList() {
        if (wordDao.getByName(currentWord.getWord()) == null) {
            wordDao.insert(currentWord);
        }
    }


    @Override
    public void update(Observable o, Object arg) {

        //ПОлучили данные с сайта, вытаскиваем значения
        String meaning = (String) arg;
        if (((String) meaning).length() < 10) {
            currentWordMLD.setValue(currentWord);
            oneClick.setValue(0);
        } else {
            StringBuilder sb = new StringBuilder();

            JSONArray jsonArray = null;
            try {
                jsonArray = new JSONArray(meaning);

                boolean findword = false;
                for (int j = 0; j < jsonArray.length(); j++) {
                    JSONObject firstJSONobject = jsonArray.getJSONObject(j);
                    if (firstJSONobject.getString("text").equals(currentWord.getWord())) {
                        //Нашлось точно такое слово в словаре

                        JSONArray jsonArrayMeanings = firstJSONobject.getJSONArray("meanings");

                        //Пишем основные переводы
                        for (int i = 0; i < Math.min(jsonArrayMeanings.length(), 5); i++) {
                            sb.append(jsonArrayMeanings.getJSONObject(i).getJSONObject("translation").getString("text"));
                            sb.append(", ");

                        }
                        final String toValue = sb.toString();
                        currentWord.setTranslate(sb.toString());

                        //озвучка пока не работает
                        //currentWord.setPathToSound(jsonArrayMeanings.getJSONObject(0).getString("soundUrl"));

                        //Пишем варианты использования - словосочетания и прочее
                        sb = new StringBuilder();
                        for (int i = 1; i < Math.min(jsonArray.length(), 8); i++) {
                            sb.append(jsonArray.getJSONObject(i).getString("text"));
                            sb.append(" (");
                            sb.append(jsonArray.getJSONObject(i).getJSONArray("meanings").getJSONObject(0).getJSONObject("translation").getString("text"));
                            sb.append(")\n");
                        }

                        currentWord.setExamples(sb.toString());
                        findword = true;
                        break;

                    }
                }
                if (!findword) {//точно такого слова нет но skyeng предложил варианты

                    alts = new String[jsonArray.length()];
                    for (int i = 0; i < jsonArray.length(); i++) {
                        alts[i] = jsonArray.getJSONObject(i).getString("text");
                    }
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            altsMD.setValue(alts);
                        }
                    });
                }

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        currentWordMLD.setValue(currentWord);
                        oneClick.setValue(2);
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}