package com.example.getauthor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ProgressBar;

import com.example.getauthor.databinding.ActivityMainBinding;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private TextInputLayout layoutSearch;
    private TextInputEditText editSearch;
    private MaterialTextView viewResultBook, viewResultAuthor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        layoutSearch = binding.layoutEditSearch;
        editSearch = binding.editSearch;
        viewResultBook = binding.viewResultBook;
        viewResultAuthor = binding.viewResultAuthor;
        MaterialButton buttonSearch = binding.buttonSearch;
        ProgressBar progressBar = binding.progressBar;

        HideKeyboard hideKeyboard = new HideKeyboard(this, this);
        hideKeyboard.setupUI(binding.getRoot());

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

        buttonSearch.setOnClickListener(view -> {
            String search = Objects.requireNonNull(editSearch.getText()).toString();
            if (TextUtils.isEmpty(search)) {
                layoutSearch.setError("Required");
                return;
            }
            viewResultBook.setText("");
            viewResultAuthor.setText("");
            progressBar.setVisibility(View.VISIBLE);

            if (new CheckConnection(this).checkConn()) {
                new TaskAsyncFetch(viewResultBook, viewResultAuthor, progressBar).execute(search);
            }
            else {
                progressBar.setVisibility(View.GONE);
                Snackbar snackbar = Snackbar.make(binding.getRoot(), "Check your internet connection!", Snackbar.LENGTH_LONG);
                snackbar.setAction("DISMISS", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        snackbar.dismiss();
                    }
                }).setBackgroundTint(getColor(R.color.yellow_page_300))
                        .setActionTextColor(getColor(R.color.black))
                        .show();
            }
        });
    }
}