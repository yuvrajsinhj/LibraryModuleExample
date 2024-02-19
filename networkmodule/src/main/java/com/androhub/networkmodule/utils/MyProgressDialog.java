package com.androhub.networkmodule.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.androhub.networkmodule.R;


public class MyProgressDialog extends Dialog implements OnDismissListener {
    public MyProgressDialog(Context context) {
        super(context, R.style.CustomAlertDialogStyle);
//        super(context,R.style.CustomDialog);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void show() {

        try {
            super.show();
            setContentView(R.layout.dialog_progress);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        // TODO Auto-generated method stub
        super.dismiss();
    }

}