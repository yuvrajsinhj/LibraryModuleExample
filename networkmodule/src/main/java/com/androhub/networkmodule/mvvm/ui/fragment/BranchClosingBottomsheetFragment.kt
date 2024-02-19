package com.androhub.networkmodule.mvvm.ui.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.androhub.networkmodule.PrefConst
import com.androhub.networkmodule.R
import com.androhub.networkmodule.databinding.BranchClosingBottomsheetFragmentBinding
import com.androhub.networkmodule.mvvm.model.request.AddTicketRequestBean
import com.androhub.networkmodule.mvvm.model.response.CheckBranchBean
import com.androhub.networkmodule.mvvm.model.response.getbranchdetails.BranchTime
import com.androhub.networkmodule.mvvm.model.response.getbranchdetails.Data
import com.androhub.networkmodule.mvvm.model.response.getbranchdetails.Service

import com.androhub.networkmodule.utils.Constant
import com.androhub.networkmodule.mvvm.viewmodel.CustomerBookingTicketViewModel
import java.util.*

class BranchClosingBottomsheetFragment : BaseFragmentBottomSheet(), View.OnClickListener {

    lateinit var binding: BranchClosingBottomsheetFragmentBinding
    lateinit var viewModel: CustomerBookingTicketViewModel
    var serviceData: Service? = null
    var fromNearBy: Boolean = false
    var waitingTime: Int? = null
    var breakTime: Int? = null
    private var bankTimeArray = ArrayList<BranchTime>()
    var nearByCurrentBranchDistance: Double = -1.0
    var isOpenBranch: Boolean? = false
    var inBreak: Boolean? = false
    var inGraceTime: Boolean? = false
    var mainResponse: Data.TodayTime? = null
    var commaSeperatedString = ""
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        binding = DataBindingUtil.inflate(inflater,
            R.layout.branch_closing_bottomsheet_fragment,
            container,
            false)
        viewModel = ViewModelProvider(this).get(CustomerBookingTicketViewModel::class.java)
        if (arguments != null) {
            try {
//                if (requireArguments().containsKey(Constant.INTENT_EXTRA.MESSAGE_RESPONSE)) {
//                    val duration =
//                        (requireArguments().getString(Constant.INTENT_EXTRA.MESSAGE_RESPONSE))
//                    binding.textDescription.text = duration.toString()
//                }
                if (requireArguments().containsKey(Constant.INTENT_EXTRA.SELECTED_BRANCH_DISTANCE))
                    nearByCurrentBranchDistance =
                        (requireArguments().getDouble(Constant.INTENT_EXTRA.SELECTED_BRANCH_DISTANCE))
                fromNearBy = requireArguments().getBoolean(Constant.INTENT_EXTRA.FROM_NEAR_BY)
                breakTime = requireArguments().getInt(Constant.INTENT_EXTRA.BREAK_TIME)
                waitingTime = requireArguments().getInt(Constant.INTENT_EXTRA.WAITING_TIME)
                serviceData =
                    requireArguments().getSerializable(Constant.INTENT_EXTRA.SERVICE_BEAN) as Service?
                bankTimeArray =
                    requireArguments().getSerializable(Constant.INTENT_EXTRA.TIME_BEAN) as ArrayList<BranchTime>

//                        if (requireArguments().containsKey(Constant.INTENT_EXTRA.DURATION)) {
//                    val duration = (requireArguments().getLong(Constant.INTENT_EXTRA.DURATION))
                binding.textDescription.text =
                    resources.getString(R.string.close_alert)
//                        }


            } catch (e: Exception) {
            }
        }

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initUi()
        setObserver()
    }

    override fun getLayoutId(): View {
        return binding.root
    }

    override fun initUi() {

        binding.ivClose.setOnClickListener(this)
        binding.btnClose.setOnClickListener(this)
        binding.txtProcced.setOnClickListener(this)


    }

    override fun getContext(): Context? {
        return activity
    }
    // variable to track event time
    private var mLastClickTime: Long = 0
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnClose -> {
                dismiss()
            }
            R.id.ivClose -> {
                dismiss()
            }
            R.id.txtProcced -> {
//                callApi(2)
                if (SystemClock.elapsedRealtime() - mLastClickTime < 3000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                callApi(2)
            }
        }

    }

    private fun callApi(tag: Int = 1, showLoading: Boolean = true) {
        if (!isConnected(context, true))
            return
        if (tag == 1) {
            var beanToSend = AddTicketRequestBean()
            beanToSend.deviceID = getDeviceID()
            beanToSend.lat = getStoredUserLatitude(requireActivity())
            beanToSend.long = getStoredUserLongitude(requireActivity())
            beanToSend.distanceFromBranch = "" + nearByCurrentBranchDistance

            beanToSend.prefix = serviceData!!.prefix!!
            beanToSend.branchId = serviceData!!.branchID!!
            beanToSend.branchName = serviceData!!.branchName!!
            beanToSend.serviceId = serviceData!!.id!!
            beanToSend.serviceName = serviceData!!.name!!
            beanToSend.merchantId = serviceData!!.merchantID
            beanToSend.merchantName = serviceData!!.merchantName!!
            beanToSend.merchantLogo = serviceData!!.logoURL!!
            beanToSend.approxTime = serviceData!!.servingTime!!
            beanToSend.maxRebookTime = serviceData!!.maxRebookTime!!
            beanToSend.branchOpenTime = commaSeperatedString
            beanToSend.queueTime =
                if (serviceData!!.isFasterSelected && serviceData!!.nearByFastBranch != null)
                    serviceData!!.nearByFastBranch!!.fasterBranchWaitTime
                else serviceData!!.currentBranchWaitTime!!
            beanToSend.customerId = prefManager.getString(PrefConst.PREF_USER_ID)






            if (inBreak!!) {
                beanToSend.branchStatus = "break"
            } else if (inGraceTime!!) {
                beanToSend.branchStatus = "grace"
            } else if (isOpenBranch!!) {
                if (inBreak!!) {
                    beanToSend.branchStatus = "break"
                } else {
                    beanToSend.branchStatus = "open"
                }
            }


            var fname = prefManager.getString(PrefConst.PREF_FIRST_NAME)
            var lname = prefManager.getString(PrefConst.PREF_LAST_NAME)
            var cName = getFullName(fname, lname)
            beanToSend.customerName = cName
            beanToSend.phone = prefManager.getString(PrefConst.PREF_PHONE)

            /**
             * If segment available sent segment and prefix with service name first chart
             */
            var segment = prefManager.getString(PrefConst.USER_CURRENT_SEGMENT)
            var segmentPrifix = prefManager.getString(PrefConst.USER_CURRENT_SEGMENT_PREFIX)

            //print segment
            // Utils.print("USER_CURRENT_SEGMENT Before Add = $segment $segmentPrifix")
            // var serviceNamePrefix= if (serviceData!=null && !TextUtils.isEmpty(serviceData!!.englishServiceName))serviceData!!.englishServiceName!!.first() else if (serviceData!=null && !TextUtils.isEmpty(serviceData!!.name))serviceData!!.name!!.first() else ""
            if (!TextUtils.isEmpty(segment)) {

                beanToSend.segment = segment
                beanToSend.prefix = (segmentPrifix + beanToSend.prefix).toUpperCase(Locale.ROOT)
            }
            //Utils.print("USER_CURRENT_SEGMENT Before Add 2 = ${beanToSend.segment} ${beanToSend.prefix}")

            /*Utils.print("check- prefix="+serviceData!!.prefix!!)
            Utils.print("check- branchID="+serviceData!!.branchID!!)
            Utils.print("check- branchName="+serviceData!!.branchName!!)
            Utils.print("check- id="+serviceData!!.id!!)
            Utils.print("check- name="+serviceData!!.name!!)
            Utils.print("check- approxTime="+beanToSend.approxTime)*/
            showLoading()
            //


            var timeZone =
                if (!TextUtils.isEmpty(prefManager.getString(PrefConst.BRANCH_TIME_ZONE))) prefManager.getString(
                    PrefConst.BRANCH_TIME_ZONE
                ) else TimeZone.getDefault().getID()

            beanToSend.timeZone = timeZone
            if (mainResponse?.shifts != null) {
                beanToSend.shift = mainResponse?.shifts
            } else {
                beanToSend.shift = Constant.TEMP_SHIFT
            }
            beanToSend.customerEmail =  prefManager.getString(PrefConst.PREF_EMAIL)

            viewModel.requestAddTicketApiCall(requireContext(), beanToSend)
        } else if (tag == 2) {
            val hashMap = HashMap<String, String>()

            hashMap.put("branchID", serviceData!!.branchID!!)

            showLoading()
            viewModel.requestCheckBranchApiCall(requireContext(), hashMap)
        } else if (tag == 3) {
            val hashMap = HashMap<String, Any>()
            hashMap.put("branchID", serviceData!!.branchID!!)
            hashMap.put("isFromSDK", true)
            showLoading()
            viewModel.requestGetBranchDetailsByBranchIdApiCall(requireContext()!!, hashMap)
        }

    }

    private fun setObserver() {

        viewModel.responseAddTicketApiCall()
            .observe(requireActivity(), androidx.lifecycle.Observer {
                hideLoading()
                if (it != null) {
                    if (it.status == Constant.STATUS.SUCCESS) {
                        //clear flag for segment

                        //print segment
                        //  var segment = prefManager.getString(PrefConst.USER_CURRENT_SEGMENT)
                        // var segmentPrifix = prefManager.getString(PrefConst.USER_CURRENT_SEGMENT_PREFIX)
                        //    Utils.print("USER_CURRENT_SEGMENT $segment $segmentPrifix")

                        prefManager.setString(PrefConst.USER_CURRENT_SEGMENT, "")
                        prefManager.setString(PrefConst.USER_CURRENT_SEGMENT_PREFIX, "")

                        var data = it.data
                        if (data != null) {
                            data.logoURL = serviceData!!.logoURL!!
                            data.servingTime = serviceData!!.servingTime!!
                            startActivity(
                                Intent(
                                    activity,
                                    CustomerConfirmationTicketActivity::class.java
                                )
                                    .putExtra(Constant.INTENT_EXTRA.IN_GRACETIME, inGraceTime)
                                    .putExtra(Constant.INTENT_EXTRA.IN_BREAK, inBreak)
                                    .putExtra(Constant.INTENT_EXTRA.TICKET_BEAN, data)
                                    .putExtra(Constant.INTENT_EXTRA.TICKET_ADDED, true)
                            )
                            requireActivity().finishAffinity()

                        } else
                            showErrorMsg(requireContext().resources.getString(R.string.no_results_were_found))
                    } else {


                        var message =
                            if (!TextUtils.isEmpty(it.message)) it.message!! else getString(R.string.queue_not_found)

                        showErrorMsg(message)
                        //  showDialogAsStatus(response.status, response.message)
                    }
                } else {
                    showSuccessMsg(resources.getString(R.string.in_error))
                }
            })
        viewModel.responseCheckBranchApiCall().observe(this, androidx.lifecycle.Observer {
            if (it != null) {
                hideLoading()
                if (it.status == Constant.STATUS.SUCCESS) {
                    var bean: CheckBranchBean = it
                    isOpenBranch = bean.data.isOpenBranch
                    inBreak = bean.data.inBreak
                    inGraceTime = bean.data.inGraceTime
                    if (isOpenBranch!! || inBreak!! || inGraceTime!!) {
                        callApi(1)
                    } else {
                        showErrorMsg(it.message)
                    }
                } else {
                    showErrorMsg(it.message)
                }
            }
        })


        viewModel.responseGetBranchByBranchIdApiCall().observe(this, androidx.lifecycle.Observer {

            if (it != null) {
                if (it.status == Constant.STATUS.SUCCESS) {
                    hideLoading()

                    mainResponse = it.data?.todayTime

                    commaSeperatedString =
                        it.data?.todayTime?.shifts?.joinToString(separator = ",") { it -> "${it.start},${it.end}" }
                            .toString()
//                    Log.e("commaSeperatedString","$commaSeperatedString")


                } else {

                    hideLoading()

                }
            } else {
                hideLoading()

                showErrorMsg(resources.getString(R.string.not_found_branch))


            }
        })
    }

}