package com.androhub.networkmodule.utils

import android.annotation.TargetApi
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import com.androhub.networkmodule.utils.prefrence.SharedPreferenceManager


class PermissionActivity : Activity() {

    internal var Permission: String? = ""

    @TargetApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        init(savedInstanceState)
        try {
            requestPermissions(
                arrayOf<String>(this.Permission!!), 55)
        }catch (e:Exception){
            return

        }
    }

    private fun init(state: Bundle?) {
        if (state != null) {
            Permission = state.getString("Permission")
        } else {
            val intent = intent
            Permission = intent.getStringExtra("Permission")
        }
    }

    public override fun onSaveInstanceState(state: Bundle) {
        state.putString("Permission", Permission)
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        SharedPreferenceManager.setBoolean(permissions[0], true)

        val intent = Intent()
        // TODO avt : 8/8/17 change action string here and in manifest
        intent.action = "com.dooree.PERMISSION_RESULT_INTENT"
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            intent.putExtra("Permission", true)
        } else {
            intent.putExtra("Permission", false)
        }
        sendBroadcast(intent)
        finish()
    }
    override fun finish() {
        super.finish()
        overridePendingTransition(0, 0)
    }

}