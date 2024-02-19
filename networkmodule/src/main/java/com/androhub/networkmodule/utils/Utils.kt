package com.androhub.networkmodule.utils

import android.app.Activity
import android.content.Context
import android.text.TextUtils
import android.view.View

import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import com.androhub.networkmodule.AppManager
import com.androhub.networkmodule.MyApplication
import com.androhub.networkmodule.PrefManager
import com.androhub.networkmodule.R

import com.google.android.material.snackbar.Snackbar
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.math.roundToInt

object Utils {
    lateinit var appManager1: AppManager
    lateinit var prefManager1: PrefManager
    val c = Calendar.getInstance()

    fun Utils(appManager: AppManager, prefManager: PrefManager) {
        appManager1 = appManager
        prefManager1 = prefManager
    }



    fun getCurrentDate(timeFormat: String = Constant.DATE_FORMAT): String {
        //val sdf = SimpleDateFormat(timeFormat)
        val sdf =
            SimpleDateFormat(timeFormat, Locale.ENGLISH)
        val currentDate = sdf.format(Date())


        return currentDate
    }

    @JvmStatic
    fun showMemberToDisplay(context: Context, members: String): String? {
        return if (isArLang()) {
            Utility.convertToArabic(members)
        } else members
    }

    @JvmStatic
    fun showTimeToDisplay(context: Context, time: Double?): String? {
        //  var dd= amountToDisplay(time)
        //val arr: Array<String> =  dd.toString().split(".").toTypedArray()

        //  var a=arr[0]+":"+arr[1]
        //for noe display time in double not in 00:00
        var intTime = time?.roundToInt() ?: 0
        var suffif =
            if (intTime <= 1) context.resources.getString(R.string.min_suffix) else context.resources.getString(
                R.string.mins_suffix
            )


        return if (isArLang()) {
            var arabicTime = Utility.convertToArabic(intTime.toString())
            //""  + suffif+arabicTime
            "" + arabicTime + suffif
        } else "" + intTime + suffif
        //return "" + intTime + suffif
    }

    @JvmStatic
    fun isArLang(): Boolean {
//        var sDefSystemLanguage = MyApplication.sDefSystemLanguage
//        print("sDefSystemLanguage=" + sDefSystemLanguage)
        var lang = MyApplication.getAppManager().prefManager.language
        print("sDefSystemLanguage pref=" + lang)
        return (!TextUtils.isEmpty(lang) && lang.equals(Constant.LANGUAGE.AREBIC, true))
        //return "" + intTime + suffif
    }

    @JvmStatic
    fun showTwoDigitDisplay(value: Double): String? {
        val nf: NumberFormat = NumberFormat.getNumberInstance(Locale.US)
        val precision = nf as DecimalFormat
        precision.applyPattern("0.00")
        // val precision = DecimalFormat("0.00")
        //var a=precision.format(value)

        return precision.format(value)
    }

    @JvmStatic
    fun getDateEserviceTwo(milliSeconds: Long): String? {
        var format=Constant.DISPLAY_TIME_FORMAT_ESERVICE_TWO
        return parseDate(
            getDate(milliSeconds, Constant.SERVER_DATE_FORMAT),
            Constant.SERVER_DATE_FORMAT,
            format
        )
    }

    @JvmStatic
    fun getDate(milliSeconds: Long): String? {
        var format =
            if (isArLang()) Constant.DISPLAY_DATE_FORMAT_AR else Constant.DISPLAY_DATE_FORMAT
        return parseDate(
            getDate(milliSeconds, Constant.SERVER_DATE_FORMAT),
            Constant.SERVER_DATE_FORMAT,
            format
        )
    }





    fun getDate(milliSeconds: Long, dateFormat: String?): String? {
        // Create a DateFormatter object for displaying date in specified format.
        val formatter = SimpleDateFormat(dateFormat)

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = milliSeconds
        return formatter.format(calendar.time)
    }

    @JvmStatic
    fun parseDate(selecteddate: String?, currentFormat: String, requiredFormat: String): String? {
        if (TextUtils.isEmpty(selecteddate)) return ""

        // *** note that it's "yyyy-MM-dd hh:mm:ss" not "yyyy-mm-dd hh:mm:ss"
        val dt = SimpleDateFormat(currentFormat)
        var day = ""
        try {
            val date = dt.parse(selecteddate)//getLocalDate(selecteddate!!))
            val dt1 = SimpleDateFormat(requiredFormat)
            day = dt1.format(date)
        } catch (e: ParseException) {
            print(e)
        }
        return day
    }


    fun getEventStartTime(startTime: String): Long {

        val dateStr = startTime
        val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        df.timeZone = TimeZone.getTimeZone("UTC")

        val startTime = df.parse(dateStr)
        df.timeZone = TimeZone.getDefault()

        val endDate2 = df.format(startTime)
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")

        val startDate = Date()
        val startDateCurrent = inputFormat.format(startDate)

        val dateObj: Date = inputFormat.parse(endDate2)
        val dateObj2: Date = inputFormat.parse(startDateCurrent)


        val difference: Long = dateObj.getTime() - dateObj2.getTime()
        val days = (difference / (1000 * 60 * 60 * 24))

        return (difference - 1000 * 60 * 60 * 24 * days) / (1000 * 60)
    }

