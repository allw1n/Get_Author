package com.example.getauthor;

import android.os.AsyncTask;

import com.google.android.material.textview.MaterialTextView;

import java.lang.ref.WeakReference;

public class FetchDetails extends AsyncTask<String, Void, String> {

    private WeakReference<MaterialTextView> viewBookName, viewAuthorName;

    public FetchDetails(MaterialTextView viewBookName, MaterialTextView viewAuthorName) {
        this.viewBookName = new WeakReference<>(viewBookName);
        this.viewAuthorName = new WeakReference<>(viewAuthorName);
    }

    @Override
    protected String doInBackground(String... strings) {
        return NetworkUtils.getBookInfo(strings[0]);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}
