package com.androhub.networkmodule.uc;

import android.text.Editable;
import android.text.TextWatcher;

public class CustomTextWatcher implements TextWatcher {
    private TextWatcherListener listener;
    private int edTag;

    public CustomTextWatcher(int edTag, TextWatcherListener listener) {
        this.listener = listener;
        this.edTag = edTag;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        listener.onTextChanged(edTag, String.valueOf(charSequence));
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    public interface TextWatcherListener{
        void onTextChanged(int edTag, String text);
    }
}
