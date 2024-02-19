//package com.mylib.base
//
//import android.annotation.SuppressLint
//import android.app.Activity
//import android.content.Context
//import android.net.ConnectivityManager
//import android.net.NetworkCapabilities
//import android.os.Bundle
//import android.text.TextUtils
//import android.view.View
//import androidx.appcompat.app.AppCompatActivity
//import androidx.fragment.app.FragmentActivity
//import com.mylib.*
//import com.mylib.utils.Constant
//import com.mylib.utils.PermissionUtils
//import com.mylib.utils.Utility
//import com.mylib.utils.Utils
//import me.yokeyword.fragmentation.SupportFragment
//
//
///**
// * This fragment must be parent fragment of all the fragments in app.
// * this fragment contains common functionality that is common between all the child fragments.
// */
//abstract class BaseFragmentMain : SupportFragment(), BaseView {
//    lateinit var prefManager: PrefManager
//    lateinit var appManager: AppManager
//    var mContext: Context? = null
//    private var rootFragment: SupportFragment? = null
//
//
//    val supportActivity: FragmentActivity
//        get() = _mActivity
//
//
//    val supportFragment: BaseFragmentMain
//        get() = this
//
//    override fun onBackPressedSupport(): Boolean {
//        Utility.hideSoftInput(_mActivity)
//        return super.onBackPressedSupport()
//
//    }
//
//    override fun showErrorMsg(msg: String) {
//        (_mActivity as BaseActivityMain).showErrorMsg(msg)
//    }
//
//    override fun showErrorMsg(msg: Int) {
//        (_mActivity as BaseActivityMain).showErrorMsg(msg)
//    }
//
//    override fun showSuccessMsg(msg: String) {
//        (_mActivity as BaseActivityMain).showSuccessMsg(msg)
//    }
//
//    override fun showSuccessMsg(msg: Int) {
//        (_mActivity as BaseActivityMain).showSuccessMsg(msg)
//    }
//
//    override fun showLoading() {
//        (_mActivity as BaseActivityMain).showLoading()
//    }
//
//
//
//
//    override fun onResume() {
//        super.onResume()
//
//        MyApplication.setApplicationlanguage(context);
//    }
//
//    override fun hideLoading() {
//        (_mActivity as BaseActivityMain).hideLoading()
//    }
//
//    fun showDialogAsStatus(status: Int?, message: String? = "") {
//        (_mActivity as BaseActivityMain).showDialogAsStatus(status, message)
//    }
//
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        mContext = context
//
//        appManager = AppManager.getInstance(context)
//        prefManager = appManager.getPrefManager()
//
//        if (Utils.isArLang()) {
//            requireActivity().getWindow().getDecorView()
//                .setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
//        } else {
//            requireActivity().getWindow().getDecorView()
//                .setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
//        }
//    }
//
//
//    abstract fun getLayoutId(): View?
//
//
//    fun isNetworkConnected(context: Context): Boolean {
//        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//        val activeNetwork = cm.activeNetworkInfo
//        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
//    }
//
//    fun isOnline(context: Context): Boolean {
//        val connectivityManager =
//            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//        if (connectivityManager != null) {
//            val capabilities =
//                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
//            if (capabilities != null) {
//                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
//                    // Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
//                    return true
//                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
//                    //  Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
//                    return true
//                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
//                    //  Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
//                    return true
//                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)) {
//                    //  Log.i("Internet", "NetworkCapabilities.TRANSPORT_VPN")
//                    return true
//                }
//            }
//        } else if (isNetworkConnected(context)) {
//            return true
//        }
//
//        return false
//    }
//
//    fun isConnected(show: Boolean): Boolean {
//        try {
//            if (isOnline(requireContext())) {
//                return true
//            } else {
//                if (show) {
//                    Utils.snackBar(
//                        getLayoutId(),
//                        getContext(),
//                        resources.getString(com.androhub.networkmodule.R.string.no_internet_available)
//                    )
//                }
//                return false
//            }
//        } catch (e: Exception) {
//            return true
//        }
//
//
//    }
//
//    override fun onCreateFragmentAnimator(): me.yokeyword.fragmentation.anim.FragmentAnimator {
//        return me.yokeyword.fragmentation.anim.FragmentAnimator(
//            R.anim.enter_from_right, R.anim.exit_to_right,
//            R.anim.enter_from_left, R.anim.exit_to_left
//        )
//    }
//
//    fun setRootFragment(root: SupportFragment) {
//        rootFragment = root
//    }
//
//    fun popToRootChild() {
//        if (rootFragment != null) {
//            popToChild(rootFragment!!.javaClass, false, Runnable { }, 0)
//
//        }
//    }
//
//
//
//    fun askPermissionIfRequire(
//        permission: String,
//        rationalMsg: String,
//        permissionListener: PermissionUtils.PermissionListener,
//    ) {
//        (_mActivity as BaseActivityMain).askPermissionIfRequire(
//            permission,
//            rationalMsg,
//            permissionListener
//        )
//    }
//
//    override fun onSupportVisible() {
//        super.onSupportVisible()
//    }
//
//    @SuppressLint("HardwareIds")
//    fun getDeviceID(): String {
//        var id = Constant.TEMP_ID
//        try {
//            id = android.provider.Settings.Secure.getString(
//                requireActivity().contentResolver,
//                android.provider.Settings.Secure.ANDROID_ID
//            )
//        } catch (e: Exception) {
//        } finally {
//            return id;
//        }
//    }
//
//    fun getFullName(fName: String, lName: String): String {
//        return if (!TextUtils.isEmpty(fName) && !TextUtils.isEmpty(lName)) "${fName + " " + lName}"
//        else if (!TextUtils.isEmpty(fName)) fName
//        else if (!TextUtils.isEmpty(lName)) lName
//        else ""
//    }
//
//
//
//}
//
