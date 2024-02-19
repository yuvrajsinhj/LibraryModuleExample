package com.androhub.networkmodule.utils

import android.content.Context
import android.content.res.ColorStateList
import android.os.Build
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.androhub.networkmodule.R
import java.text.SimpleDateFormat
import java.util.*

class UtilsForm {
    companion object {
        fun fromHtml(str: String): String {
            return if (Build.VERSION.SDK_INT >= 24) Html.fromHtml(str, Html.FROM_HTML_MODE_LEGACY)
                .toString() else Html.fromHtml(str).toString()
        }

        fun setMerginToviews(view: View, topMergin: Int, width: Int, height: Int) {
            val layoutParams = LinearLayout.LayoutParams(width, height)
            layoutParams.setMargins(40, topMergin, 40, 40)
            view.layoutParams = layoutParams
        }
        fun setMerginToviewsTemp(view: View, topMergin: Int, width: Int, height: Int) {
            val layoutParams = LinearLayout.LayoutParams(width, height)
            layoutParams.setMargins(0, topMergin, 40, 40)
            view.layoutParams = layoutParams
        }

        fun setMerginToviews(view: View) {
            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            layoutParams.setMargins(40, 20, 40, 0)
            view.layoutParams = layoutParams
        }


        fun getCustomColorStateList(context: Context): ColorStateList {
            return ColorStateList(
                arrayOf(
                    intArrayOf(android.R.attr.state_checked),
                    intArrayOf(-android.R.attr.state_checked)
                ),
                intArrayOf(
                    ContextCompat.getColor(context, R.color.btn_default),//disabled
                    ContextCompat.getColor(context, R.color.font_gray_3) //enabled
                )
            )
        }

        fun getCurrentDate(): Date {
            val calendar = Calendar.getInstance()
            return calendar.time
        }


        fun getDateStringToShow(date: Date, format: String): String {
            try {
                val simpleDateFormat = SimpleDateFormat(format, Locale.ENGLISH);
                return simpleDateFormat.format(date)
            } catch (e: Exception) {
                Log.d("Error", e.toString())
                return ""
            }
        }


    }
}