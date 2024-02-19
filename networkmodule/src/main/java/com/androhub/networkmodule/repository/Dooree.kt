package com.mylib.repository

import android.content.Context
import android.content.Intent
import com.androhub.networkmodule.mvvm.ui.BranchActivity
import com.androhub.networkmodule.utils.Constant

class Dooree {
    companion object {
        fun getInstance(context: Context, merchant_id: String, secret_key: String, phone: String, iso_code: String): Dooree {

            if (!merchant_id.isNullOrEmpty() &&!secret_key.isNullOrEmpty() &&!phone.isNullOrEmpty() &&!iso_code.isNullOrEmpty()){
                val intent = Intent(context, BranchActivity::class.java)
                intent.putExtra(
                    Constant.INTENT_EXTRA.USER_MERCHANT_ID,
                    merchant_id
                )
                intent.putExtra(
                    Constant.INTENT_EXTRA.USER_SECRET_KEY,
                    secret_key
                )
                intent.putExtra(Constant.INTENT_EXTRA.USER_PHONE_KEY, phone)
                intent.putExtra(Constant.INTENT_EXTRA.USER_ISO_KEY, iso_code)
                context.startActivity(intent)

            }

            return Dooree()
        }
    }


}