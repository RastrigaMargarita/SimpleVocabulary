package com.margretcraft.simplevocabulary;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.RemoteViews;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

/**
 * Implementation of App Widget functionality.
 */
public class WordRepiterWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        final String WORD_URL = "https://dictionary.skyeng.ru/api/public/v1/words/search?search=mood";


        DownloadMeaningTask task = new DownloadMeaningTask();

        String meaning = null;
        try {
            meaning = task.execute(WORD_URL).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
        Log.i("my", meaning);
        views.setTextViewText(R.id.textViewWord, meaning);
        try {
            JSONObject jsonObject = new JSONObject(meaning);
            //String city = jsonObject.getString("definition");
            String temp = jsonObject.getJSONArray("meanings").getJSONObject(0).getJSONObject("translation").getString("text");

            //String description = jsonObject.getJSONArray("weather").getJSONObject(0).getString("description");
            //String weather = String.format("%s\nРўРµРјРїРµСЂР°С‚СѓСЂР°: %s\nРќР° СѓР»РёС†Рµ: %s", city, temp, description);
            //textViewWeather.setText(weather);
            views.setTextViewText(R.id.textViewWord, temp);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        // CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object


        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    private static class DownloadMeaningTask extends AsyncTask<String, Void, String> {
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
                String line = reader.readLine();
                while (line != null) {
                    result.append(line);
                    line = reader.readLine();
                }
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
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//            try {
//            JSONObject jsonObject = new JSONObject(s);
//            String city = jsonObject.getString("name");
//            String temp = jsonObject.getJSONObject("main").getString("temp");
//            String description = jsonObject.getJSONArray("weather").getJSONObject(0).getString("description");
//            String weather = String.format("%s\nРўРµРјРїРµСЂР°С‚СѓСЂР°: %s\nРќР° СѓР»РёС†Рµ: %s", city, temp, description);
//            textViewWeather.setText(weather);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

    }
}

