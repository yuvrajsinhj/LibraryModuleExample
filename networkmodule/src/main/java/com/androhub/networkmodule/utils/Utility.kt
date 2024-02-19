package com.androhub.networkmodule.utils


import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.res.Resources
import android.os.Build
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.androhub.networkmodule.MyApplication
import com.androhub.networkmodule.R
import com.androhub.networkmodule.utils.Utils.isArLang

object Utility {
    fun checkForSecurity(activity: Activity) {

        var isEmulator = false
//        val isEmulator =isEmulator()//false
//        val isRooted =RootChecker.isRooted(activity)
        val isRooted =false//isRooted()//false
        if (isEmulator || isRooted) {
            val builder =
                AlertDialog.Builder(activity)
            builder.setTitle(activity.resources.getString(R.string.security_title))
            builder.setMessage(activity.resources.getString(R.string.msg_emmulator))
            val positiveText = activity.resources.getString(android.R.string.ok)
            builder.setPositiveButton(
                positiveText
            ) { dialog, _ -> // positive button logic
                dialog.dismiss()


            }
            builder.setOnDismissListener {
                if (!activity.isFinishing)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        activity.finishAffinity()
                    }else{
                        activity.finish()
                    }
            }
            val dialog = builder.create()
            dialog.show()
        }
    }


    fun dpToPxMain(dp: Float): Float {
        val density = Resources.getSystem().displayMetrics.density
        return dp * density
    }



    internal var pdlg: MyProgressDialog? = null


    fun showLoading(c: Context) {
        if (pdlg == null) {
            pdlg = MyProgressDialog(c)
            pdlg!!.setCancelable(false)

            try {
                pdlg!!.show()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    fun closeLoading() {
        try {
            if (pdlg != null && pdlg!!.isShowing) {
                pdlg!!.dismiss()
                pdlg = null
            }
        } catch (e: java.lang.IllegalArgumentException) {
        } catch (e: Exception) {
        }
    }


    fun showErrorToast(ct: Context, message: String) {
        if (!TextUtils.isEmpty(message) && !message.equals(ct.resources.getString(R.string.in_error)))
            showToast(ct, message, true)
    }

    fun
            showSuccessToast(ct: Context, message: String) {
        if (!TextUtils.isEmpty(message) && !message.equals(ct.resources.getString(R.string.in_error)))
            showToast(ct, message, false)
    }


    private fun showToast(ct: Context, message: String, isError: Boolean) {
        try {
            val inflater = LayoutInflater.from(ct)
            val layout = inflater.inflate(
                R.layout.layout_toast_message,
                null
            )
            val textV: TextView = layout.findViewById(R.id.lbl_toast) as TextView

            /*if (Utils.isArLang())
            textV.setTextDirection(View.TEXT_DIRECTION_ANY_RTL);*/

            if (isError) {
                textV.setTextColor(ct.resources.getColor(R.color.font_red))
                textV.setBackgroundResource(R.drawable.toast_error_background)
            } else {
                textV.setTextColor(ct.resources.getColor(R.color.font_green))
                textV.setBackgroundResource(R.drawable.toast_success_background)
            }
            if (message.contains("Chain")||message.contains("Socket")||message.contains("Server error") ){
                textV.text = ct.resources.getString(R.string.please_try_again)
            }else{
                textV.text = message
            }

            val toast = Toast(ct)
            toast.duration = Toast.LENGTH_SHORT
            toast.view = layout

            var valueInPixels = 100.toFloat();
            var a = dpToPxMain(valueInPixels).toInt()
            toast.setGravity(Gravity.TOP, 0, 0);
            toast.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }




    fun hideSoftInput(activity: Activity) {
        var view = activity.currentFocus
        if (view == null) view = View(activity)
        val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun getConfirmationDialog(
        context: Context,
        title: String,
        msg: String,
        btnPositiveTitle: String,
        btnNegativeTitle: String,
        onPositiveBtnClick: DialogInterface.OnClickListener,
        onNegativeBtnClick: DialogInterface.OnClickListener,
    ): AlertDialog.Builder {

        return getStyledConfirmationDialog(
            context,
            0,
            title,
            msg,
            btnPositiveTitle,
            btnNegativeTitle,
            onPositiveBtnClick,
            onNegativeBtnClick
        )
    }



    fun getStyledConfirmationDialog(
        context: Context,
        styleId: Int,
        title: String,
        msg: String,
        btnPositiveTitle: String,
        btnNegativeTitle: String,
        onPositiveBtnClick: DialogInterface.OnClickListener?,
        onNegativeBtnClick: DialogInterface.OnClickListener?,
    ): AlertDialog.Builder {

        var lang= MyApplication.getAppManager().getPrefManager().language
        MyApplication.localeManager.setNewLocale( context!!, lang)

        val builder: AlertDialog.Builder
        if (styleId == 0) {
            builder = AlertDialog.Builder(context)
        } else {
            builder = AlertDialog.Builder(context, styleId)
        }
        builder.setCancelable(true)
        builder.setTitle(title)
        if (!msg.isEmpty()) {
            builder.setMessage(msg)
        }
        if (!btnNegativeTitle.equals("", ignoreCase = true)) {
            builder.setNegativeButton(btnNegativeTitle) { dialog, which ->
                if (onNegativeBtnClick != null) {
                    onNegativeBtnClick.onClick(dialog, 0)
                } else {
                    dialog.dismiss()
                }
            }
        }
        if (!btnPositiveTitle.equals("", ignoreCase = true)) {
            builder.setPositiveButton(btnPositiveTitle) { dialog, which ->
                if (onPositiveBtnClick != null) {
                    onPositiveBtnClick.onClick(dialog, 0)
                } else {
                    dialog.dismiss()
                }
            }
        }
        return builder
    }

    fun convertToArabic(value: String): String {
        return if (isArLang()) {
            (value + "").replace("1".toRegex(), "١").replace(
                "2".toRegex(),
                "٢"
            ).replace("3".toRegex(), "٣").replace("4".toRegex(), "٤").replace(
                "5".toRegex(),
                "٥"
            ).replace("6".toRegex(), "٦").replace("7".toRegex(), "٧").replace(
                "8".toRegex(),
                "٨"
            ).replace("9".toRegex(), "٩").replace("0".toRegex(), "٠")
        } else value
    }

}