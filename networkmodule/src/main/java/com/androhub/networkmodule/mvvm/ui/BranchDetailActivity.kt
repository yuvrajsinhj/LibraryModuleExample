package com.androhub.networkmodule.mvvm.ui

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.androhub.networkmodule.AppManager
import com.androhub.networkmodule.MyApplication
import com.androhub.networkmodule.PrefConst
import com.androhub.networkmodule.R
import com.androhub.networkmodule.adapter.NearByBranchMainAdapter
import com.androhub.networkmodule.adapter.RowModel
import com.androhub.networkmodule.base.BaseActivityMain
import com.androhub.networkmodule.databinding.ActivityBranchDetailsBinding
import com.androhub.networkmodule.mvvm.model.request.FasterNearByDataBean
import com.androhub.networkmodule.mvvm.model.response.GetFasterBranchListResponseBean
import com.androhub.networkmodule.mvvm.model.response.getbranchbymerchant.Branch
import com.androhub.networkmodule.mvvm.model.response.getbranchdetails.BranchTime
import com.androhub.networkmodule.mvvm.model.response.getbranchdetails.Data
import com.androhub.networkmodule.mvvm.model.response.getbranchdetails.Service
import com.androhub.networkmodule.mvvm.ui.fragment.CustomerBookingTicketAppointBottomSheetFragment
import com.androhub.networkmodule.mvvm.ui.fragment.OpenHoursBottomsheetFragment
import com.androhub.networkmodule.mvvm.viewmodel.GetBranchByBranchIdModel
import com.androhub.networkmodule.utils.Constant
import com.androhub.networkmodule.utils.ImageDisplayUitls
import com.androhub.networkmodule.utils.Utils
import com.androhub.networkmodule.utils.frombuilder.dynamicFormBuilder.DynamicGenerateFormActivity

