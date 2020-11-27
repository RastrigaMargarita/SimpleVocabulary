package com.margretcraft.simplevocabulary.model;

import com.margretcraft.simplevocabulary.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;

public class DownloadWord extends Observable implements Runnable{
    private final String searchingWord;
    private final Observer viewModelObserver;

    public DownloadWord(String searchingWord, Observer viewModelObserver) {
        super();
        this.searchingWord = searchingWord;
        this.viewModelObserver = viewModelObserver;
    }

    @Override
    public void run() {
        URL url = null;
        HttpURLConnection urlConnection = null;
        StringBuilder result = new StringBuilder();
        try {
            url = new URL(AppState.getINSTANCE().getString(R.string.search_line) + searchingWord);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = urlConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(inputStreamReader);

            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                result.append(line);
            }
            reader.close();

            viewModelObserver.update(this, result.toString());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();

            }
        }
    }
}
