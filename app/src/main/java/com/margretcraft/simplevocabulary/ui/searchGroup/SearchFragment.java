package com.margretcraft.simplevocabulary.ui.searchGroup;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.margretcraft.simplevocabulary.R;
import com.margretcraft.simplevocabulary.model.Word;

import java.util.ArrayList;

public class SearchFragment extends Fragment {

    private SearchViewModel searchViewModel;
    private EditText editTextWord;
    private TextView textViewTranslate;
    private TextView textViewExamples;
    private ImageButton imageButtonSearch;
    private ListView listViewAlt;
    private ArrayAdapter<String> arrayAdapter;
    private int statePicture;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, final Bundle savedInstanceState) {
        searchViewModel =
                new ViewModelProvider(this).get(SearchViewModel.class);
        searchViewModel.setActivity(getActivity());
        View root = inflater.inflate(R.layout.fragment_search, container, false);

        editTextWord = root.findViewById(R.id.textViewWord);
        editTextWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listViewAlt.setVisibility(View.INVISIBLE);

            }
        });
        editTextWord.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (statePicture!=0){
                statePicture = 0;
                setStatePicture();}
                return false;
            }
        });

        textViewTranslate = root.findViewById(R.id.textViewTranslate);

        textViewExamples = root.findViewById(R.id.textViewExamples);

        imageButtonSearch = root.findViewById(R.id.floatingActionButton);
        imageButtonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                String sendWord = editTextWord.getText().toString().trim();
                if (!sendWord.equals("")) {
                switch (statePicture) {
                    case 0:
                            searchViewModel.search(sendWord);
                        break;
                    case 1:
                        showSnakBar(R.string.AlreadyAdded);
                        break;
                    case 2:
                        searchViewModel.addToList();
                        showSnakBar(R.string.Added);
                        statePicture = 1;
                        setStatePicture();
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + statePicture);
                }
                } else {
                showSnakBar(R.string.TypeAWord);
            }
            }
        });
        listViewAlt = root.findViewById(R.id.recyclerViewAlt);
        listViewAlt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                editTextWord.setText(arrayAdapter.getItem(position));
                listViewAlt.setVisibility(View.INVISIBLE);
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                searchViewModel.search(editTextWord.getText().toString().trim());
            }

        });

        arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, new ArrayList<String>());
        listViewAlt.setAdapter(arrayAdapter);

        searchViewModel.getCurrentWord().observe(getViewLifecycleOwner(), new Observer<Word>() {
            @Override
            public void onChanged(@Nullable Word s) {
                assert s != null;
                setValues(s);
            }
        });
        searchViewModel.getAltsMD().observe(getViewLifecycleOwner(), new Observer<String[]>() {
            @Override
            public void onChanged(String[] strings) {
                arrayAdapter.clear();
                for (String string : strings) {
                    arrayAdapter.add(string);
                }
                arrayAdapter.notifyDataSetChanged();
                listViewAlt.setVisibility(View.VISIBLE);
            }
        });

        searchViewModel.getOneClick().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer gotStatePicture) {
                statePicture = gotStatePicture;
                setStatePicture();

            }
        });
        return root;
    }

    private void setStatePicture() {
        switch (statePicture) {

            case 0:
                imageButtonSearch.setImageDrawable(getActivity().getDrawable(R.drawable.ques));
                break;
            case 1:
                imageButtonSearch.setImageDrawable(getActivity().getDrawable(R.drawable.ok));
                break;
            case 2:
                imageButtonSearch.setImageDrawable(getActivity().getDrawable(R.drawable.save_bw));
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + statePicture);
        }
    }

    private void showSnakBar(int message) {
        Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG).show();
    }

    private void setValues(Word s) {
        if (s.getTranslate() != null) {
            textViewTranslate.setText(s.getTranslate().replace(", ", "\n"));

        }
        if (s.getExamples() != null) {
            textViewExamples.setText(s.getExamples());
        }
    }

}