package com.androhub.networkmodule.utils

import android.annotation.TargetApi
import android.app.Activity
import android.content.*
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import com.androhub.networkmodule.R
import com.androhub.networkmodule.utils.prefrence.SharedPreferenceManager


class PermissionUtils {

    interface PermissionListener {
        fun granted()

        fun denied()
    }
    companion object {
        private var listener: PermissionListener? = null


        /**
         * Listener to notify permission request result
         */


        /**
         * Check whether we have required permission or not.
         * if not it will ask for permission or display rational msg box if user restricted permission
         * by selecting "do not ask again"
         * All the @[RuntimePermission] must use this function to take permission.
         *
         * @param activity        Activity
         * @param Permission      @[android.Manifest.permission] String
         * @param rationalMessage msg to display
         * @param Onlistener      listener to call on result
         */

        @TargetApi(Build.VERSION_CODES.M)
        fun askPermission(
            activity: Activity,
            Permission: String,
            rationalMessage: String,
            Onlistener: PermissionListener
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val hasWriteContactsPermission = activity.checkSelfPermission(Permission)
                if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
                    if (activity.shouldShowRequestPermissionRationale(Permission)) {
                        val intent = Intent(activity.applicationContext, PermissionActivity::class.java)
                        intent.putExtra("Permission", Permission)
                        activity.startActivity(intent)
                        listener = Onlistener
                        registerRecever(activity)
                    } else {
                        if (SharedPreferenceManager.getBooleanPreference(Permission, false)) {
                            Utility.getConfirmationDialog(activity,
                                activity.getString(R.string.dialog_title_permission),
                                rationalMessage,
                                activity.getString(R.string.settings_caps),
                                activity.getString(R.string.cancel_caps),
                                DialogInterface.OnClickListener { dialog, which ->
                                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                    val uri = Uri.fromParts("package", activity.packageName, null)
                                    intent.data = uri
                                    activity.startActivityForResult(intent, 420)
                                    Utility.showErrorToast(
                                        activity,
                                        activity.getString(R.string.please_grant_permission)
                                    )
                                    dialog.dismiss()
                                },
                                DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() }).show()
                        } else {
                            val intent = Intent(activity.applicationContext, PermissionActivity::class.java)
                            intent.putExtra("Permission", Permission)
                            activity.startActivity(intent)
                            listener = Onlistener
                            registerRecever(activity)
                        }
                    }

                } else {
                    Onlistener.granted()
                }
            } else {
                Onlistener.granted()
            }

        }


        private fun registerRecever(activity: Activity) {
            val filter1 = IntentFilter("com.dooree.PERMISSION_RESULT_INTENT")
            activity.registerReceiver(receiver(), filter1)
        }


        class receiver : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                try {
                    if (intent.getBooleanExtra("Permission", false)) {
                        listener!!.granted()
                    } else {
                        listener!!.denied()
                    }
                } catch (e: Exception) {

                }

            }
        }
    }
}