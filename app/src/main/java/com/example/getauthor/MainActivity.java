package com.example.getauthor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.example.getauthor.databinding.ActivityMainBinding;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private TextInputLayout layoutSearch;
    private TextInputEditText editSearch;
    private MaterialTextView viewResultBook, viewResultAuthor;
    private MaterialButton buttonSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        layoutSearch = binding.layoutEditSearch;
        editSearch = binding.editSearch;
        viewResultBook = binding.viewResultBook;
        viewResultAuthor = binding.viewResultAuthor;
        buttonSearch = binding.buttonSearch;

        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (layoutSearch.isErrorEnabled()) {
                    layoutSearch.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String search = Objects.requireNonNull(editSearch.getText()).toString();
                new FetchDetails(viewResultBook, viewResultAuthor).execute(search);
            }
        });
    }
}