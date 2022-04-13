package com.example.getauthor;

import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.material.textview.MaterialTextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.List;

public class FetchDetails extends AsyncTask<String, Void, String> {

    private final WeakReference<MaterialTextView> viewBookName, viewAuthorName;
    private final WeakReference<ProgressBar> progressBar;

    public FetchDetails(MaterialTextView viewBookName, MaterialTextView viewAuthorName, ProgressBar progressBar) {
        this.viewBookName = new WeakReference<>(viewBookName);
        this.viewAuthorName = new WeakReference<>(viewAuthorName);
        this.progressBar = new WeakReference<>(progressBar);
    }

    @Override
    protected String doInBackground(String... strings) {
        return NetworkUtils.getBookInfo(strings[0]);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try {
            JSONObject jsonReceived = new JSONObject(s);
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

            progressBar.get().setVisibility(View.GONE);
            if (title != null && authors != null) {
                viewBookName.get().setText(title);
                viewAuthorName.get().setText(R.string.by);
                viewAuthorName.get().append(authors);
            } else {
                viewBookName.get().setText(R.string.no_result);
            }
        } catch (Exception e) {
            progressBar.get().setVisibility(View.GONE);
            viewBookName.get().setText(R.string.no_result);
            e.printStackTrace();
        }
    }
}
