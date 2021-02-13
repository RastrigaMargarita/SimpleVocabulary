package com.margretcraft.simplevocabulary.ui.bookGroup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.margretcraft.simplevocabulary.R;


public class BookFragment extends Fragment {

    private BookViewModel bookViewModel;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        bookViewModel =
                new ViewModelProvider(this).get(BookViewModel.class);
        View root = inflater.inflate(R.layout.fragment_book, container, false);
        final TextView textView = root.findViewById(R.id.text_notifications);
        bookViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });



        return root;
    }
}