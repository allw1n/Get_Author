package com.example.getauthor;

import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.material.textview.MaterialTextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

public class TaskAsyncFetch extends AsyncTask<String, Void, String> {

    private final WeakReference<MaterialTextView> viewResultBook, viewResultAuthor;
    private final WeakReference<ProgressBar> progressBar;

    public TaskAsyncFetch(MaterialTextView viewResultBook, MaterialTextView viewResultAuthor, ProgressBar progressBar) {
        this.viewResultBook = new WeakReference<>(viewResultBook);
        this.viewResultAuthor = new WeakReference<>(viewResultAuthor);
        this.progressBar = new WeakReference<>(progressBar);
    }

    @Override
    protected String doInBackground(String... strings) {
        return FetchDetails.getBookInfo(strings[0]);
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
                viewResultBook.get().setText(title);
                viewResultAuthor.get().setText(R.string.by);
                viewResultAuthor.get().append(authors);
            } else {
                viewResultBook.get().setText(R.string.no_result);
            }
        } catch (Exception e) {
            progressBar.get().setVisibility(View.GONE);
            viewResultBook.get().setText(R.string.no_result);
            e.printStackTrace();
        }
    }
}
