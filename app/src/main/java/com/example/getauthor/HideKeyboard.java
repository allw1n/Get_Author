package com.example.getauthor;

import android.app.Activity;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;

public class HideKeyboard {

    private final Context context;
    private final Activity activity;

    public HideKeyboard(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    public void setupUI(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (view instanceof TextInputEditText || view instanceof MaterialAutoCompleteTextView) {
            toggleKeyboard(view);
        } else {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(activity);
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        if(inputMethodManager.isAcceptingText()) {
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(), 0
            );
        }
    }

    public void toggleKeyboard(View view) {
        if (view instanceof TextInputEditText) {
            view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean hasFocus) {
                    if (hasFocus) {
                        ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE))
                                .showSoftInput(view, InputMethodManager.SHOW_FORCED);
                    } else {
                        ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE))
                                .hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                }
            });
        } else {
            view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean hasFocus) {
                    ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            });
        }
    }
}