    fun getEventEndTime(endTime: String): Long {

        val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        df.timeZone = TimeZone.getTimeZone("UTC")

        df.timeZone = TimeZone.getDefault()

        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")

        val startDate = Date()
        val startDateCurrent = inputFormat.format(startDate)

        val dateObj2: Date = inputFormat.parse(startDateCurrent)
        val dateEndTime = endTime
        df.timeZone = TimeZone.getTimeZone("UTC")
        val endTime = df.parse(dateEndTime)
        df.timeZone = TimeZone.getDefault()
        val endTimeDate2 = df.format(endTime)
        val endDateObj: Date = inputFormat.parse(endTimeDate2)

        val difference2: Long = endDateObj.getTime() - dateObj2.getTime()
        val days2 = (difference2 / (1000 * 60 * 60 * 24))
        return (difference2 - 1000 * 60 * 60 * 24 * days2) / (1000 * 60)
    }

    fun getLocalTime(date: String?, timeZone: String?): String? {
        print("Before=" + date)
        if (TextUtils.isEmpty(timeZone)) return "00:00"


        var ourDate: String? = date
        try {
            val formatter =
                SimpleDateFormat(Constant.SERVER_TIME_FORMAT)
            formatter.timeZone =
                TimeZone.getTimeZone(timeZone)// changing time from Saudi Arabia to local
            val value = formatter.parse(ourDate)
            val dateFormatter =
                SimpleDateFormat(Constant.DISPLAY_TIME_FORMAT) //this format changeable
//            dateFormatter.timeZone = TimeZone.getDefault()
            dateFormatter.timeZone = TimeZone.getTimeZone(timeZone)
            ourDate = dateFormatter.format(value ?: "00:00")

            //Log.d("ourDate", ourDate);
        } catch (e: java.lang.Exception) {
            ourDate = "00:00"
        }
        print("After=" + ourDate)
        return ourDate
    }


     fun toast(context: Context?, message: String?) {
        if (context != null && !TextUtils.isEmpty(message))
           Utility.showSuccessToast(context!!, message!!)
    }

    fun snackBar(view: View?, context: Context?, message: String?) {
        try {
            if (message != null && view != null) {
                val imm =
                    context!!.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)

                val snackbar =
                    Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                        .setBackgroundTint(context?.resources?.getColor(R.color.colorPrimary)!!)
                val snackbarView = snackbar.view
                val snackTextView =
                    snackbarView.findViewById<View>(com.google.android.material.R.id.snackbar_text) as TextView

                snackTextView.maxLines = 4

                snackbar.show()
            }
        } catch (e: Exception) {
        }
    }


    fun hideSoftInput(activity: Activity) {
        var view = activity.currentFocus
        if (view == null) view = View(activity)
        val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
    @JvmStatic
    fun print(mesg: String) {
//        if (BuildConfig.DEBUG) {
            println(mesg)
//        }
    }

    @JvmStatic
    fun print(title: String, mesg: String) {
//        if (BuildConfig.DEBUG) {
            print("$title :: $mesg")
//        }
    }


    @JvmStatic
    fun getTicketStatus(context: Context, serverStatus: String?): String {
        if (TextUtils.isEmpty(serverStatus)) return ""
        if (serverStatus.equals(Constant.TICKET_STATUS.STATUS_CANCELLED)) return context.resources.getString(
            R.string.status_cancelled
        )
        else if (serverStatus.equals(Constant.TICKET_STATUS.STATUS_COMPLETED)) return context.resources.getString(
            R.string.status_completed
        )
        else if (serverStatus.equals(Constant.TICKET_STATUS.STATUS_NO_SHOW)) return context.resources.getString(
            R.string.status_no_show
        )
        else if (serverStatus.equals(Constant.TICKET_STATUS.STATUS_NOT_PROCESSED)) return context.resources.getString(
            R.string.status_not_processed
        )
        else if (serverStatus.equals(
                Constant.TICKET_STATUS.STATUS_PROCESSED,
                true
            )
        ) return context.resources.getString(R.string.status_processed)
        else if (serverStatus.equals(
                Constant.TICKET_STATUS.STATUS_REBOOK,
                true
            )
        ) return context.resources.getString(R.string.status_re_book)
        else if (serverStatus.equals(
                Constant.TICKET_STATUS.STATUS_ON_HOLD,
                true
            )
        ) return context.resources.getString(R.string.status_on_hold)
        else return serverStatus!!


    }


    fun makeMeShake(view: View?, duration: Int = 50, offset: Int = 4): View? {
        var duration = duration
        var offset = offset
        if (view != null) {
            val anim: Animation = TranslateAnimation((-offset).toFloat(), offset.toFloat(), 0F, 0F)
            anim.duration = duration.toLong()
            anim.repeatMode = Animation.REVERSE
            anim.repeatCount = 5
            view.startAnimation(anim)
        }
        return view
    }

}
