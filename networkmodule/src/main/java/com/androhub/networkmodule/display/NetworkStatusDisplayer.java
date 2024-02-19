package com.androhub.networkmodule.display;

import android.content.res.Resources;

import androidx.annotation.StringRes;

import com.novoda.merlin.MerlinsBeard;

public class NetworkStatusDisplayer {

    private final Resources resources;
    private final MerlinsBeard merlinsBeard;

    @androidx.annotation.Nullable
    private MerlinSnackbar snackbar;

    public NetworkStatusDisplayer(Resources resources, MerlinsBeard merlinsBeard) {
        this.resources = resources;
        this.merlinsBeard = merlinsBeard;
    }


    public void displayNegativeMessage(@StringRes int messageResource, android.view.View attachTo) {
        try {
            snackbar = MerlinSnackbar.withDuration(resources, attachTo, 2000)
                    .withText(messageResource)
                    .withTheme(new NegativeThemer())
                    .show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void reset() {
        if (snackbar == null) {
            return;
        }
        snackbar.dismiss();
        snackbar = null;
    }

}
