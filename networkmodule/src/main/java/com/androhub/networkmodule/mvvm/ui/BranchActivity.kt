package com.androhub.networkmodule.mvvm.ui

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.androhub.networkmodule.AppManager
import com.androhub.networkmodule.MyApplication
import com.androhub.networkmodule.PrefConst
import com.androhub.networkmodule.PrefManager
import com.androhub.networkmodule.R
import com.androhub.networkmodule.adapter.NearByBranchListAdapter
import com.androhub.networkmodule.adapter.OtherBranchesPaginationAdapter
import com.androhub.networkmodule.databinding.FragmentBranchBinding
import com.androhub.networkmodule.local.LocaleManager
import com.androhub.networkmodule.local.Utility
import com.androhub.networkmodule.mvvm.model.request.AnonomusRequestBean
import com.androhub.networkmodule.mvvm.model.request.TokenRequestBean
import com.androhub.networkmodule.mvvm.model.response.AddTicketResponseBean
import com.androhub.networkmodule.mvvm.model.response.DistanceData
import com.androhub.networkmodule.mvvm.model.response.TicketBean
import com.androhub.networkmodule.mvvm.model.response.getbranchbymerchant.Branch
import com.androhub.networkmodule.mvvm.ui.fragment.CustomerConfirmationTicketActivity
import com.androhub.networkmodule.mvvm.viewmodel.GetBranchByMerchantModel
import com.androhub.networkmodule.utils.Constant
import com.androhub.networkmodule.utils.ImageDisplayUitls
import com.androhub.networkmodule.utils.Utils
import com.androhub.networkmodule.utils.gpsLocation.GetLocationUpdate
import com.androhub.networkmodule.utils.gpsLocation.MyLocationListener
import com.androhub.networkmodule.utils.imagePickerUtils.ImageViewZoomActivity
import com.androhub.networkmodule.utils.imageSlider.IndicatorAnimations
import com.androhub.networkmodule.utils.imageSlider.SliderAnimations
import com.androhub.networkmodule.utils.imageSlider.SliderView
import com.androhub.networkmodule.utils.imageSlider.setup.SliderAdapter
import com.androhub.networkmodule.utils.loadmore.OnLoadMoreListener
import com.androhub.networkmodule.utils.loadmore.RecyclerViewLoadMoreScroll
import com.google.android.material.appbar.AppBarLayout
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.anko.async
import org.jetbrains.anko.dimen
import java.util.Locale


