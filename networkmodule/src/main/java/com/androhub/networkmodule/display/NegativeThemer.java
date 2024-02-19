package com.androhub.networkmodule.display;

import android.content.res.Resources;

import com.google.android.material.snackbar.Snackbar;

class NegativeThemer implements Themer {

    @Override
    public void applyTheme(Resources resources, Snackbar snackbar) {
        Snackbar.SnackbarLayout snackbarView = (Snackbar.SnackbarLayout) snackbar.getView();


    }

}
