package com.androhub.networkmodule.mvvm.ui.fragment


import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.androhub.networkmodule.PrefConst
import com.androhub.networkmodule.R
import com.androhub.networkmodule.databinding.BottomSheetConfirmBookTicketBinding
import com.androhub.networkmodule.local.Utility
import com.androhub.networkmodule.mvvm.model.request.AddTicketRequestBean
import com.androhub.networkmodule.mvvm.model.response.CheckBranchBean
import com.androhub.networkmodule.mvvm.model.response.getbranchdetails.BranchTime
import com.androhub.networkmodule.mvvm.model.response.getbranchdetails.Data
import com.androhub.networkmodule.mvvm.model.response.getbranchdetails.Service
import com.androhub.networkmodule.mvvm.model.response.getbranchdetails.ServiceForm
import com.androhub.networkmodule.mvvm.ui.BranchDetailActivity
import com.androhub.networkmodule.mvvm.viewmodel.CustomerBookingTicketViewModel
import com.androhub.networkmodule.utils.Constant
import com.androhub.networkmodule.utils.ImageDisplayUitls
import com.google.android.material.bottomsheet.BottomSheetBehavior
import java.util.ArrayList
import java.util.HashMap
import java.util.Locale
import java.util.TimeZone

class ConfirmBookingTicketBottomSheetFragment : BaseFragmentBottomSheet(),
    View.OnClickListener {
    lateinit var binding: BottomSheetConfirmBookTicketBinding
    var serviceData: Service? = null
    var fromNearBy: Boolean = false
    var waitingTime: Int? = null
    var breakTime: Int? = null
    private var bankTimeArray = ArrayList<BranchTime>()
    var nearByCurrentBranchDistance: Double = -1.0
    lateinit var viewModel: CustomerBookingTicketViewModel
    var isOpenBranch: Boolean? = false
    var inBreak: Boolean? = false
    var inGraceTime: Boolean? = false
    var mainResponse: Data.TodayTime? = null
    var commaSeperatedString = ""

    //    var formFilledDetail = ""
//    var formId = ""
    var formDetail: ServiceForm.FormId? = null


    companion object {
        var branchBreakBottomsheetFragment: BranchBreakBottomsheetFragment? = null
    }

    override fun onStart() {
        super.onStart()
        val behavior = BottomSheetBehavior.from(requireView().parent as View)
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.bottom_sheet_confirm_book_ticket,
            container,
            false
        )


        viewModel = ViewModelProvider(this).get(CustomerBookingTicketViewModel::class.java)
//        viewModel = ViewModelProvider(this).get(SettingFragmentViewModel::class.java)

        if (arguments != null) {

            if (requireArguments().containsKey(Constant.INTENT_EXTRA.SELECTED_BRANCH_DISTANCE))
                nearByCurrentBranchDistance =
                    (requireArguments().getDouble(Constant.INTENT_EXTRA.SELECTED_BRANCH_DISTANCE))
            fromNearBy = requireArguments().getBoolean(Constant.INTENT_EXTRA.FROM_NEAR_BY)
            breakTime = requireArguments().getInt(Constant.INTENT_EXTRA.BREAK_TIME)
            waitingTime = requireArguments().getInt(Constant.INTENT_EXTRA.WAITING_TIME)
            serviceData =
                requireArguments().getSerializable(Constant.INTENT_EXTRA.SERVICE_BEAN) as Service?
            Log.e("merchantId33", serviceData!!.merchantID)
            bankTimeArray =
                requireArguments().getSerializable(Constant.INTENT_EXTRA.TIME_BEAN) as ArrayList<BranchTime>
            if (requireArguments().containsKey(Constant.INTENT_EXTRA.FORM_FILLED_DETAILS_FORM_ID))
                formDetail =
                    ((requireArguments().getSerializable(Constant.INTENT_EXTRA.FORM_FILLED_DETAILS_FORM_ID) as ServiceForm.FormId?))

//                formFilledDetail = ((requireArguments().getString(Constant.INTENT_EXTRA.FORM_FILLED_DETAILS).toString()))
//                formId = ((requireArguments().getString(Constant.INTENT_EXTRA.FORM_FILLED_DETAILS_FORM_ID).toString()))

            //Show instruction for branch
            binding.include.llInstruction.visibility = View.GONE
            serviceData?.let {
                if (!TextUtils.isEmpty(it.description)) {
                    binding.include.llInstruction.visibility = View.VISIBLE
//                    binding.include.llInstruction.tvInstruction.text = it.description

                }
            }
            //set dynamic business name
            if (fromNearBy && !TextUtils.isEmpty(serviceData!!.merchantName)) {
                //   binding.tvInfoMsg.setText(resources.getString(R.string.confirm_book_ticket_info, serviceData!!.name,serviceData!!.branchName))
                binding.tvInfoMsg.setText(
                    Utility.fromHtml(
                        resources.getString(
                            R.string.confirm_book_ticket_info,
                            serviceData!!.name,
                            serviceData!!.branchName
                        )
                    )
                )
                binding.imgClock.visibility = View.VISIBLE
                binding.imgGif.visibility = View.GONE
            } else {
                ImageDisplayUitls.displayImageWithDrawable(
                    R.drawable.far_away_gif,
                    requireActivity(),
                    binding.imgGif
                )
                var messageOther = resources.getString(R.string.book_ticket_info_other)
                binding.tvInfoMsg.setText(messageOther)
                binding.imgClock.visibility = View.GONE
                binding.imgGif.visibility = View.VISIBLE
            }

            callApi(3)
            initUi()
            setObserver()
        }


        return binding.root
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDismiss(dialog: DialogInterface) {

        super.onDismiss(dialog)

    }


    override fun initUi() {
        binding.imgClose.setOnClickListener(this)
        binding.txtConfirm.setOnClickListener(this)
        binding.txtBack.setOnClickListener(this)


    }

    override fun getLayoutId(): View {
        return binding.root
    }


    override fun getContext(): Context? {
        return activity
    }

    // variable to track event time
    private var mLastClickTime: Long = 0
    override fun onClick(view: View) {
        when (view.id) {
            R.id.txtBack -> {
                if (BranchDetailActivity.customerBookingTicketAppointBottomSheetFragment != null)
                    BranchDetailActivity.customerBookingTicketAppointBottomSheetFragment!!.dismiss()

                dismiss()

            }
            R.id.imgClose -> {
                dismiss()
            }

            R.id.txtConfirm -> {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 3000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();



//                if (checkIsLoginUser(requireActivity())) {
                    if (breakTime!! > 0) {
                        showBreakTimeDialog()
                    } else {
                        showLoading()
                        callApi(2)
                    }

//                }

            }

        }
    }


    private fun showBreakTimeDialog() {


        var message = resources.getString(R.string.break_alert,
            Utility.convertToArabic(waitingTime.toString()))
        branchBreakBottomsheetFragment = BranchBreakBottomsheetFragment()

        val bundle = Bundle()

        bundle.putSerializable(Constant.INTENT_EXTRA.FROM_NEAR_BY, fromNearBy)
        bundle.putSerializable(Constant.INTENT_EXTRA.BREAK_TIME, breakTime)
        bundle.putSerializable(Constant.INTENT_EXTRA.WAITING_TIME, waitingTime)
        bundle.putSerializable(Constant.INTENT_EXTRA.SERVICE_BEAN, serviceData)
        bundle.putSerializable(Constant.INTENT_EXTRA.TIME_BEAN, bankTimeArray)
        bundle.putSerializable(Constant.INTENT_EXTRA.SELECTED_BRANCH_DISTANCE,
            nearByCurrentBranchDistance)
        if (!TextUtils.isEmpty(serviceData!!.oldBranchIDTemp)) {
            serviceData!!.branchID = serviceData!!.oldBranchIDTemp
            serviceData!!.branchName = serviceData!!.oldBranchNameTemp
        }




        bundle.putString(Constant.INTENT_EXTRA.MESSAGE_RESPONSE, message)
        branchBreakBottomsheetFragment!!.arguments = bundle

        branchBreakBottomsheetFragment!!.show(childFragmentManager,
            branchBreakBottomsheetFragment!!.tag)



    }

    fun checkIsLoginUser(activity: Activity, showError: Boolean = false): Boolean {
        var uID = prefManager.getString(PrefConst.PREF_USER_ID)
        var isLogin = prefManager.getBoolean(PrefConst.PREF_IS_LOGIN)

        if (TextUtils.isEmpty(uID) || !isLogin) {
            /*  if (showError){
                  Utility.getConfirmationDialog(activity,
                      activity.getString(R.string.login_title),
                      activity.getString(R.string.login_msg),
                      activity.getString(R.string.log_in),
                      activity.getString(R.string.cancel),
                      DialogInterface.OnClickListener { dialog, which ->
                          activity.startActivityForResult(Intent(activity, LoginActivity::class.java).putExtra(Constant.INTENT_EXTRA.IS_TICKET_BOOKING,true),Constant.INTENT_100)
                          //  activity.finishAffinity()

                          dialog.dismiss()
                      },
                      DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() }).show()}*/
//            activity.startActivityForResult(
//                Intent(activity, LoginActivity::class.java).putExtra(
//                    Constant.INTENT_EXTRA.IS_TICKET_BOOKING,
//                    true
//                ), Constant.INTENT_100
//            )
            return false
        } else
            return true
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
            beanToSend.branchName = serviceData!!.branchName
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
            beanToSend.customerEmail = prefManager.getString(PrefConst.PREF_EMAIL)
            if (formDetail!=null){
                beanToSend.formDetail = formDetail
                beanToSend.formId = formDetail!!.id
            }

            viewModel.requestAddTicketApiCall(requireContext(), beanToSend)
        } else if (tag == 2) {
            showLoading()
            val hashMap = HashMap<String, String>()

            hashMap.put("branchID", serviceData!!.branchID!!)


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

                if (it.status == Constant.STATUS.SUCCESS) {
                    var bean: CheckBranchBean = it
                    isOpenBranch = bean.data.isOpenBranch
                    inBreak = bean.data.inBreak
                    inGraceTime = bean.data.inGraceTime
                    if (isOpenBranch!! || inBreak!! || inGraceTime!!) {
                        callApi(1)
                    } else {
                        hideLoading()
                        showErrorMsg(it.message)
                    }
                } else {
                    hideLoading()
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
    override fun showLoading() {
        binding.llProgressBar.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        binding.llProgressBar.visibility = View.GONE
    }


}