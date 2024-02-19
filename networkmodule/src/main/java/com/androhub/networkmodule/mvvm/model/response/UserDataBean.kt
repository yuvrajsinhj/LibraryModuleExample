package com.androhub.networkmodule.mvvm.model.response

import android.util.Log
import com.androhub.networkmodule.MyApplication
import com.androhub.networkmodule.PrefConst
import com.google.gson.Gson
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class UserDataBean : Serializable {


//    @SerializedName("createdAt")
//    @Expose
//     var createdAt: Int? = null
//
//    @SerializedName("updatedAt")
//    @Expose
//     var updatedAt: Int? = null

    @SerializedName("id")
    @Expose
    var id: String? = null

    @SerializedName("userId")
    @Expose
    var userId: String? = null

    @SerializedName("userName")
    @Expose
    var userName: String? = null

    @SerializedName("email")
    @Expose
    var email: String? = null

    @SerializedName("firstName")
    @Expose
    var firstName: String? = null

    @SerializedName("lastName")
    @Expose
    var lastName: String? = null

    @SerializedName("profilePic")
    @Expose
    var profilePic: String? = null

    @SerializedName("phone")
    @Expose
    var phone: String? = null

    @SerializedName("countryCode")
    @Expose
    var countryCode: String? = null

    @SerializedName("isoCode")
    @Expose
    var isoCode: String? = null

    @SerializedName("phoneCountry")
    @Expose
    var phoneCountry: String? = null

    @SerializedName("website")
    @Expose
    var website: String? = null

    @SerializedName("loginWith")
    @Expose
    var loginWith: String? = null

    @SerializedName("deviceToken")
    @Expose
    var deviceToken: String? = null

    @SerializedName("forgetPasswordToken")
    @Expose
    var forgetPasswordToken: String? = null

    @SerializedName("lang ")
    @Expose
    var lang : String? = null

    @SerializedName("role")
    @Expose
    var role: String=""

    @SerializedName("isFirstTime")
    @Expose
    var isFirstTime: Boolean? = null



    @SerializedName("hashtag")
    @Expose
    var hashtag: ArrayList<String>? = null
    var hashtagString: String = ""

    @SerializedName("followerCount")
    @Expose
    var followerCount: Int? = null

    @SerializedName("isPublicVisible")
    @Expose
    var isPublicVisible: Boolean = false

    @SerializedName("followingCount")
    @Expose
    var followingCount: Int? = null

    @SerializedName("isBothEditable")
    @Expose
    var isBothEditable: Boolean = true

    @SerializedName("viewsCount")
    @Expose
    var viewsCount: Int? = null
    @SerializedName("usherData")
    @Expose
    var usherData: Boolean? = false

    //used for notification intent
    var sessionId: String = ""
    var notificationType: String = ""


    fun saveData(userDataBean: UserDataBean? = null) {
        email?.let { MyApplication.getAppManager().prefManager.setString(PrefConst.PREF_EMAIL, it) }
        firstName?.let {
            MyApplication.getAppManager().prefManager.setString(
                PrefConst.PREF_FIRST_NAME,
                it
            )
        }
        lastName?.let {
            MyApplication.getAppManager().prefManager.setString(
                PrefConst.PREF_LAST_NAME,
                it
            )
        }
        id?.let { MyApplication.getAppManager().prefManager.setString(PrefConst.PREF_USER_ID, it) }
        userName?.let {
            MyApplication.getAppManager().prefManager.setString(
                PrefConst.PREF_USER_NAME,
                it
            )
        }
        phone?.let { MyApplication.getAppManager().prefManager.setString(PrefConst.PREF_PHONE, it) }
        countryCode?.let {
            MyApplication.getAppManager().prefManager.setString(
                PrefConst.PREF_COUNTRY_CODE,
                it
            )
        }
        /*isFirstTime?.let {
            MyApplication.getAppManager().prefManager.setBoolean(
                PrefConst.PREF_IS_FIRST_TIME,
                it
            )
        }*/

        profilePic?.let {
            MyApplication.getAppManager().prefManager.setString(
                PrefConst.PREF_IMAGE_URL,
                it
            )
        }
        lang ?.let {
            MyApplication.getAppManager().prefManager.setString(
                PrefConst.CURRENT_LANGUAGE,
                it
            )
        }
        website?.let {
            MyApplication.getAppManager().prefManager.setString(
                PrefConst.PREF_WEBSITE,
                it
            )
        }
        loginWith?.let {
            MyApplication.getAppManager().prefManager.setString(
                PrefConst.PREF_LOGIN_WITH,
                it
            )
        }

        isoCode?.let {
            MyApplication.getAppManager().prefManager.setString(
                PrefConst.PREF_ISO_CODE,
                it
            )
        }
        phoneCountry?.let {
            MyApplication.getAppManager().prefManager.setString(
                PrefConst.PREF_COUNTRY_CODE_NAME,
                it
            )
        }
        isBothEditable?.let {
            MyApplication.getAppManager().prefManager.setBoolean(
                PrefConst.PREF_ISBOTH_EDITABLE,
                it
            )
        }
        usherData?.let {
            MyApplication.getAppManager().prefManager.setBoolean(
                PrefConst.PREF_IS_USHER,
                it
            )
            Log.e("usherrrr","${it}----")
        }
        Log.e("usherrrr","${userDataBean?.usherData}----")

        if (hashtag != null && hashtag!!.size > 0) {
            val commaSeperatedString = hashtag?.joinToString(separator = " ") { it -> it }
            commaSeperatedString?.let {
                MyApplication.getAppManager().prefManager.setString(
                    PrefConst.PREF_HASH_TAG,
                    it
                )
            }
        } else {
            MyApplication.getAppManager().prefManager.setString(
                PrefConst.PREF_HASH_TAG,
                ""
            )
        }




        if (userDataBean != null) {
            MyApplication.getAppManager().prefManager.setString(
                PrefConst.PREF_USER_BEAN,
                Gson().toJson(userDataBean)
            )
        }
    }


}

