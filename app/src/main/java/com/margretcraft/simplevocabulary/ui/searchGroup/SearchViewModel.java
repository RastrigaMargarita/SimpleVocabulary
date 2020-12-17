package com.margretcraft.simplevocabulary.ui.searchGroup;

import android.app.Activity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.margretcraft.simplevocabulary.AppState;
import com.margretcraft.simplevocabulary.model.BD.WordDao;
import com.margretcraft.simplevocabulary.model.Word;
import com.margretcraft.simplevocabulary.model.jsonModel.Meaning;
import com.margretcraft.simplevocabulary.model.jsonModel.RequestWord;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchViewModel extends ViewModel {

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
            AppState.getSkyEngApi().loadWord(searchWord).enqueue(new Callback<RequestWord[]>() {
                @Override
                public void onResponse(Call<RequestWord[]> call, Response<RequestWord[]> response) {
                    updateData(response.body());
                }

                @Override
                public void onFailure(Call<RequestWord[]> call, Throwable t) {
                    currentWordMLD.setValue(currentWord);
                    oneClick.setValue(0);
                }
            });
        }
    }

    public void addToList() {
        if (wordDao.getByName(currentWord.getWord()) == null) {
            wordDao.insert(currentWord);
        }
    }


    public void updateData(RequestWord[] jsonArray) {

        StringBuilder sb = new StringBuilder();

        boolean findword = false;

        for (int j = 0; j < jsonArray.length; j++) {

            if (jsonArray[j].getText().equals(currentWord.getWord())) {
                //Нашлось точно такое слово в словаре

                Meaning[] meanings = jsonArray[j].getMeanings();

                for (int i = 0; i < Math.min(jsonArray[j].getMeanings().length, 5); i++) {
                    sb.append(meanings[i].getTranslation().getText());
                    sb.append(", ");

                }
                currentWord.setTranslate(sb.toString());

                //озвучка пока не работает
                currentWord.setPathToSound(meanings[0].getSoundUrl());

                //Пишем варианты использования - словосочетания и прочее
                sb = new StringBuilder();
                for (int i = 1; i < Math.min(jsonArray.length, 8); i++) {
                    sb.append(jsonArray[i].getText());
                    sb.append(" (");
                    sb.append(jsonArray[i].getMeanings()[0].getTranslation().getText());
                    sb.append(")\n");
                }

                currentWord.setExamples(sb.toString());
                findword = true;
                break;

            }
        }
        if (!findword) {//точно такого слова нет но skyeng предложил варианты

            alts = new String[jsonArray.length];
            for (int i = 0; i < jsonArray.length; i++) {
                alts[i] = jsonArray[i].getText();
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
    }
}

