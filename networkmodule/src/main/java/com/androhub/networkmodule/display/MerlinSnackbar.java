package com.androhub.networkmodule.display;

import android.content.res.Resources;

import androidx.annotation.IntegerRes;
import androidx.annotation.StringRes;

import com.google.android.material.snackbar.Snackbar;


class MerlinSnackbar {

    private static final String EMPTY_MESSAGE = "";
    private final Snackbar snackbar;

    @androidx.annotation.NonNull
    static MerlinSnackbar withDuration(Resources resources, android.view.View attachTo, @IntegerRes int durationResource) {
        int duration = resources.getInteger(durationResource);
        Snackbar snackbar = Snackbar.make(attachTo, EMPTY_MESSAGE, duration);
        return new MerlinSnackbar(snackbar);
    }

    private MerlinSnackbar(Snackbar snackbar) {
        this.snackbar = snackbar;
    }

    MerlinSnackbar withTheme(Themer themer) {
        themer.applyTheme(snackbar.getContext().getResources(), snackbar);
        return this;
    }



    MerlinSnackbar withText(@StringRes int messageResource) {
        snackbar.setText(messageResource);
        return this;
    }

    MerlinSnackbar show() {
        if (!snackbar.isShown()) {
            snackbar.show();
        }
        return this;
    }

    void dismiss() {
        snackbar.dismiss();
    }
}
