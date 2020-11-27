package com.margretcraft.simplevocabulary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.margretcraft.simplevocabulary.model.Word;

import java.util.ArrayList;

public class WordsListAdapter extends RecyclerView.Adapter<WordsListAdapter.AdapterHolder> {
    private final LayoutInflater inflater;
    ArrayList<Word> wordList;


    public WordsListAdapter(Context context, ArrayList<Word> wordList) {
        this.inflater = LayoutInflater.from(context);
        this.wordList = wordList;
    }

    @NonNull
    @Override
    public AdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.word_line, parent, false);

        return new AdapterHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WordsListAdapter.AdapterHolder holder, int position) {
       holder.textViewWord.setText(wordList.get(position).getWord());
       holder.textViewTranslates.setText(wordList.get(position).getTranslate());
    }

    @Override
    public int getItemCount() {
        return wordList.size();
    }


    public class AdapterHolder extends RecyclerView.ViewHolder {

       TextView textViewWord;
       TextView textViewTranslates;
       TextView textViewExamples;

        public AdapterHolder(@NonNull View itemView) {
            super(itemView);
            textViewWord = itemView.findViewById(R.id.textViewWordC);
            textViewTranslates = itemView.findViewById(R.id.textViewTranslatesC);
            textViewExamples = itemView.findViewById(R.id.textViewExamplesC);
        }
    }
}
