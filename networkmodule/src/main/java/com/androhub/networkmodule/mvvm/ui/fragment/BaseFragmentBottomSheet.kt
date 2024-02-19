package com.androhub.networkmodule.mvvm.ui.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.core.content.ContextCompat
import com.androhub.networkmodule.AppManager
import com.androhub.networkmodule.PrefConst
import com.androhub.networkmodule.PrefManager
import com.androhub.networkmodule.R
import com.androhub.networkmodule.base.BaseActivityMain

import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.novoda.merlin.Bindable
import com.novoda.merlin.NetworkStatus


abstract class BaseFragmentBottomSheet : BottomSheetDialogFragment(), Bindable {
    var mContext: Context? = null

    //    lateinit var pDialogLoading: KAlertDialog
    // private var networkStatusDisplayer: NetworkStatusDisplayer? = null
//    private var merlinsBeard: MerlinsBeard? = null
    lateinit var prefManager: PrefManager
    lateinit var appManager: AppManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mContext = context

        appManager = AppManager.getInstance(mContext)
        prefManager = appManager.prefManager

    }

    abstract fun getLayoutId(): View
    protected abstract fun initUi()
    /*  protected fun registerConnectable(connectable: Connectable?) {
          merlin!!.registerConnectable(connectable)
      }

      protected fun registerDisconnectable(disconnectable: Disconnectable?) {
          merlin!!.registerDisconnectable(disconnectable)
      }

      protected fun registerBindable(bindable: Bindable?) {
          merlin!!.registerBindable(bindable)
      }*/

    override fun onBind(networkStatus: NetworkStatus) {
        if (!networkStatus.isAvailable) {
        }
    }

    fun showDialogAsStatus(status: Int?, message: String? = "") {
        (activity as BaseActivityMain).showDialogAsStatus(status, message)
    }


    //    protected fun showProgressBar(show: Boolean) {
//        if (show) {
//            try {
//                if (pDialogLoading!!.isShowing) pDialogLoading!!.dismiss()
//                pDialogLoading!!.show()
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        } else {
//            try {
//                if (pDialogLoading!!.isShowing) pDialogLoading!!.dismiss()
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
//    }
    fun isOnline(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val n = cm.activeNetwork
            val nc = cm.getNetworkCapabilities(n)
            if (n != null && nc != null) {
                //It will check for both wifi and cellular network
                return nc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || nc.hasTransport(
                    NetworkCapabilities.TRANSPORT_WIFI
                ) || nc.hasTransport(NetworkCapabilities.TRANSPORT_VPN)
            }
            return false
        } else {
            val netInfo = cm.activeNetworkInfo
            return netInfo != null && netInfo.isConnectedOrConnecting
        }
    }

    abstract override fun getContext(): Context?
    fun isConnected(context: Context?, show: Boolean): Boolean {
        if (context == null) return true
        return isOnline(requireContext())


    }

    @SuppressLint("HardwareIds")
    fun getDeviceID(): String {
        return android.provider.Settings.Secure.getString(
            requireActivity().contentResolver,
            android.provider.Settings.Secure.ANDROID_ID
        )
    }

    /**
     * pass lat long no metter location enable or not
     */
    fun getStoredUserLatitude(activity: Activity): String {
        var oldLat = appManager.prefManager.getString(PrefConst.USER_CURRENT_LAT)
        var tempLat = appManager.prefManager.getString(PrefConst.USER_CURRENT_LAT_TEMP)
        return if (!TextUtils.isEmpty(oldLat)) oldLat else if (!TextUtils.isEmpty(tempLat)) tempLat else "0.0"


    }

    fun getStoredUserLongitude(activity: Activity): String {
        var oldLng = appManager.prefManager.getString(PrefConst.USER_CURRENT_LNG)
        var tempLng = appManager.prefManager.getString(PrefConst.USER_CURRENT_LNG_TEMP)
        return if (!TextUtils.isEmpty(oldLng)) oldLng else if (!TextUtils.isEmpty(tempLng)) tempLng else "0.0"

    }

    fun getUserLatitude(activity: Activity): String {//"","locationLat":"22.2864036","locationLng":"73.2303622"
        //"22.2860338","locationLng":"73.2300619
        //return "31.9805"

        var oldLat = appManager.prefManager.getString(PrefConst.USER_CURRENT_LAT)
        var tempLat = appManager.prefManager.getString(PrefConst.USER_CURRENT_LAT_TEMP)
        if (TextUtils.isEmpty(oldLat)
            && (isLocationEnable(activity) && !TextUtils.isEmpty(tempLat))
        ) {


            prefManager.setString(PrefConst.USER_CURRENT_LAT, tempLat)

            return tempLat
        }

        return oldLat

    }

    fun getUserLongitude(activity: Activity): String {
        //  return "35.8377"

        var oldLng = appManager.prefManager.getString(PrefConst.USER_CURRENT_LNG)
        var tempLng = appManager.prefManager.getString(PrefConst.USER_CURRENT_LNG_TEMP)
        if (TextUtils.isEmpty(oldLng)
            && (isLocationEnable(activity) && !TextUtils.isEmpty(tempLng))
        ) {

            prefManager.setString(PrefConst.USER_CURRENT_LNG, tempLng)

            return tempLng
        }

        return oldLng
    }

    fun isLocationEnable(_mActivity: Activity): Boolean {
        val lm = _mActivity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var gps_enabled = false
        var network_enabled = false
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
        } catch (ex: Exception) {
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        } catch (ex: java.lang.Exception) {
        }

        if (!gps_enabled && !network_enabled) {
            return false
        }
        return true
    }

    fun getFullName(fName: String, lName: String): String {
        return if (!TextUtils.isEmpty(fName) && !TextUtils.isEmpty(lName)) "${fName + " " + lName}"
        else if (!TextUtils.isEmpty(fName)) fName
        else if (!TextUtils.isEmpty(lName)) lName
        else ""
    }