class BranchActivity : AppCompatActivity(), PermissionListener, MyLocationListener,
    View.OnClickListener, NearByBranchListAdapter.BranchNameInter {

    //
    lateinit var binding: FragmentBranchBinding
    lateinit var layoutManager: LinearLayoutManager
    private var branchArrayList = ArrayList<Branch>()
    private var branchArrayListOther = ArrayList<Branch?>()
    var preventCustomerBookingOption: String = ""
    var branchAdapter: NearByBranchListAdapter? = null
    private lateinit var viewGroup: ViewGroup
    var mLastLocation: Location? = null

    var permissionListener: PermissionListener? = null

    //gps
    var getLocationUpdate: GetLocationUpdate? = null
//    private var merchantID = ""

    private var iconLogoUrl = ""

    //    var otherBranchAdapter2: OtherBranchAdapter? = null
    var otherBranchAdapter: OtherBranchesPaginationAdapter? = null
    lateinit var viewModel: GetBranchByMerchantModel
    var adapterSlider: SliderAdapter? = null
    var page: Int = 0
    private var ticketList = ArrayList<TicketBean>()

    //var cursor: String? = Constant.API_KEYS.INITIAL
    private var scrollListener: RecyclerViewLoadMoreScroll? = null

    //for this app
    lateinit var prefManager: PrefManager
    lateinit var appManager: AppManager

    var UserMerchantID = ""
    var UserSecretKey = ""
    var UserIso = ""
    var UserPhone = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.fragment_branch)
        binding.lifecycleOwner = this
        viewModel = ViewModelProvider(this).get(GetBranchByMerchantModel::class.java)
        viewGroup =
            (findViewById<View>(android.R.id.content) as ViewGroup).getChildAt(0) as ViewGroup
        appManager = AppManager.getInstance(this)
        MyApplication.localeManager = LocaleManager(this)
        prefManager = appManager.getPrefManager()
        permissionListener = this
        binding.sliderView.visibility = View.GONE
        updateLanguage()
        checkPermission()
        getIntentData()
        setStatusBarColor(R.color.home_bg_color_gray)
        startGPSLocation()
        initStart()
        initSlider()
        try {
            ticketList = MyApplication.getAppManager().prefManager.savedTicketList
        } catch (e: Exception) {
            prefManager.saveTicketList(null)
//                prefManager.saveHistoryTicketList(null)
        }
        setSliderAdapter()
        refreshTicketListRepeat()
    }

    private fun initSlider() {


        adapterSlider = SliderAdapter(this, ticketList, this)

        binding.sliderView.setSliderAdapter(adapterSlider!!)

        binding.sliderView.setIndicatorAnimation(IndicatorAnimations.THIN_WORM) //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        binding.sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
        binding.sliderView.autoCycleDirection = SliderView.AUTO_CYCLE_DIRECTION_RIGHT
        binding.sliderView.scrollTimeInSec = 5
        binding.sliderView.isAutoCycle = true
        binding.sliderView.startAutoCycle()
        binding.sliderView.setIndicatorVisibility(true)
        binding.sliderView.enableDisableScroll(false)


    }

    private fun setSliderAdapter() {
        manageSliderVisibility(ticketList.size > 0)

        if (ticketList.size == 1) {
            //if single ticket
            binding.sliderView.stopAutoCycle()
            binding.sliderView.enableDisableScroll(true)
        } else {

            binding.sliderView.enableDisableScroll(false)
        }

        if (adapterSlider != null) {
            adapterSlider!!.renewItems(ticketList)
        } else {
            adapterSlider = SliderAdapter(this, ticketList, this)

            binding.sliderView.setSliderAdapter(adapterSlider!!)

        }
    }

    fun manageSliderVisibility(show: Boolean = true) {

        if (prefManager.getBoolean(PrefConst.TICKET_BOOKED_HIDE_IMAGE_SLIDER)) {

            binding.sliderView.visibility = View.VISIBLE
            binding.sliderView.setViewHeight(this.resources.getDimension(dimen(360)))

        } else if (ticketList.size > 0) {

            binding.sliderView.visibility = View.VISIBLE
            binding.sliderView.setViewHeight(this.resources.getDimension(dimen(360)))

        } else {
            binding.sliderView.visibility = View.GONE

        }

    }

    fun startGPSLocation() {
        getLocationUpdate = GetLocationUpdate(this, this)
        if (getLocationUpdate != null)
            getLocationUpdate!!.onStartLocation(Constant.DISTANCE_INTERVAL_SHORT)
    }

    private fun getIntentData() {
        if (intent.hasExtra(Constant.INTENT_EXTRA.USER_MERCHANT_ID)) {
//            val myLocale = Locale("en")
//            val res = resources
//            val dm = res.displayMetrics
//            val conf: Configuration = res.configuration
//            conf.locale = myLocale
//            res.updateConfiguration(conf, dm)

            UserMerchantID = intent.getStringExtra(Constant.INTENT_EXTRA.USER_MERCHANT_ID)!!
            UserSecretKey = intent.getStringExtra(Constant.INTENT_EXTRA.USER_SECRET_KEY)!!
            UserPhone = intent.getStringExtra(Constant.INTENT_EXTRA.USER_PHONE_KEY)!!
            UserIso = intent.getStringExtra(Constant.INTENT_EXTRA.USER_ISO_KEY)!!

            prefManager.setString(PrefConst.USER_MERCHANT_ID, UserMerchantID)
            prefManager.setString(PrefConst.USER_SECRET_KEY, UserSecretKey)
            prefManager.setString(PrefConst.USER_PHONE_KEY, UserPhone)
            prefManager.setString(PrefConst.USER_ISO_KEY, UserIso)
        } else {
            UserMerchantID = prefManager.getString(PrefConst.USER_MERCHANT_ID)
            UserSecretKey = prefManager.getString(PrefConst.USER_SECRET_KEY)
            UserPhone = prefManager.getString(PrefConst.USER_PHONE_KEY)
            UserIso = prefManager.getString(PrefConst.USER_ISO_KEY)
        }
    }

    private fun initStart() {

        inItListeners()
        branchArrayList = ArrayList()
        setNearByBranchAdapter(branchArrayList)
        callTokenApi(2)

    }

    private fun inItListeners() {
//        binding.toolbarImage.setOnClickListener(this)
//        binding.imgSearch.setOnClickListener(this)
//        binding.imgSearch.visibility = View.GONE
        binding.imgBack.setOnClickListener(this)
        setObserver()
        loadMoreStuff()
    }


    private fun callTokenApi(tag: Int = 1) {
        if (tag == 1) {
            if (!UserPhone.isNullOrEmpty() && !UserIso.isNullOrEmpty()) {
                showLoading()
                var beanToSend = AnonomusRequestBean()
                beanToSend.isoCode = UserIso
                beanToSend.phone = UserPhone

                viewModel.requestGetAninymousLoginApiCall(this, beanToSend)
            } else {
                Toast.makeText(this, "Number or code empty", Toast.LENGTH_LONG).show()
                finish()
            }

        } else if (tag == 2) {
            if (!UserMerchantID.isNullOrEmpty() && !UserSecretKey.isNullOrEmpty()) {
                showLoading()
                var beanToSend = TokenRequestBean()
                beanToSend.merchantId = UserMerchantID
                beanToSend.secretkey = UserSecretKey

                viewModel.requestGetGenerateTokenApiCall(this, beanToSend)
            } else {
                Toast.makeText(this, "Invalid Merchant", Toast.LENGTH_LONG).show()
                finish()
            }
        } else if (tag == 3) {

            val hashMap = HashMap<String, String>()
            hashMap.put("customerId", prefManager.getString(PrefConst.PREF_USER_ID))
            Utils.print("custAPI 5")
            viewModel.requestGetTicketByCustomerListApiCall(this, hashMap)


        }
    }

    private fun callApi(isNearby: Boolean, isShowLoading: Boolean = true) {
        if (!UserMerchantID.isNullOrEmpty())
            if (isNearby && !UserMerchantID.isNullOrEmpty()) {
                val hashMap = HashMap<String, Any>()
                try {
                    if (getUserLatitude(this)!=""){
                        hashMap.put(
                            "lat",
                            getUserLatitude(this)
                        )
                        hashMap.put(
                            "lng",
                            getUserLongitude(this)
                        )
                    }else{
                        return;
                    }

                }catch (e:Exception){

                }

                hashMap.put("id", UserMerchantID)
                hashMap.put("isNearBy", "$isNearby")
                var cc = "IN"
                hashMap.put("country", cc)
                hashMap.put("distance", "100")

                showLoading()

                if (getUserLatitude(this)!="") {
                    viewModel.requestGetBranchByMerchantListApiCall(this, hashMap)
                }
            } else if (!UserMerchantID.isNullOrEmpty()) {
                val hashMap = HashMap<String, Any>()
                try {

                    if (getUserLatitude(this)!=""){
                        hashMap.put(
                            "lat",
                            getUserLatitude(this)
                        )
                        hashMap.put(
                            "lng",
                            getUserLongitude(this)
                        )
                    }else{

                    }
                }catch (e:Exception){

                }
                hashMap.put("id", UserMerchantID)
                hashMap.put("isNearBy", "$isNearby")
                var cc = "IN"
                hashMap.put("country", cc)
                hashMap.put("distance", "0")

                showLoading()


                viewModel.requestGetOtherBranchByMerchantListApiCall(this, hashMap)

            } else {
                Toast.makeText(this, "Invalid Merchant", Toast.LENGTH_LONG).show()
                finish()
            }


    }

    private fun setObserver() {
        viewModel.responseGetBranchDistanceApiCall()
            .observe(this@BranchActivity, androidx.lifecycle.Observer {
                try {
                    val bean: DistanceData? = it.data
                    branchArrayList.forEachIndexed { index, branch ->
                        if (branch.id == bean?.branchId) {
                            branchArrayList[index].distanceStr = bean.distance.toString()
//                    Log.e("eeeeee", branchArrayList[index].distanceStr)
                        }
                    }
                    branchArrayListOther.forEachIndexed { index, branch ->
                        if (branch!!.id == bean?.branchId) {
                            branchArrayListOther[index]!!.distanceStr = bean.distance.toString()
                        }
                    }
                    setNearByBranchAdapter(branchArrayList)
                    setOtherBranchAdapter(branchArrayListOther, preventCustomerBookingOption)
                } catch (e: Exception) {

                }

            })
        viewModel.responseGetBranchByMerchantApiCall().observe(
            this,
            androidx.lifecycle.Observer {
                hideLoading()
                if (it != null) {
                    if (it.status == Constant.STATUS.SUCCESS) {
                        if (!it.data?.isActive!!) {
//                            showErrorMsg(resources.getString(R.string.inactive_merchant))
                            onBackPressed()
                        }
                        try {


                            var branchArrayList = ArrayList<Branch>()

                            if (it.data != null && !TextUtils.isEmpty(it.data!!.id))

                                branchArrayList = (it.data!!.branch as ArrayList<Branch>)


                            this.branchArrayList = (it.data!!.branch as ArrayList<Branch>)

                            setNearByBranchAdapter(branchArrayList)
                            setDistanceInBranchAdapter(branchArrayList)


                            setStatusBarColor(R.color.btn_default_dark)

                            if ((branchArrayList.isEmpty() || branchArrayList.size == 0)) {
                                binding.tvNearbyTitle.visibility = View.GONE
                                binding.llrvNearByBranch.visibility = View.GONE
                            } else {
                                binding.tvNearbyTitle.visibility = View.VISIBLE
                                binding.llrvNearByBranch.visibility = View.VISIBLE
                            }
                            var suffix =
                                if (it.data!!.total!! > 1) this.resources.getString(R.string.branches) else this.resources.getString(
                                    R.string.branch_str
                                )
                            binding.tvBranchCount.setText(Utility.convertToArabic("" + it.data!!.total) + suffix)

                            setStatusBarColor(R.color.btn_default_dark)
//                            }


                            binding.tvBankName.setText(it.data!!.name)
//                            binding.tvBankNameTool.setText(it.data!!.name)
                            showToolbar()
                            iconLogoUrl = it.data!!.logoURL.toString()
                            ImageDisplayUitls.displayImage(
                                it!!.data!!.logoURL,
                                this@BranchActivity,
                                binding.ivThumb, it.data!!.name
                            )


                        } finally {
//                            callDirectionAPI(branchArrayList)
                        }

                    } else {

                    }
                } else {

                }

            })

        var loop = 0
        viewModel.responseGetGetTokenGenerateApiCall().observe(this, Observer {
            hideLoading()
            loop=1
            if (it != null) {
                if (it.status == Constant.STATUS.SUCCESS) {
                    prefManager.setString(PrefConst.PREF_USER_TOKEN_SECURITY, it.data!!.token)
                    callTokenApi(1)

                    callApi(true, true)
                    callApi(false, true)
                } else {
                    callTokenApi(2)
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                }
            } else {
                callTokenApi(2)
                Toast.makeText(this, "Token Expire", Toast.LENGTH_LONG).show()
            }
        })
        viewModel.responseGetAninymousLoginApiCall().observe(this, Observer {
            hideLoading()
            if (it != null) {
                if (it.status == Constant.STATUS.SUCCESS) {
                    prefManager.setString(PrefConst.PREF_USER_ID, it.data!!.id)
                    prefManager.setString(PrefConst.PREF_FIRST_NAME, it.data!!.name)
                    prefManager.setString(PrefConst.PREF_LAST_NAME, it.data!!.lastName)
                    callTokenApi(3)

                } else {
                    callTokenApi(2)
                }
            } else {
                callTokenApi(2)
            }
        })

        viewModel.responseGetTicketByCustomerListApiCall().observe(
            this,
            androidx.lifecycle.Observer {

                hideLoading()
                if (it != null && it.data != null) {
                    if (it.status == Constant.STATUS.SUCCESS) {
                        ticketList = it.data!!.ticket
                        prefManager.saveTicketList(ticketList)

                        // if (ticketList.size > 0)
                        prefManager.setBoolean(PrefConst.TICKET_BOOKED_HIDE_IMAGE_SLIDER, false)

                        setSliderAdapter()

                    } else {
                        prefManager.saveTicketList(null)
                        var message =
                            if (!TextUtils.isEmpty(it.message)) it.message!! else resources.getString(
                                R.string.please_try_again
                            )
                    }
                } else {
                    prefManager.saveTicketList(null)
                }
            })
        viewModel.responseGetOtherBranchByMerchantApiCall().observe(
            this,
            androidx.lifecycle.Observer {

                if (it != null) {
                    try {
                        if (it.status == Constant.STATUS.SUCCESS) {
                            if (!it.data?.isActive!!) {

                                onBackPressed()
                            }

                            try {
                                if (it.data!!.isEservice!!) {
                                    iconLogoUrl = it.data!!.logoURL.toString()


                                }

                                if (page == 0) {

//                                setNearByBranchAdapter(true)
                                    binding.tvBankName.setText(it.data!!.name)
//                                    binding.tvBankNameTool.setText(it.data!!.name)

                                    var suffix =
                                        if (it.data!!.total!! > 1) this.resources.getString(R.string.branches) else this.resources.getString(
                                            R.string.branch_str
                                        )
                                    binding.tvBranchCount.setText(Utility.convertToArabic("" + it.data!!.total) + suffix)

                                    showToolbar()

                                    ImageDisplayUitls.displayImage(
                                        it!!.data!!.logoURL,
                                        this@BranchActivity,
                                        binding.ivThumb, it!!.data!!.name
                                    )

                                }

                                if (it.data != null && it.data!!.branch != null && it.data!!.branch!!.size > 0) {

                                    if (page == 0) {
                                        branchArrayListOther!!.clear()
                                    }

                                    var list = it.data!!.branch!!


                                    preventCustomerBookingOption =
                                        it.data!!.preventCustomerBookingOption!!
                                    branchArrayListOther!!.addAll(list)

                                    setOtherBranchAdapter(
                                        branchArrayListOther!!,
                                        preventCustomerBookingOption
                                    )
                                    try {
                                        setDistanceInOtherBranchAdapter(branchArrayListOther)
                                    } catch (e: Exception) {

                                    }


                                    async {
                                        /*var list=mDb.BranchListDao().getItemById(merchantID,false)
                                        Utils.print("DB List="+list.size)
                                        if (list.size>0) {
                                            mDb.BranchListDao().updateOther(branchArrayListOther)
                                        }
                                        else*/


                                    }
                                } else {


                                    if (page!! > 0) {
                                        page = page!! - 1
                                        return@Observer
                                    }

                                    if (page == 0) {
                                        //while get no data in 1st page

                                        setOtherBranchAdapter(
                                            ArrayList<Branch?>(),
                                            preventCustomerBookingOption
                                        )
                                    }
                                }
                            } finally {
                                // if (page == 0) {

                                if (binding.clMain.isVisible) else
                                    binding.clMain.visibility = View.VISIBLE
                                //}


                            }

                        } else {


                            binding.clMain.visibility = View.VISIBLE

                            var message =
                                if (!TextUtils.isEmpty(it.message)) it.message!! else resources.getString(
                                    R.string.please_try_again
                                )
                        }
                    } catch (e: java.lang.Exception) {
                    }

                } else {

                    binding.clMain.visibility = View.VISIBLE
                }
            })

    }

    private fun loadMoreStuff() {
        layoutManager = LinearLayoutManager(this@BranchActivity)
        binding.rvOtherBranch.layoutManager = layoutManager
        binding.rvOtherBranch.addItemDecoration(
            DividerItemDecoration(
                this,
                LinearLayoutManager.VERTICAL
            )
        )
        scrollListener = RecyclerViewLoadMoreScroll(layoutManager)
        scrollListener!!.setOnLoadMoreListener(object : OnLoadMoreListener {
            override
            fun onLoadMore() {
                otherBranchAdapter!!.addLoadingView()
                page = page!!.plus(1)
                println("Page: loadmore " + page)
//                callOtherBranchesApi(page!!, false)
            }
        })
        binding.rvOtherBranch.addOnScrollListener(scrollListener!!)
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

    private fun setNearByBranchAdapter(branchArrayList: ArrayList<Branch>) {
        Utils.print("dbHaveNoData branchArrayList=" + branchArrayList.size)
        if (branchArrayList.size > 0) {
            Utils.print("Double check 4=")
            binding.tvNearbyTitle.visibility = View.VISIBLE
            binding.llrvNearByBranch.visibility = View.VISIBLE
        } else {
            binding.tvNearbyTitle.visibility = View.GONE
            binding.llrvNearByBranch.visibility = View.GONE
        }

        if (branchAdapter != null) {
            branchAdapter!!.setData(branchArrayList)
        } else {
            binding.rvNearByBranch.setLayoutManager(
                LinearLayoutManager(
                    this@BranchActivity,
                    LinearLayoutManager.VERTICAL,
                    false
                )
            )
            binding.rvNearByBranch.addItemDecoration(
                DividerItemDecoration(
                    this,
                    LinearLayoutManager.VERTICAL
                )
            )

            branchAdapter =
                NearByBranchListAdapter(this@BranchActivity, this, branchArrayList)

            binding.rvNearByBranch.setAdapter(branchAdapter)
        }

    }

    private fun setDistanceInBranchAdapter(branchArrayList: java.util.ArrayList<Branch>) {

        if (mLastLocation?.latitude != null) {
            CoroutineScope(Dispatchers.IO).launch {
                branchArrayList.forEachIndexed { index, branch ->
                    val hashMap = HashMap<String, Any>()
                    hashMap.put("branchId", branch.id)
                    hashMap.put("branchlat", branch.lat.toString())
                    hashMap.put("branchlng", branch.long.toString())
                    hashMap.put("lat", mLastLocation?.latitude.toString())
                    hashMap.put("lng", mLastLocation?.longitude.toString())


                    viewModel.requestGetBranchDistanceApiCall(this@BranchActivity, hashMap)

                }
            }
        }


    }

    private fun setDistanceInOtherBranchAdapter(branchArrayList: java.util.ArrayList<Branch?>) {
        try {
            val localCopy = branchArrayList
            if (mLastLocation?.latitude != null) {
                CoroutineScope(Dispatchers.IO).launch {
                    localCopy.forEachIndexed { index, branch ->
                        val hashMap = HashMap<String, Any>()
                        hashMap.put("branchId", branch!!.id)
                        hashMap.put("branchlat", branch.lat.toString())
                        hashMap.put("branchlng", branch.long.toString())
                        hashMap.put("lat", mLastLocation?.latitude.toString())
                        hashMap.put("lng", mLastLocation?.longitude.toString())


                        viewModel.requestGetBranchDistanceApiCall(this@BranchActivity, hashMap)

                    }
                }
            }
        } catch (e: Exception) {

        }

    }

    private fun setOtherBranchAdapter(
        mList: ArrayList<Branch?>,
        preventCustomerBookingOption: String?,
    ) {

        if (mList.size > 0) {
            binding.llrvOtherBranch.visibility = View.VISIBLE
            binding.tvOtherBranchTitle.visibility = View.VISIBLE
            // binding.layoutNoData.visibility = View.GONE
        } else {
            binding.llrvOtherBranch.visibility = View.GONE
            binding.tvOtherBranchTitle.visibility = View.GONE
            //binding.layoutNoData.visibility = View.VISIBLE
        }

        if (page!! > 0) {

            otherBranchAdapter!!.notifyDataSetChanged()
        } else {
            otherBranchAdapter = OtherBranchesPaginationAdapter(
                this@BranchActivity,
                mList,
                this,
                preventCustomerBookingOption
            )
            binding.rvOtherBranch.setAdapter(otherBranchAdapter)
        }
        //mDb.BranchListDao().
    }

    private var mLastClickTime: Long = 0

    override fun onClick(view: View) {
        when (view.id) {
            R.id.llMAinSliderAdapter -> {
                var pos: Int = view.tag as Int
                var bean: TicketBean = adapterSlider!!.getItem(pos)

                var beanToSend: AddTicketResponseBean.Data = AddTicketResponseBean.Data()

                beanToSend.ticketType = bean.ticketType
                beanToSend.location = bean.location
                beanToSend.locationArabic = bean.locationArabic
                beanToSend.serviceName = bean.serviceName
                beanToSend.approxTime = bean.approxTime
                beanToSend.branchName = bean.branchName
                beanToSend.id = bean.id
                beanToSend.number = bean.number
                beanToSend.branchId = bean.branchId
                beanToSend.serviceName = bean.serviceName
                beanToSend.userId = bean.userId
                beanToSend.merchantName = bean.merchantName
                beanToSend.logoURL = bean.merchantLogo
                beanToSend.servingTime = if (bean.waitTime != null) bean.waitTime!! else 0.0
                beanToSend.customersCount = bean.customersCount
                beanToSend.processedByUserId = bean.processedByUserId
                beanToSend.status = bean.status
                beanToSend.starttime = bean.starttime
                beanToSend.textUnderTime = bean.textUnderTime
                beanToSend.arabicTextUnderTime = bean.arabicTextUnderTime
                beanToSend.textUnderTimeFinish = bean.textUnderTimeFinish
                beanToSend.arabicTextUnderTimeFinish = bean.arabicTextUnderTimeFinish
                beanToSend.appointmentEndTime = bean.appointmentEndTime
                beanToSend.createdAt = bean.createdAt
                beanToSend.counter = bean.counter


                    //ticket
                    beanToSend.notCallDetailsAPI = true
                    //ticket
                    beanToSend.eventLogo = bean.eventLogo
                    beanToSend.eventName = bean.eventName
                    beanToSend.starttime = bean.starttime
                    beanToSend.createdAt = bean.createdAt
                    beanToSend.endTime = bean.endTime
                    startActivity(
                        Intent(
                            this,
                            CustomerConfirmationTicketActivity::class.java
                            // CustomerTurnTicketActivity::class.java
                        ).putExtra(Constant.INTENT_EXTRA.TICKET_BEAN, beanToSend)
                            .putExtra(Constant.INTENT_EXTRA.DO_NOT_REFRESH, true)
                    )
            }
            R.id.imgBack -> {
                finish()
            }

            R.id.btnMore -> {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 2500) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                //other
                if (branchArrayListOther.size > 0) {
                    var position: Int = view.getTag() as Int
                    val intent = Intent(this@BranchActivity, BranchDetailActivity::class.java)
                    intent.putExtra(
                        Constant.INTENT_EXTRA.BRANCH_ID,
                        branchArrayListOther.get(position)!!.id
                    )
                    intent.putExtra(
                        Constant.INTENT_EXTRA.SELECTED_BRANCH_DISTANCE,
                        branchArrayListOther.get(position)!!.distance
                    )
                    intent.putExtra(
                        Constant.INTENT_EXTRA.BRANCH_BEAN,
                        branchArrayListOther.get(position)
                    )
                    intent.putExtra(Constant.INTENT_EXTRA.MERCHANT_ID, UserMerchantID)
                    startActivity(intent)

                } else {
                    Utils.print("Error - branchArrayListOther.size > 0 " + resources.getString(R.string.something_went_working))
                    // showErrorMsg()
                }
            }

            R.id.llMain -> {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 2500) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                //other
                if (branchArrayListOther.size > 0) {
                    var position: Int = view.getTag() as Int
                    val intent = Intent(this@BranchActivity, BranchDetailActivity::class.java)
                    intent.putExtra(
                        Constant.INTENT_EXTRA.PREVENT_CUSTOMER_BOOKING,
                        preventCustomerBookingOption
                    )
                    intent.putExtra(
                        Constant.INTENT_EXTRA.BRANCH_ID,
                        branchArrayListOther.get(position)!!.id
                    )
                    intent.putExtra(
                        Constant.INTENT_EXTRA.SELECTED_BRANCH_DISTANCE,
                        branchArrayListOther.get(position)!!.distance
                    )
                    intent.putExtra(
                        Constant.INTENT_EXTRA.BRANCH_BEAN,
                        branchArrayListOther.get(position)
                    )
                    intent.putExtra(Constant.INTENT_EXTRA.MERCHANT_ID, UserMerchantID)
                    startActivity(intent)

                } else {
                    Utils.print("Error - branchArrayListOther.size > 0 " + resources.getString(R.string.something_went_working))
                    // showErrorMsg()
                }
            }
        }
    }

    //near by click
    private var mLastClickTimeNearBy: Long = 0
    override fun onItemClickMain(position: Int) {
        if (branchArrayList.size > 0) {
            val bean = branchArrayList.get(position)



            Utils.print("onItemClickMain 1= " + mLastClickTimeNearBy)
            if (SystemClock.elapsedRealtime() - mLastClickTimeNearBy < 1500) {
                return;
            }
            mLastClickTimeNearBy = SystemClock.elapsedRealtime();
            Utils.print("onItemClickMain 2= " + mLastClickTimeNearBy)
            if (branchArrayList.size > 0) {

                /*if (TextUtils.isEmpty(bean.distanceStr)) {

                    return
                }*/
                Utils.print("onItemClickMain 3= " + bean.distanceStr)

                val intent = Intent(this@BranchActivity, BranchDetailActivity::class.java)
                intent.putExtra(Constant.INTENT_EXTRA.BRANCH_BEAN, bean)
                intent.putExtra(Constant.INTENT_EXTRA.BRANCH_ID, bean.id)
                intent.putExtra(Constant.INTENT_EXTRA.FROM_NEAR_BY, false)
                intent.putExtra(Constant.INTENT_EXTRA.MERCHANT_ID, UserMerchantID)

                if (branchAdapter != null) {
                    var currentBean = branchAdapter!!.list[position]

                    intent.putExtra(
                        Constant.INTENT_EXTRA.SELECTED_BRANCH_DISTANCE,
                        currentBean.distance
                    )
                    intent.putExtra(
                        Constant.INTENT_EXTRA.SELECTED_BRANCH_MINUTES,
                        currentBean.durationValueInMinutes
                    )

                    if (branchArrayList != null && branchArrayList.size > 0)
                        intent.putExtra(Constant.INTENT_EXTRA.NEAR_BY_BRANCH_LIST, branchArrayList)

                }

                startActivity(intent)

            } else {
                Utils.print("Error - branchArrayList.size > 0 " + resources.getString(R.string.something_went_working))
                // showErrorMsg()
            }
        }


    }

    fun isLoading(): Boolean {
        return binding.llProgressBar.visibility == View.VISIBLE
    }

    fun showLoading() {
        // binding.llProgressBar.visibility = View.VISIBLE

        runOnUiThread {

            binding.llProgressBar.visibility = View.VISIBLE
        }
    }

    fun hideLoading() {

        runOnUiThread { binding.llProgressBar.visibility = View.GONE }
    }

    /**
     * Load image to zoom in and zoom out
     */
    fun loadZoomImage(context: Context, imagePath: String, imageRes: Int) {
        if (!TextUtils.isEmpty(imagePath) || imageRes > 0) {
            val intent = Intent(context, ImageViewZoomActivity::class.java)
            intent.putExtra(Constant.INTENT_EXTRA.IMAGE_PATH, imagePath)
            intent.putExtra(Constant.INTENT_EXTRA.IMAGE_RES, imageRes)
            context.startActivity(intent)
        }
    }

    private fun showToolbar() {

        binding.appBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { barLayout, verticalOffset ->

            var a = Math.abs((verticalOffset))
            if (a == binding.appBar.getTotalScrollRange()) {
                setStatusBarColor(R.color.home_bg_color_gray)
                setupSwipeRefresh(false)
            } else if (a != 0 && a < 610) {
                setupSwipeRefresh(false)
            } else if (verticalOffset == 0) {
                setupSwipeRefresh(true)

                setStatusBarColor(R.color.home_bg_color_gray)
//                }
            } else {
                setupSwipeRefresh(true)
            }

        })


    }

    private fun setupSwipeRefresh(setEnable: Boolean) {

        binding.swipeRefresh.isEnabled = setEnable
    }

    var locationUpdate = false
    override fun onLocationUpdate(location: Location?) {
        Utils.print("onLocationUpdate in Branch Details")
        mLastLocation = location!!
        Log.e("mLastLocation", "${mLastLocation!!.latitude}---${mLastLocation!!.longitude}")
        if (getLocationUpdate != null) {
            getLocationUpdate!!.setCancelUpdateCall(true)
        }
        callApi(true, true)
        callApi(false, true)
        if (!locationUpdate) {
            locationUpdate = true
            showLoading()
//            Handler().postDelayed(Runnable {
////                hideLoading()
////                callApi(true, false)
////                callApi(false, false)
//            }, 3000)

        }
    }

    private fun checkPermission() {

        Log.e("requestpermission","called")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            TedPermission.create()
                .setPermissionListener(permissionListener)
                //.setDeniedMessage(resources.getString(R.string.permission_message))
                .setPermissions(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                )
                .check()
        } else {
            TedPermission.create()
                .setPermissionListener(permissionListener)
                //.setDeniedMessage(resources.getString(R.string.permission_message))
                .setPermissions(

                    Manifest.permission.ACCESS_FINE_LOCATION
                )
                .check()
        }

    }

    override fun onPermissionGranted() {
        getLocationUpdate = GetLocationUpdate(this, object : MyLocationListener {
            override fun onLocationUpdate(location: Location?) {
                getUserLatitude(this@BranchActivity)
                getUserLongitude(this@BranchActivity)



            }
        })
        if (getLocationUpdate != null)
            getLocationUpdate!!.onStartLocation()
    }

    override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
       Log.e("sss","sdsdsd")

    }


    fun getUserLongitude(activity: Activity?): String {
        //  return "35.8377"

        var oldLng = appManager.getPrefManager().getString(PrefConst.USER_CURRENT_LNG)
        var tempLng = appManager.getPrefManager().getString(PrefConst.USER_CURRENT_LNG_TEMP)
        if (activity != null && TextUtils.isEmpty(oldLng)
        ) {

            prefManager.setString(PrefConst.USER_CURRENT_LNG, tempLng)

            return tempLng
        }

        return oldLng
    }

    fun getUserLatitude(activity: Activity?): String {//"","locationLat":"22.2864036","locationLng":"73.2303622"
        //"22.2860338","locationLng":"73.2300619
        //return "31.9805"

        var oldLat = appManager.getPrefManager().getString(PrefConst.USER_CURRENT_LAT)
        var tempLat = appManager.getPrefManager().getString(PrefConst.USER_CURRENT_LAT_TEMP)
        if (activity != null && TextUtils.isEmpty(oldLat)
        ) {


            prefManager.setString(PrefConst.USER_CURRENT_LAT, tempLat)

            return tempLat
        }

        return oldLat

    }

    var updater: Runnable? = null
    val timerHandler = Handler()
    fun refreshTicketListRepeat() {

        updater = Runnable {
            var isLoad =
                (ticketList.size > 0)
            if (ticketList.size > 0) {
                try {
                    callTokenApi(3)


                } catch (e: Exception) {
                    Toast.makeText(this, "-$e",Toast.LENGTH_SHORT).show()
                }

            }
            timerHandler.postDelayed(updater!!, Constant.REFRESH_TIME_HOME)
        }
        timerHandler.post(updater!!)
    }

    private fun updateLanguage() {

        try {
            MyApplication.localeManager.setNewLocale(this, resources.configuration.locale.toString())

        }catch (e:Exception){
            Toast.makeText(this, "-$e",Toast.LENGTH_SHORT).show()
        }


    }

}