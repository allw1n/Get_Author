package com.example.getauthor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

    private TextInputLayout layoutSearch;
    private TextInputEditText editSearch;
    private MaterialTextView viewResultBook, viewResultAuthor;
    private ProgressBar progressBar;
    private final static int LOADER_ID = 0;

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
        progressBar = binding.progressBar;

        if (getSupportLoaderManager().getLoader(LOADER_ID) != null) {
            getSupportLoaderManager().initLoader(LOADER_ID, null, this);
        }

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
                //new TaskAsyncFetch(viewResultBook, viewResultAuthor, progressBar).execute(search);

                Bundle queryBundle = new Bundle();
                queryBundle.putString("search", search);
                getSupportLoaderManager().restartLoader(LOADER_ID, queryBundle, this);
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

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        String search = "";
        if (args != null) {
            search = args.getString("search");
        }
        return new LoaderTaskAsyncFetch(this, search);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {
        try {
            JSONObject jsonReceived = new JSONObject(data);
            JSONArray itemsArray = jsonReceived.getJSONArray("items");

            int i = 0;
            String title = null;
            String authors = null;

            while (i < itemsArray.length() && (title == null || authors == null)) {
                JSONObject book = itemsArray.getJSONObject(i);
                JSONObject volumeInfo = book.getJSONObject("volumeInfo");

                try {
                    title = volumeInfo.getString("title");
                    authors = volumeInfo.getString("authors");
                    authors = authors.replaceAll("[\"\\[\\]]", "").replace(",", ", ");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                i++;
            }

            progressBar.setVisibility(View.GONE);
            if (title != null && authors != null) {
                viewResultBook.setText(title);
                viewResultAuthor.setText(R.string.by);
                viewResultAuthor.append(authors);
            } else {
                viewResultBook.setText(R.string.no_result);
            }
        } catch (Exception e) {
            progressBar.setVisibility(View.GONE);
            viewResultBook.setText(R.string.no_result);
            e.printStackTrace();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }
}