class BranchDetailActivity : BaseActivityMain(), View.OnClickListener,
    NearByBranchMainAdapter.BranchInterfaceTempCity,
    NearByBranchMainAdapter.BranchInterfaceTempCuntry,
    NearByBranchMainAdapter.BranchInterfaceTempState {
    var nearByBranchList = ArrayList<Branch>()
    var nearByCurrentBranchDistance: Double = -1.0
    var nearByCurrentBranchMinutes: Double = -1.0

    lateinit var binding: ActivityBranchDetailsBinding
    private var branchModel = ArrayList<Data.ParentService>()
    private var tempbranchModel: MutableList<RowModel> = mutableListOf()
    private var bankTimeArray = ArrayList<BranchTime>()

    var tempbranchAdapter: NearByBranchMainAdapter? = null

    //    var branchTimingAdapter: BranchTimingAdapter? = null
    var mainResponse: Data? = null

    private var branchBean: Branch? = null
    private var branchID = ""
    private var fromWhere = ""
    private var merchantID = ""


    lateinit var viewModel: GetBranchByBranchIdModel
    var fromNearBy: Boolean = false
    var preventCustomerBookingOption: String = ""
    var isCustomerSegmentAvailable: Boolean = false
    var isSegmentAPIRunning: Boolean = false

    //flag for to check service list again call api and check once is service availlable or nor
    var callAPIAgain: Boolean = false
    var selectedServiceBean: Service? = null

    companion object {
        var clickOnButton: Boolean = false
        var customerBookingTicketAppointBottomSheetFragment: CustomerBookingTicketAppointBottomSheetFragment? =
            null

        var openHoursBottomsheetFragment: OpenHoursBottomsheetFragment? = null
    }

    //for this app
//    lateinit var prefManager: PrefManager
//    lateinit var appManager: AppManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_branch_details)
        binding.lifecycleOwner = this
        viewModel = ViewModelProvider(this).get(GetBranchByBranchIdModel::class.java)
        setStatusBarColor(R.color.home_bg_color_gray)
        appManager = AppManager.getInstance(this)
        prefManager = appManager.getPrefManager()

        getIntentData()
        initUi()
    }

    override fun initUi() {
        inItListeners()

        binding.tvBranchTiming.setOnClickListener(this)
        binding.ivBranchTiming.setOnClickListener(this)
//        binding.rvBranchTiming?.layoutManager =
//            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvNearByBranch.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

    }

    override fun getContext(): Context {
        return this
    }

    override fun getLayoutId(): View {
        return binding.root
    }

    private fun inItListeners() {
        binding.ivClose.setOnClickListener(this)


        binding.llTodayTime.setOnClickListener(this)

    }

    fun getIntentData() {

        if (intent.hasExtra(Constant.INTENT_EXTRA.BRANCH_ID)) {
            preventCustomerBookingOption =
                intent.getStringExtra(Constant.INTENT_EXTRA.PREVENT_CUSTOMER_BOOKING).toString()
            branchID = intent.getStringExtra(Constant.INTENT_EXTRA.BRANCH_ID)!!
            var tempList = prefManager.savedBranchList
            mainResponse = prefManager.savedBranchData
            var tempID = prefManager.getString(PrefConst.BRANCH_ID_TEMP)
            if (tempList != null && tempID == branchID && mainResponse != null) {
                binding.llMain.visibility = View.VISIBLE
                tempbranchModel = tempList
                setVerticalAdapter()
                mainResponse!!.merchantID.let {

                    merchantID = it!!
                }
                binding.tvBankName.text = mainResponse?.branchName
                binding.tvMerchantName.text = mainResponse?.merchantName
                binding.tvBranchTiming.text = mainResponse?.showTextStatus
                binding.tvBranchTiming.setTextColor(Color.parseColor(mainResponse!!.colorCode))
                if (branchBean != null) {

                } else {
                    var bean = Branch()
                    bean.name = mainResponse!!.branchName
                    bean.merchantName = mainResponse!!.merchantName
                    bean.merchantId = mainResponse!!.merchantID
                    bean.logoURL = mainResponse!!.logoURL
                    bean.lat = mainResponse!!.lat
                    bean.long = mainResponse!!.long
                    branchBean = bean

                }



                ImageDisplayUitls.displayImage(
                    mainResponse!!.logoURL,
                    this@BranchDetailActivity,
                    binding.ivThumbDetails, mainResponse!!.merchantName
                )


//                        setBranchTimingData()

                bankTimeArray = (mainResponse!!.branchTime as ArrayList<BranchTime>)
                //get time in between and update branch opn close
                mainResponse?.isOpenBranch = mainResponse!!.isOpenBranch
                updateBranchOpenCloseUI()
                if (tempbranchAdapter != null) {
                    tempbranchAdapter!!.setOpenCloseBranch(mainResponse!!.isOpenBranch!!)
                }

            } else {
                binding.llMain.visibility = View.GONE
            }
            callApi(1)


        }

        if (intent.hasExtra(Constant.INTENT_EXTRA.NEAR_BY_BRANCH_LIST)) {
            nearByBranchList =
                (intent.getSerializableExtra(Constant.INTENT_EXTRA.NEAR_BY_BRANCH_LIST) as ArrayList<Branch>?)!!

            if (intent.hasExtra(Constant.INTENT_EXTRA.SELECTED_BRANCH_MINUTES)) {
                nearByCurrentBranchMinutes =
                    intent.getDoubleExtra(Constant.INTENT_EXTRA.SELECTED_BRANCH_MINUTES, -1.0)
            }

        }
        if (intent.hasExtra(Constant.INTENT_EXTRA.SELECTED_BRANCH_DISTANCE)) {
            nearByCurrentBranchDistance =
                intent.getDoubleExtra(Constant.INTENT_EXTRA.SELECTED_BRANCH_DISTANCE, -1.0)
        }

        if (intent.hasExtra(Constant.INTENT_EXTRA.MERCHANT_ID)) {
            merchantID = intent.getStringExtra(Constant.INTENT_EXTRA.MERCHANT_ID)!!
        }
        if (intent.hasExtra(Constant.INTENT_EXTRA.FROM_WHERE)) {
            fromWhere = intent.getStringExtra(Constant.INTENT_EXTRA.FROM_WHERE)!!
        }
        if (intent.hasExtra(Constant.INTENT_EXTRA.FROM_NEAR_BY)) {
            fromNearBy = intent.getBooleanExtra(Constant.INTENT_EXTRA.FROM_NEAR_BY, false)
            Utils.print("fromNearBy=" + fromNearBy)
        }
        if (intent.hasExtra(Constant.INTENT_EXTRA.PREVENT_CUSTOMER_BOOKING)) {

        }


    }

    private fun callApi(tag: Int = 1, serviceBean: Service? = null) {

        if (tag == 3 && isCustomerSegmentAvailable) {
            isSegmentAPIRunning = true
            if (TextUtils.isEmpty(merchantID)) {
                return
            }
            //get customer segment
            val hashMap = HashMap<String, Any>()
            hashMap.put("merchantId", merchantID)
            // hashMap.put("merchantID", merchantID)
            //hashMap.put("branchID", branchID)
            hashMap.put("branchId", branchID)
            //hashMap.put("isoCode", prefManager.getString(PrefConst.PREF_ISO_CODE))
            hashMap.put("iso", prefManager.getString(PrefConst.PREF_ISO_CODE))
            hashMap.put("phone", prefManager.getString(PrefConst.PREF_PHONE))

            viewModel.requestGetSegmentApiCall(this, hashMap)
//            showLoading()
        }

        if (tag == 2) {

            var beanToSend = FasterNearByDataBean()
            beanToSend.serviceId = serviceBean!!.id!!
            viewModel.requestGetFasterBranchListApiCall(this, beanToSend)

            showLoading()
        } else if (tag == 1) {
            var tempList = prefManager.savedBranchList
            var tempID = prefManager.getString(PrefConst.BRANCH_ID_TEMP)
            if (tempList == null || tempID != branchID || mainResponse == null) {
                showLoading()
            }
            val hashMap = HashMap<String, Any>()
            hashMap.put("branchID", branchID)
            hashMap.put("isFromSDK", true)
            viewModel.requestGetBranchDetailsByBranchIdApiCall(this, hashMap)
        } else if (tag == 4) {

        }
        setObserver(serviceBean)

    }

    private fun setObserver(serviceBean: Service? = null) {

        viewModel.responseGetSegmentApiCall()
            .observe(this, androidx.lifecycle.Observer {
                hideLoading()
//                hideSegmentLoading()
                isSegmentAPIRunning = false
                if (it != null) {
                    if (it.status == Constant.STATUS.SUCCESS) {
                        val data = it.dataSegment
                        if (data != null) {
                            val name = data.segmentName
                            prefManager.setString(PrefConst.USER_CURRENT_SEGMENT, name)
                            val prefix = data.prefix
                            prefManager.setString(PrefConst.USER_CURRENT_SEGMENT_PREFIX, prefix)

                        } else {

                            prefManager.setString(PrefConst.USER_CURRENT_SEGMENT, "")
                            prefManager.setString(PrefConst.USER_CURRENT_SEGMENT_PREFIX, "")
                        }

                    } else {

                        prefManager.setString(PrefConst.USER_CURRENT_SEGMENT, "")
                        prefManager.setString(PrefConst.USER_CURRENT_SEGMENT_PREFIX, "")

                    }
                } else {

                    prefManager.setString(PrefConst.USER_CURRENT_SEGMENT, "")
                    prefManager.setString(PrefConst.USER_CURRENT_SEGMENT_PREFIX, "")

                }
            })
        viewModel.responseGetFasterBranchListApiCall()
            .observe(this, androidx.lifecycle.Observer {
                hideLoading()
                if (it != null) {
                    if (it.status == Constant.STATUS.SUCCESS) {

                        var dataList = it.data

                        if (serviceBean != null && dataList != null && dataList.size == 1 && dataList[0] != null && !TextUtils.isEmpty(
                                dataList[0]!!.branchId
                            ) && dataList[0]!!.branchId.equals(serviceBean.branchID)
                        ) {
                            gotoNext(serviceBean)
                        } else if (dataList != null && dataList.size > 0) {
                            //logic to get fast branch by location or near by
                            calculateFasterBranch(serviceBean!!, dataList)

                        } else {
                            gotoNext(serviceBean!!)
                        }
                    } else {
                        hideLoading()
                        var message =
                            if (!TextUtils.isEmpty(it.message)) it.message!! else getString(R.string.queue_not_found)

                        showErrorMsg(message)
                        //  showDialogAsStatus(response.status, response.message)
                    }
                } else {
                    hideLoading()
                    showErrorMsg(resources.getString(R.string.in_error))
                }
            })
        viewModel.responseGetBranchByBranchIdApiCall().observe(this, androidx.lifecycle.Observer {
            hideLoading()
            if (it != null) {
                if (it.status == Constant.STATUS.SUCCESS) {


                    try {
                        branchModel = ArrayList()

                        mainResponse = it.data!!

                        MyApplication.getAppManager().prefManager.setString(
                            PrefConst.BRANCH_TIME_ZONE,
                            it.data!!.timeZone
                        )
                        Constant.TEMP_SHIFT = mainResponse!!.todayTime!!.shifts!!
                        mainResponse?.merchantID?.let { merchantID = it }
                        if (!mainResponse?.branchActive!!) {
                            showErrorMsg(resources.getString(R.string.inactive_branch))
                            onBackPressed()
                            return@Observer

                        } else if (!mainResponse?.merchantActive!!) {
                            showErrorMsg(resources.getString(R.string.inactive_merchant))
                            clickOnButton = false
                            onBackPressed()
                            return@Observer

                        }


                        tempbranchModel.clear()
                        branchModel = (it.data!!.parentService as ArrayList<Data.ParentService>)
                        it.data!!.parentService?.forEachIndexed { index, element ->
                            // ...
                            tempbranchModel.add(
                                RowModel(
                                    RowModel.COUNTRY,
                                    it.data!!.parentService!![index]
                                )
                            )

                        }
                        prefManager.saveBranchList(null)
                        prefManager.saveBranchData(null)
                        prefManager.saveBranchData(mainResponse)
                        prefManager.setString(PrefConst.BRANCH_ID_TEMP, branchID)

                        prefManager.saveBranchList(tempbranchModel)

                        if (callAPIAgain || (branchModel.size > 0)) {

                            setVerticalAdapter()
                        } else {
                            callAPIAgain = true
                            //someTimes apis response service not show so call API once again to check service
                            callApi(1)
                        }
                        /**
                         * Segment logic - if we get segment as a customer then call segment api otherwise it will take branch level segment from backend
                         */
                        var segmentationAllow = mainResponse!!.segmentationAllow
                        var segmentBranchLevel =
                            if (mainResponse != null && !TextUtils.isEmpty(mainResponse!!.segment)) mainResponse!!.segment else ""
                        isCustomerSegmentAvailable =
                            (segmentationAllow!! && (!TextUtils.isEmpty(segmentBranchLevel) && segmentBranchLevel == Constant.SEGMENT.CUSTOMER))
                        Utils.print("isCustomerSegmentAvailable $isCustomerSegmentAvailable")
                        callApi(3)


                    } catch (e: Exception) {
                        Log.e("errorMSG", e.message.toString())
                    } finally {

                        mainResponse!!.merchantID.let {

                            merchantID = it!!
                        }
                        binding.tvBankName.text = it.data?.branchName
                        binding.tvMerchantName.text = it.data?.merchantName
                        binding.tvBranchTiming.text = it.data?.showTextStatus
                        binding.tvBranchTiming.setTextColor(Color.parseColor(it.data!!.colorCode))
                        if (branchBean != null) {

                        } else {
                            var bean = Branch()
                            bean.name = mainResponse!!.branchName
                            bean.merchantName = mainResponse!!.merchantName
                            bean.merchantId = mainResponse!!.merchantID
                            bean.logoURL = mainResponse!!.logoURL
                            bean.lat = mainResponse!!.lat
                            bean.long = mainResponse!!.long
                            branchBean = bean

                        }


                        ImageDisplayUitls.displayImage(
                            it.data!!.logoURL,
                            this@BranchDetailActivity,
                            binding.ivThumbDetails, mainResponse!!.merchantName
                        )


//                        setBranchTimingData()

                        bankTimeArray = (it.data!!.branchTime as ArrayList<BranchTime>)
                        //get time in between and update branch opn close
                        mainResponse?.isOpenBranch = it.data!!.isOpenBranch
                        updateBranchOpenCloseUI()
                        if (tempbranchAdapter != null) {
                            tempbranchAdapter!!.setOpenCloseBranch(mainResponse!!.isOpenBranch!!)
                        }
                    }

                } else {

                    hideLoading()
                    binding.llMain.visibility = View.VISIBLE
                    var message =
                        if (!TextUtils.isEmpty(it.message)) it.message!! else resources.getString(R.string.please_try_again)
//                    showDialogAsStatus(it.status, message)
                }
            } else {
                hideLoading()
                binding.llMain.visibility = View.VISIBLE
                showErrorMsg(resources.getString(R.string.in_error))
            }
        })


    }

    private fun updateBranchOpenCloseUI() {
        //if closed then change text
        if (!mainResponse!!.isOpenBranch!!)
            binding.txtLabel.text = resources.getString(R.string.services)


    }

    private fun setAdapterData() {

        tempbranchAdapter = NearByBranchMainAdapter(
            this,
            tempbranchModel,
            this, this, this,
            fromNearBy,
            mainResponse!!.isOpenBranch!!,
            mainResponse!!.inGraceTime!!,
            mainResponse!!.inBreak!!,
            preventCustomerBookingOption!!
        )

        binding.rvNearByBranch.adapter = tempbranchAdapter

        branchModel = (mainResponse!!.parentService as ArrayList<Data.ParentService>)
        if (branchModel.size > 0) {
            binding.rvNearByBranch.visibility = View.VISIBLE
            binding.layoutNoData.visibility = View.GONE
        } else {

            binding.rvNearByBranch.visibility = View.GONE
            binding.layoutNoData.visibility = View.VISIBLE
        }
        binding.llMain.visibility = View.VISIBLE
    }

    private fun setVerticalAdapter() {
        hideLoading()
        setAdapterData()
    }

    private fun gotoNext(service: Service?, isSkip: Boolean = false) {
        if (!mainResponse?.merchantActive!!) {
            showErrorMsg(resources.getString(R.string.inactive_merchant))
            clickOnButton = false
            onBackPressed()
            return

        }
        selectedServiceBean = service
        hideLoading()


        //open segment screen every launch time
        /***
         * Must need to pass metchant id, name , logo
         */


        customerBookingTicketAppointBottomSheetFragment =
            CustomerBookingTicketAppointBottomSheetFragment()


        if (service != null) {
            //Utils.print("onItemClickMain 4 = "+mLastClickTime)
            if (mainResponse != null) {
                service.merchantID = merchantID
                service.merchantName = mainResponse!!.merchantName!!
                service.branchName = mainResponse!!.branchName!!
                service.branchName = mainResponse!!.branchName!!
                service.logoURL = mainResponse!!.logoURL!!
                service.isOpenBranch = mainResponse!!.isOpenBranch!!


            }
            val bundle = Bundle()
            bundle.putSerializable(Constant.INTENT_EXTRA.SERVICE_BEAN, service)
            bundle.putSerializable(Constant.INTENT_EXTRA.TIME_BEAN, bankTimeArray)
            bundle.putBoolean(Constant.INTENT_EXTRA.FROM_NEAR_BY, fromNearBy)
            bundle.putBoolean(Constant.INTENT_EXTRA.IN_BREAK, mainResponse!!.inBreak!!)
            bundle.putBoolean(Constant.INTENT_EXTRA.IN_GRACETIME, mainResponse!!.inGraceTime!!)
            bundle.putDouble(
                Constant.INTENT_EXTRA.SELECTED_BRANCH_DISTANCE,
                nearByCurrentBranchDistance
            )
            if (nearByBranchList != null && nearByBranchList.size > 0) {
                bundle.putSerializable(
                    Constant.INTENT_EXTRA.NEAR_BY_BRANCH_LIST,
                    nearByBranchList
                )

                bundle.putDouble(
                    Constant.INTENT_EXTRA.SELECTED_BRANCH_MINUTES,
                    nearByCurrentBranchMinutes
                )
            }
            customerBookingTicketAppointBottomSheetFragment!!.arguments = bundle
        }
        customerBookingTicketAppointBottomSheetFragment!!.show(
            supportFragmentManager,
            customerBookingTicketAppointBottomSheetFragment!!.tag
        )

        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up)


    }

    private fun calculateFasterBranch(
        serviceBean: Service,
        dataList: ArrayList<GetFasterBranchListResponseBean.Data?>?,
    ) {
        var fastBranchList = ArrayList<Branch>()
        try {


            for ((index, value) in dataList!!.withIndex()) {
                var currentId = value!!.branchId
                for ((index, valueNearBy) in nearByBranchList.withIndex()) {
                    if (valueNearBy.id.equals(currentId)) {
                        fastBranchList.add(valueNearBy)
                        break
                    }
                }
            }

        } finally {
            if (fastBranchList.size > 0) {

                var fastBranchBean = fastBranchList.get(0)
                if (serviceBean.branchID.equals(fastBranchBean.id)) {
                    //same branch so no need to open faster
                } else serviceBean.nearByFastBranch = fastBranchBean
            }
            gotoNext(serviceBean)
        }

    }


    override fun onItemClickMainCity(position: Int, view: View?) {
        if (!clickOnButton) {
            clickOnButton = true
        } else return

//        //For checking isBranch open at booking
//        if (!checkIsLogin(activity!!, false)) {
//            clickOnButton = false
//            showSuccessMsg(resources.getString(R.string.login_msg))
//            val intent = Intent(mContext, LoginActivity::class.java)
//            intent.putExtra(
//                Constant.INTENT_EXTRA.IS_TICKET_BOOKING,
//                true
//            )
//            getResultLogin.launch(intent)
//        }else
//        {
        if (tempbranchAdapter != null) {
            //  Utils.print("onItemClickMain 3 = "+mLastClickTime)

            val service: Data.Child1 = tempbranchAdapter!!.mArrayList.get(position).subChieldService

            var serviceBean = Service()
            serviceBean.branchID = service.branchID
            serviceBean.name = service.name
            serviceBean.id = service.id
            serviceBean.appointmentTime = service.appointmentTime
            serviceBean.appointmentPrefix = service.appointmentPrefix
            serviceBean.englishServiceName = service.englishName
            serviceBean.description = service.description
            serviceBean.servingTime = service.servingTime
            serviceBean.maxRebookTime = service.maxRebookTime
            serviceBean.lang = service.lang
            serviceBean.prefix = service.prefix

                 if (!service.form!!.id.isNullOrEmpty()) {
                    serviceBean.form = service.form
                    clickOnButton = false
                    val intent = Intent(this, DynamicGenerateFormActivity::class.java)
                    intent.putExtra(Constant.INTENT_EXTRA.SERVICE_BEAN, serviceBean)
                    intent.putExtra(Constant.INTENT_EXTRA.MAIN_RESPONSE, mainResponse)
                    intent.putExtra(Constant.INTENT_EXTRA.BRANCH_BEAN, branchBean)
                    intent.putExtra(Constant.INTENT_EXTRA.MERCHANT_ID, merchantID)
                    intent.putExtra(Constant.INTENT_EXTRA.BANK_TIME_ARRAY, bankTimeArray)
                    intent.putExtra(Constant.INTENT_EXTRA.FROM_NEAR_BY, fromNearBy)
                    intent.putExtra(Constant.INTENT_EXTRA.SELECTED_BRANCH_DISTANCE,
                        nearByCurrentBranchDistance)
                    if (nearByBranchList.size > 0) {
                        intent.putExtra(Constant.INTENT_EXTRA.NEAR_BY_BRANCH_LIST, nearByBranchList)
                        intent.putExtra(Constant.INTENT_EXTRA.SELECTED_BRANCH_MINUTES,
                            nearByCurrentBranchMinutes)
                    }
//                intent.putExtra(Constant.INTENT_EXTRA.BRANCH_ID, branchID)
//                intent.putExtra(Constant.INTENT_EXTRA.MERCHANT_LOGO_URL, mainResponse!!.logoURL)
//                intent.putExtra(Constant.INTENT_EXTRA.MERCHANT_NAME, mainResponse!!.merchantName)
                    startActivity(intent)
                } else {
            gotoNext(serviceBean)
                }
            //}


        }
//        }

        if (isSegmentAPIRunning)
            Utils.makeMeShake(view)


    }

    override fun onItemClickMainCuntry(position: Int, view: View?) {
        if (!clickOnButton) {
            clickOnButton = true
        } else return

//        //For checking isBranch open at booking
//        if (!checkIsLogin(activity!!, false)) {
//            clickOnButton = false
//            showSuccessMsg(resources.getString(R.string.login_msg))
//            val intent = Intent(mContext, LoginActivity::class.java)
//            intent.putExtra(
//                Constant.INTENT_EXTRA.IS_TICKET_BOOKING,
//                true
//            )
//            getResultLogin.launch(intent)
//        }else{
        if (tempbranchAdapter != null) {
            //  Utils.print("onItemClickMain 3 = "+mLastClickTime)
            val service: Data.ParentService =
                tempbranchAdapter!!.mArrayList.get(position).parentService

            var serviceBean = Service()
            serviceBean.branchID = service.branchID
            serviceBean.name = service.name
            serviceBean.id = service.id
            serviceBean.appointmentTime = service.appointmentTime
            serviceBean.appointmentPrefix = service.appointmentPrefix
            serviceBean.englishServiceName = service.englishName
            serviceBean.description = service.description
            serviceBean.servingTime = service.servingTime
            serviceBean.maxRebookTime = service.maxRebookTime
            serviceBean.lang = service.lang
            serviceBean.prefix = service.prefix


                 if (!service.form!!.id.isNullOrEmpty()) {
                    serviceBean.form = service.form
                    clickOnButton = false
                    val intent = Intent(this, DynamicGenerateFormActivity::class.java)
                    intent.putExtra(Constant.INTENT_EXTRA.SERVICE_BEAN, serviceBean)
                    intent.putExtra(Constant.INTENT_EXTRA.MAIN_RESPONSE, mainResponse)
                    intent.putExtra(Constant.INTENT_EXTRA.BRANCH_BEAN, branchBean)
                    intent.putExtra(Constant.INTENT_EXTRA.MERCHANT_ID, merchantID)
                    intent.putExtra(Constant.INTENT_EXTRA.BANK_TIME_ARRAY, bankTimeArray)
                    intent.putExtra(Constant.INTENT_EXTRA.FROM_NEAR_BY, fromNearBy)
                    intent.putExtra(Constant.INTENT_EXTRA.SELECTED_BRANCH_DISTANCE,
                        nearByCurrentBranchDistance)
                    if (nearByBranchList.size > 0) {
                        intent.putExtra(Constant.INTENT_EXTRA.NEAR_BY_BRANCH_LIST, nearByBranchList)
                        intent.putExtra(Constant.INTENT_EXTRA.SELECTED_BRANCH_MINUTES,
                            nearByCurrentBranchMinutes)
                    }
//                intent.putExtra(Constant.INTENT_EXTRA.BRANCH_ID, branchID)
//                intent.putExtra(Constant.INTENT_EXTRA.MERCHANT_LOGO_URL, mainResponse!!.logoURL)
//                intent.putExtra(Constant.INTENT_EXTRA.MERCHANT_NAME, mainResponse!!.merchantName)
                    startActivity(intent)

                } else {
            gotoNext(serviceBean)
            }
//                }


//            }
        }


    }

    override fun onItemClickMainState(position: Int, view: View?) {
        if (!clickOnButton) {
            clickOnButton = true
        } else return

//        //For checking isBranch open at booking
//        if (!checkIsLogin(activity!!, false)) {
//            clickOnButton = false
//            showSuccessMsg(resources.getString(R.string.login_msg))
//            val intent = Intent(mContext, LoginActivity::class.java)
//            intent.putExtra(
//                Constant.INTENT_EXTRA.IS_TICKET_BOOKING,
//                true
//            )
//            getResultLogin.launch(intent)
//        }else{
        if (tempbranchAdapter != null) {
            //  Utils.print("onItemClickMain 3 = "+mLastClickTime)
            val service: Data.Child =
                tempbranchAdapter!!.mArrayList.get(position).susbParetntService

            var serviceBean = Service()
            serviceBean.branchID = service.branchID
            serviceBean.name = service.name
            serviceBean.id = service.id
            serviceBean.appointmentTime = service.appointmentTime
            serviceBean.appointmentPrefix = service.appointmentPrefix
            serviceBean.englishServiceName = service.englishName
            serviceBean.description = service.description
            serviceBean.servingTime = service.servingTime
            serviceBean.maxRebookTime = service.maxRebookTime
            serviceBean.lang = service.lang
            serviceBean.prefix = service.prefix


                if  (!service.form!!.id.isNullOrEmpty()) {
                    serviceBean.form = service.form
                    clickOnButton = false
                    val intent = Intent(this, DynamicGenerateFormActivity::class.java)
                    intent.putExtra(Constant.INTENT_EXTRA.SERVICE_BEAN, serviceBean)
                    intent.putExtra(Constant.INTENT_EXTRA.MAIN_RESPONSE, mainResponse)
                    intent.putExtra(Constant.INTENT_EXTRA.BRANCH_BEAN, branchBean)
                    intent.putExtra(Constant.INTENT_EXTRA.MERCHANT_ID, merchantID)
                    intent.putExtra(Constant.INTENT_EXTRA.BANK_TIME_ARRAY, bankTimeArray)
                    intent.putExtra(Constant.INTENT_EXTRA.FROM_NEAR_BY, fromNearBy)
                    intent.putExtra(Constant.INTENT_EXTRA.SELECTED_BRANCH_DISTANCE,
                        nearByCurrentBranchDistance)
                    if (nearByBranchList.size > 0) {
                        intent.putExtra(Constant.INTENT_EXTRA.NEAR_BY_BRANCH_LIST, nearByBranchList)
                        intent.putExtra(Constant.INTENT_EXTRA.SELECTED_BRANCH_MINUTES,
                            nearByCurrentBranchMinutes)
                    }
//                intent.putExtra(Constant.INTENT_EXTRA.BRANCH_ID, branchID)
//                intent.putExtra(Constant.INTENT_EXTRA.MERCHANT_LOGO_URL, mainResponse!!.logoURL)
//                intent.putExtra(Constant.INTENT_EXTRA.MERCHANT_NAME, mainResponse!!.merchantName)
                    startActivity(intent)
                } else {
            gotoNext(serviceBean)
                }


//            }
        }


    }


    /**
    //     * Change StatusBar Color
    //     */
