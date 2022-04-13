package com.example.getauthor;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.appcompat.app.AppCompatActivity;

public class CheckConnection {
    AppCompatActivity activity;

    public CheckConnection(AppCompatActivity activity) {
        this.activity = activity;
    }

    public boolean checkConn() {
        ConnectivityManager connManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connManager != null) networkInfo = connManager.getActiveNetworkInfo();
        if (networkInfo == null) return false;
        else return networkInfo.isConnected();
    }
}