//    protected fun showErrorDialog() {
//        val kAlertDialog = KAlertDialog(mContext, KAlertDialog.ERROR_TYPE)
//        kAlertDialog.titleText = mContext!!.resources.getString(R.string.Oops)
//        kAlertDialog.setContentText(
//            mContext!!.resources.getString(R.string.something_went_working)
//        ).show()
//    }

//    protected fun showErrorDialog(message: String?, showTitle: Boolean) {
//        val kAlertDialog = KAlertDialog(mContext, KAlertDialog.ERROR_TYPE)
//        if (showTitle) kAlertDialog.titleText = mContext!!.resources.getString(R.string.Oops)
//        kAlertDialog.setContentText(message).show()
//    }

//    protected fun showErrorDialog(message: String?, title: String?) {
//        KAlertDialog(mContext, KAlertDialog.ERROR_TYPE)
//            .setTitleText(title)
//            .setContentText(message).show()
//    }
//
//    protected fun showSuccessDialog(message: String?) {
//        KAlertDialog(mContext, KAlertDialog.SUCCESS_TYPE)
//            .setContentText(message).show()
//    }

//    protected fun showMessageDialog(
//        message: String?,
//        showTitle: Boolean
//    ) {
////        val kAlertDialog = KAlertDialog(mContext)
//        if (showTitle) kAlertDialog.titleText = mContext!!.resources.getString(R.string.Oops)
//        kAlertDialog.setCancelable(true)
//        kAlertDialog.setCanceledOnTouchOutside(true)
//        kAlertDialog.setContentText(message).show()
//    }

//    protected fun showDialogAsStatus(status: Int?, message: String? = "") {
//        if (status == Constant.STATUS.TOKEN_EXPIRE) {
////            checkToken(object : InterfaceRefreshToken {
////                override fun refreshToken() {
////                }
////            }, false)
//            startActivity(Intent(getContext(), SplashActivity::class.java))
//            (mContext as Activity).finishAffinity()
//        } else if (message != null && !message.isEmpty()) {
//            showErrorDialog(message, false)
//        }
//    }

    //    fun checkToken(interfaceRefreshToken: InterfaceRefreshToken, show: Boolean = true) {
//        if (show)
//            showProgressBar(true)
//        val mUser = FirebaseAuth.getInstance().currentUser
//        var idToken = ""
//        mUser?.getIdToken(true)?.addOnCompleteListener { task ->
//            showProgressBar(false)
//            if (task.isSuccessful) {
//                idToken = task.result!!.token.toString()
//                prefManager.setString(PrefConst.PREF_USER_TOKEN_SECURITY, idToken)
//                // Send token to your backend via HTTPS
//                // ...
//                interfaceRefreshToken.refreshToken()
//                Utils.print("New user token=====$idToken")
//            }else{
//                showProgressBar(false)
//            }
//        }
//    }
    interface InterfaceRefreshToken {
        fun refreshToken()
    }

//    fun getLoginUserData(): UserDataBean {
//        var yourBean = UserDataBean()
//        try {
//            val userDataBean: Type = object : TypeToken<UserDataBean?>() {}.type
//            val gson = Gson()
//            yourBean = gson.fromJson(
//                MyApplication.getAppManager().prefManager.getString(PrefConst.PREF_USER_BEAN),
//                userDataBean
//            )
//        } catch (e: java.lang.Exception) {
//            e.printStackTrace()
//        }
//        return yourBean
//    }

    open fun showLoading() {
        if (activity != null)
            (activity as BaseActivityMain).showLoading()
    }

    fun showErrorMsg(msg: String) {
        if (activity != null)
            (activity as BaseActivityMain).showErrorMsg(msg)
    }

    fun showErrorMsg(msg: Int) {
        if (activity != null)
            (activity as BaseActivityMain).showErrorMsg(msg)
    }


    fun showSuccessMsg(msg: String) {
        if (activity != null)
            (activity as BaseActivityMain).showSuccessMsg(msg)
    }

    fun showSuccessMsg(msg: Int) {
        if (activity != null)
            (activity as BaseActivityMain).showSuccessMsg(msg)
    }

    open fun hideLoading() {
        if (activity != null)
            (activity as BaseActivityMain).hideLoading()
    }

    fun checkIsLogin(showError: Boolean = false): Boolean {
//        if (activity != null)
//            return (activity as BaseActivityMain).checkIsLogin(requireActivity(), showError)
//        return false
        return true
    }

    /**
     * Update button background
     */
    fun setButtonBg(context: Context, view: View, setEnable: Boolean) {

        var drawable: Int =
            if (setEnable) R.drawable.btn_round_green_round else R.drawable.btn_shape_unselected

        view.background = ContextCompat.getDrawable(context, drawable)

    }
}