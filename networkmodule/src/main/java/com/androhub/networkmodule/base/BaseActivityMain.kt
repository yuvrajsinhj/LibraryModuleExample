package com.androhub.networkmodule.base

//import com.rudderstack.android.sdk.core.RudderProperty

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.androhub.networkmodule.R
import com.androhub.networkmodule.*
import com.androhub.networkmodule.display.NetworkStatusDisplayer
import com.androhub.networkmodule.utils.PermissionUtils
import com.androhub.networkmodule.utils.Utility
import com.androhub.networkmodule.utils.Utils
import com.novoda.merlin.Bindable
import com.novoda.merlin.Connectable
import com.novoda.merlin.Disconnectable
import com.novoda.merlin.Merlin
import com.novoda.merlin.MerlinsBeard
import com.novoda.merlin.NetworkStatus
import me.yokeyword.fragmentation.SupportActivity
import java.text.SimpleDateFormat
import java.util.*


public abstract class BaseActivityMain : SupportActivity(), BaseView,
    Bindable {


    var isBinded: Boolean = false
    var isBuild: Boolean = false
    var activity: AppCompatActivity? = null
    var networkStatusDisplayer: NetworkStatusDisplayer? = null
    var merlinsBeard: MerlinsBeard? = null

    lateinit var prefManager: PrefManager
    lateinit var appManager: AppManager
    var TAG = "Base"

    //For scale
    @SuppressLint("ServiceCast")
    open fun adjustFontScale(configuration: Configuration, isForceFullySet: Boolean = false) {
        if (isForceFullySet || configuration.fontScale != 1f) {

            configuration.fontScale = 1f
            val metrics: DisplayMetrics = resources.displayMetrics
            val wm: WindowManager =
                getSystemService(Context.WINDOW_SERVICE) as WindowManager
            wm.getDefaultDisplay().getMetrics(metrics)
            metrics.scaledDensity = configuration.fontScale * metrics.density
            baseContext.resources.updateConfiguration(configuration, metrics)
        }
    }

    fun getFormatTimeWithTZ(mills: String): String? {
        val date11 = Date(Objects.requireNonNull(mills).toLong())
        val timeZoneDate = SimpleDateFormat("HH:mm", Locale.getDefault())
        return timeZoneDate.format(date11)
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //background blurring mechanism // commented as per client requirement
        /* getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
             WindowManager.LayoutParams.FLAG_SECURE)*/
        Log.e("sssssssss", "fffffffffff")
        setUp()

    }

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

    abstract fun initUi()

    abstract fun getContext(): Context?

    abstract fun getLayoutId(): View?

    override fun onSaveInstanceState(outState: Bundle) {
        outState!!.putInt("dummy_value", 0)
        super.onSaveInstanceState(outState)
    }


    fun registerConnectable(connectable: Connectable?) {
        merlin?.registerConnectable(connectable)
    }

    protected fun registerDisconnectable(disconnectable: Disconnectable?) {
        merlin?.registerDisconnectable(disconnectable)
    }

    protected fun registerBindable(bindable: Bindable?) {
        merlin?.registerBindable(bindable)
    }

    override fun onStop() {
        super.onStop()
        merlin?.unbind()

    }

    private fun setUp() {
        //assign
        adjustFontScale(getResources().getConfiguration())
        try {
            activity = this
        }catch (e:Exception){
            Log.e("branchArrayList", "--1"+e.message)

        }

        TAG = (activity as Activity).localClassName

        appManager = AppManager.getInstance(activity)
        prefManager = appManager.getPrefManager()

        // Obtain the FirebaseAnalytics instance.

        //network check
        if (!isBuild) {
            isBuild = true
            merlin = Merlin.Builder()
                .withConnectableCallbacks()
                .withDisconnectableCallbacks()
                .withBindableCallbacks()
                .build(activity)
        }


        merlinsBeard = MerlinsBeard.Builder()
            .build(this)
        networkStatusDisplayer = NetworkStatusDisplayer(resources, merlinsBeard)
        if (!isBinded) {
            isBinded = true
            merlin!!.bind()
        }
        //language preferences
        if (Utils.isArLang()) {

            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        } else {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }
    }


    override fun onResume() {
        super.onResume()

        //prevent to run , check for Emulator device and rooted device
        Utility.checkForSecurity(this)
        //network check
//        registerConnectable(this)
//        registerDisconnectable(this)
        registerBindable(this)

    }

//    override fun attachBaseContext(base: Context?) {
//        Log.e("addd","ddddd")
////        super.attachBaseContext(MyApplication.localeManager.setLocale(base))
//    }

    override fun onBind(networkStatus: NetworkStatus) {
        if (!networkStatus.isAvailable) {
        }
    }


    override fun onPause() {
        super.onPause()
        if (networkStatusDisplayer != null)
            networkStatusDisplayer!!.reset()

    }

    override fun onDestroy() {
        super.onDestroy()
        activity = null
        networkStatusDisplayer = null


    }


    fun isOnline(context: Context): Boolean {
        try {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val n = cm.activeNetwork
                if (n != null) {
                    val nc = cm.getNetworkCapabilities(n)
                    //It will check for both wifi and cellular network
                    return nc!!.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || nc.hasTransport(
                        NetworkCapabilities.TRANSPORT_WIFI
                    ) || nc.hasTransport(
                        NetworkCapabilities.TRANSPORT_VPN
                    )
                }
                return false
            } else {
                val netInfo = cm.activeNetworkInfo
                return netInfo != null && netInfo.isConnectedOrConnecting
            }
        } catch (e: Exception) {
            return true
        }
    }


    fun isConnected(show: Boolean): Boolean {
        if (show) {
            if (isOnline(applicationContext!!)) {
            } else {
                if (networkStatusDisplayer != null)
                    networkStatusDisplayer!!.displayNegativeMessage(
                        com.androhub.networkmodule.R.string.no_internet_available,
                        getLayoutId()
                    )
            }
        }
        return isOnline(applicationContext!!)
    }

    /**
     * Common dialog to handle response
     */
    fun showDialogAsStatus(status: Int?, message: String? = "") {

//        if (status == Constant.STATUS.USER_NOT_FOUND && activity != null && !activity!!.isFinishing) {
//            inItRoom(activity!!)
//
//        } else if (status == Constant.STATUS.TOKEN_EXPIRE) {
//            showErrorMsg(resources.getString(R.string.token_expired))
//            checkToken(object : InterfaceRefreshToken {
//                override fun refreshToken() {
//                }
//            }, false)
////            startActivity(Intent(getContext(), SplashActivity::class.java))
////            finishAffinity()
//        } else {
//            var msg =
//                if (!TextUtils.isEmpty(message)) message /*else if (isLogin) resources.getString(R.string.token_expired) */ else resources.getString(
//                    R.string.internal_server
//                )
//            showErrorMsg(msg!!)
//        }
    }




    interface InterfaceRefreshToken {
        fun refreshToken()
    }

    /**
     * Show loader
     */
    override fun showLoading() {
        Utility.showLoading(this)
    }

    /***
     * Hide loader
     */
    override fun hideLoading() {
        Utility.closeLoading()
    }

    /**
     * Toast for error message from resource id
     */
    override fun showErrorMsg(msg: Int) {
        Utility.showErrorToast(this, getString(msg))
    }

    /**
     * Toast for error message from string message
     */
    override fun showErrorMsg(msg: String) {
        Utility.showErrorToast(this, msg)
    }

    /**
     * Default error message
     */
    fun showErrorMsg() {

        Utility.showErrorToast(this, this.resources.getString(R.string.something_went_working))
    }

    /**
     * Toast for success message from resource id
     */
    override fun showSuccessMsg(msg: Int) {
        Utility.showSuccessToast(this, getString(msg))
    }

    /**
     * Toast for success message from string message
     */
    override fun showSuccessMsg(msg: String) {
        Utility.showSuccessToast(this, msg)
    }

    /**
     * Run time permission
     */
    fun askPermissionIfRequire(
        permission: String,
        rationalMsg: String,
        permissionListener: PermissionUtils.PermissionListener,
    ) {
        PermissionUtils.askPermission(this, permission, rationalMsg, permissionListener)
    }

    /**
     * User to viewa enable disable
     */
    public fun setEditTextEnableDisable(setEnable: Boolean, editText: EditText) {
        editText.isEnabled = setEnable
        editText.isClickable = setEnable
        editText.isLongClickable = setEnable

    }


    override fun onBackPressedSupport() {
        Utility.hideSoftInput(this)

        super.onBackPressedSupport()

    }

    companion object {
        val TAG = BaseActivityMain::class.java.simpleName
        var merlin: Merlin? = null

    }

    interface OnImagePickerDelete {
        fun OnClickImagePickerDelete()
    }

    /**
     * Change StatusBar Color
     */
    fun setStatusBarColor(ColorRes: Int = R.color.view_color_one) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            var colorStatusBar = ColorRes
            getWindow().setStatusBarColor(
                ContextCompat.getColor(
                    getApplicationContext(),
                    colorStatusBar
                )
            );
        }
    }

    fun getFullName(fName: String, lName: String): String {
        return if (!TextUtils.isEmpty(fName) && !TextUtils.isEmpty(lName)) "${fName + " " + lName}"
        else if (!TextUtils.isEmpty(fName)) fName
        else if (!TextUtils.isEmpty(lName)) lName
        else ""
    }


    @SuppressLint("HardwareIds")
    fun getDeviceID(context: Context?): String {
        if (null == context)
            return "123456852" + System.currentTimeMillis();
        return android.provider.Settings.Secure.getString(
            context!!.contentResolver,
            android.provider.Settings.Secure.ANDROID_ID
        );
    }

    fun isLocationEnable(_mActivity: Activity): Boolean {
        val lm = _mActivity!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
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


    fun getUserLatitude(activity: Activity?): String {//"","locationLat":"22.2864036","locationLng":"73.2303622"
        //"22.2860338","locationLng":"73.2300619
        //return "31.9805"

        var oldLat = appManager.getPrefManager().getString(PrefConst.USER_CURRENT_LAT)
        var tempLat = appManager.getPrefManager().getString(PrefConst.USER_CURRENT_LAT_TEMP)
        if (activity != null && TextUtils.isEmpty(oldLat)
            && (isLocationEnable(activity) && !TextUtils.isEmpty(tempLat))
        ) {


            prefManager.setString(PrefConst.USER_CURRENT_LAT, tempLat)

            return tempLat
        }

        return oldLat

    }

    fun getUserLongitude(activity: Activity?): String {
        //  return "35.8377"

        var oldLng = appManager.getPrefManager().getString(PrefConst.USER_CURRENT_LNG)
        var tempLng = appManager.getPrefManager().getString(PrefConst.USER_CURRENT_LNG_TEMP)
        if (activity != null && TextUtils.isEmpty(oldLng)
            && (isLocationEnable(activity) && !TextUtils.isEmpty(tempLng))
        ) {

            prefManager.setString(PrefConst.USER_CURRENT_LNG, tempLng)

            return tempLng
        }

        return oldLng
    }





}