//    fun setStatusBarColor(ColorRes: Int = R.color.view_color_one) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            var colorStatusBar = ColorRes
//            getWindow().setStatusBarColor(
//                ContextCompat.getColor(
//                    getApplicationContext(),
//                    colorStatusBar
//                )
//            );
//        }
//    }
//
//    fun showLoading() {
//        binding.llProgressBar.visibility = View.VISIBLE
//    }
//
//    fun hideLoading() {
//        binding.llProgressBar.visibility = View.GONE
//    }
//
//    fun showErrorMsg(message: String) {
//        if (!TextUtils.isEmpty(message) && !message.equals(resources.getString(R.string.in_error)))
//            showToast(this, message, true)
//    }

//    private fun showToast(ct: Context, message: String, isError: Boolean) {
//        try {
//            val inflater = LayoutInflater.from(ct)
//            val layout = inflater.inflate(
//                R.layout.layout_toast_message,
//                null
//            )
//            val textV: TextView = layout.findViewById(R.id.lbl_toast) as TextView
//
//            /*if (Utils.isArLang())
//            textV.setTextDirection(View.TEXT_DIRECTION_ANY_RTL);*/
//
//            if (isError) {
//                textV.setTextColor(ct.resources.getColor(R.color.font_red))
//                textV.setBackgroundResource(R.drawable.toast_error_background)
//            } else {
//                textV.setTextColor(ct.resources.getColor(R.color.font_green))
//                textV.setBackgroundResource(R.drawable.toast_success_background)
//            }
//            if (message.contains("Chain") || message.contains("Socket") || message.contains("Server error")) {
//                textV.text = ct.resources.getString(R.string.please_try_again)
//            } else {
//                textV.text = message
//            }
//
//            val toast = Toast(ct)
//            toast.duration = Toast.LENGTH_SHORT
//            toast.view = layout
//
//            var valueInPixels =
//                ct.getResources().getDimension(com.intuit.sdp.R.dimen._100sdp).toFloat()
//            var a = dpToPxMain(valueInPixels).toInt()
//            toast.setGravity(Gravity.TOP, 0, 0);
//            toast.show()
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//
//    }

    override fun onClick(view: View) {
        when (view.id) {

            R.id.ivBranchTiming -> {
                openHoursBottomsheetFragment = OpenHoursBottomsheetFragment()

                val bundle = Bundle()
                bundle.putSerializable(Constant.INTENT_EXTRA.MAIN_RESPONSE, mainResponse)
                openHoursBottomsheetFragment!!.arguments = bundle

                openHoursBottomsheetFragment!!.show(
                    supportFragmentManager,
                    openHoursBottomsheetFragment!!.tag
                )
            }

            R.id.tvBranchTiming -> {
                openHoursBottomsheetFragment = OpenHoursBottomsheetFragment()

                val bundle = Bundle()
                bundle.putSerializable(Constant.INTENT_EXTRA.MAIN_RESPONSE, mainResponse)
                openHoursBottomsheetFragment!!.arguments = bundle

                openHoursBottomsheetFragment!!.show(
                    supportFragmentManager,
                    openHoursBottomsheetFragment!!.tag
                )
            }

            R.id.ivClose -> {
                onBackPressedSupport()
            }


        }
    }
}