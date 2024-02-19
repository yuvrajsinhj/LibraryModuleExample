package com.androhub.networkmodule.mvvm.ui.fragment

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.androhub.networkmodule.PrefConst
import com.androhub.networkmodule.R
import com.androhub.networkmodule.databinding.BottomSheetCustBookTicketBinding
import com.androhub.networkmodule.local.Utility
import com.androhub.networkmodule.mvvm.model.request.AddTicketRequestBean
import com.androhub.networkmodule.mvvm.model.request.TicketCountRequestBean
import com.androhub.networkmodule.mvvm.model.response.CheckBranchBean
import com.androhub.networkmodule.mvvm.model.response.GetFasterBranchListResponseBean
import com.androhub.networkmodule.mvvm.model.response.getbranchbymerchant.Branch
import com.androhub.networkmodule.mvvm.model.response.getbranchdetails.BranchTime
import com.androhub.networkmodule.mvvm.model.response.getbranchdetails.Data
import com.androhub.networkmodule.mvvm.model.response.getbranchdetails.Service
import com.androhub.networkmodule.mvvm.model.response.getbranchdetails.ServiceForm

import com.androhub.networkmodule.utils.Constant
import com.androhub.networkmodule.utils.Utils

import com.androhub.networkmodule.mvvm.ui.BranchDetailActivity
import com.androhub.networkmodule.mvvm.viewmodel.CustomerBookingTicketViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.Duration
import java.text.SimpleDateFormat
import java.util.*


class CustomerBookingTicketAppointBottomSheetFragment : BaseFragmentBottomSheet(),
    View.OnClickListener {
    lateinit var binding: BottomSheetCustBookTicketBinding
    var serviceData: Service? = null
    private var bankTimeArray = ArrayList<BranchTime>()
    var nearByBranchList = ArrayList<Branch>()
    var nearByCurrentBranchDistance: Double = -1.0
    var nearByCurrentBranchMinutes: Double = -1.0
    var fasterBranchList = ArrayList<GetFasterBranchListResponseBean.Data?>()
    lateinit var viewModel: CustomerBookingTicketViewModel
    var fromNearBy: Boolean = false
    var isOpenBranch: Boolean? = false
    var inBreak: Boolean? = false
    var inGraceTime: Boolean? = false
    var mainResponse: Data.TodayTime? = null
    var commaSeperatedString = ""
    var waitingTime: Int? = null
    var breakTime: Int? = null

    var formDetail: ServiceForm.FormId?=null

    override fun onStart() {
        super.onStart()
        val behavior = BottomSheetBehavior.from(requireView().parent as View)
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    companion object {
        var branchClosingBottomsheetFragment:BranchClosingBottomsheetFragment?=null
        var branchBreakBottomsheetFragment: BranchBreakBottomsheetFragment?=null
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.bottom_sheet_cust_book_ticket,
            container,
            false
        )
        viewModel = ViewModelProvider(this).get(CustomerBookingTicketViewModel::class.java)

        if (arguments != null) {


            try {

                if (requireArguments().containsKey(Constant.INTENT_EXTRA.FORM_FILLED_DETAILS_FORM_ID))
                 formDetail= ((requireArguments().getSerializable(Constant.INTENT_EXTRA.FORM_FILLED_DETAILS_FORM_ID) as ServiceForm.FormId?))

                if (requireArguments().containsKey(Constant.INTENT_EXTRA.NEAR_BY_BRANCH_LIST))
                    nearByBranchList =
                        ((requireArguments().getSerializable(Constant.INTENT_EXTRA.NEAR_BY_BRANCH_LIST) as ArrayList<Branch>?)!!)

                if (requireArguments().containsKey(Constant.INTENT_EXTRA.SELECTED_BRANCH_DISTANCE))
                    nearByCurrentBranchDistance =
                        (requireArguments().getDouble(Constant.INTENT_EXTRA.SELECTED_BRANCH_DISTANCE))

                if (requireArguments().containsKey(Constant.INTENT_EXTRA.SELECTED_BRANCH_MINUTES))
                    nearByCurrentBranchMinutes =
                        (requireArguments().getDouble(Constant.INTENT_EXTRA.SELECTED_BRANCH_MINUTES))

                Utils.print("nearByCurrentBranchDistance=" + nearByCurrentBranchDistance)
                Utils.print("nearByCurrentBranchMinutes=" + nearByCurrentBranchMinutes)
            } catch (e: Exception) {
                nearByBranchList = ArrayList()
                nearByCurrentBranchDistance = -1.0
                nearByCurrentBranchMinutes = -1.0
            }


            inBreak = requireArguments().getBoolean(Constant.INTENT_EXTRA.IN_BREAK)
            inGraceTime = requireArguments().getBoolean(Constant.INTENT_EXTRA.IN_GRACETIME)
            fromNearBy = requireArguments().getBoolean(Constant.INTENT_EXTRA.FROM_NEAR_BY)
            serviceData =
                requireArguments().getSerializable(Constant.INTENT_EXTRA.SERVICE_BEAN) as Service?
            Log.e("merchantId22",serviceData!!.merchantID)

            bankTimeArray =
                requireArguments().getSerializable(Constant.INTENT_EXTRA.TIME_BEAN) as ArrayList<BranchTime>

            if (serviceData != null && !TextUtils.isEmpty(serviceData!!.branchID)) {
                serviceData!!.oldBranchIDTemp = serviceData!!.branchID!!
                serviceData!!.oldBranchNameTemp = serviceData!!.branchName
            }

            binding.txtBack.visibility = if (fromNearBy) View.VISIBLE else View.GONE

            //Show instruction for branch
            binding.include.llInstruction.visibility = View.GONE
            /*serviceData?.let {
                if (!TextUtils.isEmpty(it.description) && fromNearBy)
                {
                    //hide back button while instruction available
                     binding.txtBack.visibility=View.GONE
                    //binding.include.llInstruction.visibility=View.VISIBLE
                   // binding.include.llInstruction.tvInstruction.text=it.description

                }
            }*/


            //set dynamic business name
            if (!TextUtils.isEmpty(serviceData!!.name)) {
                var htmlName = "<b><font color=#000000>${serviceData!!.name}</font></b>"
                var messageNearBy = resources.getString(R.string.book_ticket_info, htmlName)

                if (serviceData!!.isOpenBranch || inBreak!! || inGraceTime!!) {
                    binding.tvInfoMsg.setText(Utility.fromHtml(messageNearBy))
                }

            }
            binding.txtInstantTicket.visibility =
                if (serviceData!!.isOpenBranch || inBreak!! || inGraceTime!!) View.VISIBLE else View.GONE
            binding.tvClose.visibility =
                if (serviceData!!.isOpenBranch || inBreak!! || inGraceTime!!) View.GONE else View.VISIBLE

            /*if (binding.txtInstantTicket.isVisible) {
                binding.txtInstantTicket.visibility = if (fromNearBy) View.VISIBLE else View.GONE
            }*/
            callApi(5)
            initUi()
            setObserver()
            //callApi(2)
            showLoading()
            Handler().postDelayed({
                hideLoading()
                updateTime()
            }, 2000)

        }
        return binding.root
    }


    override fun onDismiss(dialog: DialogInterface) {

        super.onDismiss(dialog)
        Utils.print("details onDismiss")
        BranchDetailActivity.clickOnButton = false
        updater?.let { timerHandler.removeCallbacks(it) };

    }


    var updater: Runnable? = null
    val timerHandler = Handler()
    fun updateTime() {

        updater = Runnable {

            callApi(2, !binding.llPeopleData.isVisible)

            timerHandler.postDelayed(updater!!, Constant.REFRESH_TIME)
        }
        timerHandler.post(updater!!)
    }

    override fun initUi() {
        binding.txtBack.setOnClickListener(this)
        binding.imgClose.setOnClickListener(this)
        binding.txtInstantTicket.setOnClickListener(this)


    }

    override fun getLayoutId(): View {
        return binding.root
    }


    override fun getContext(): Context? {
        return activity
    }

    private var mLastClickTime: Long = 0
    override fun onClick(view: View) {
        when (view.id) {
            R.id.txtBack -> {

                dismiss()

            }
            R.id.imgClose -> {
                dismiss()
            }

            R.id.txtInstantTicket -> {

                if (SystemClock.elapsedRealtime() - mLastClickTime < 4000) {
                    return
                }

                val time = (DateTime().toString("kk:mm"))
                if (mainResponse != null) {
                    val hsiftEnd = mainResponse?.shifts?.last()
                    val formatDate = SimpleDateFormat("hh:mm")

                    val endTime: Date = formatDate.parse(hsiftEnd?.end)

                    val currentTime = DateTime()
                    val someDate = DateTime(endTime, DateTimeZone.getDefault())

                    val currentTime2 = formatDate.parse(time)
//                    Log.e("ttimes", "$currentTime ,, $someDate   -- $currentTime2--------${endTime.time}")

                    val duration = Duration(currentTime2.time, endTime.time)
//                    Log.e("ttimes", duration.standardMinutes.toString() + "<<< duration")



                    try {

                       if (duration.standardMinutes <= waitingTime!!.toLong()) {
                            showDialog(duration.standardMinutes)
//                            showErrorToast(requireContext(),resources.getString(R.string.cant_book_ticket))
                        }
                      else if (breakTime!! > 0 && TextUtils.isEmpty(serviceData!!.description)) {
                            showBreakTimeDialog()

                        }else {

                            mLastClickTime = SystemClock.elapsedRealtime();

//                            if (checkIsLoginUser(requireActivity())) {
                                if (binding.llPeopleData.isVisible && binding.pgbFaster.isGone) {
                                    Log.e("merchantId22", "--"+serviceData!!.description.toString()+"--"+fromNearBy+"--")
                                    if (fromNearBy && serviceData != null && TextUtils.isEmpty(
                                            serviceData!!.description)
                                    ) {
                                        callApi(4)
                                    } else
                                        gotoNextStep()

                                } else {
                                    Utils.makeMeShake(binding.txtInstantTicket)
                                }

//                            }
                        }
                    } catch (e: Exception) {
                        Log.e("Error", "${e.message}")
                    }


                } else {
//                    showErrorToast(requireContext(),"try again")
                }



            }

        }
    }

    private fun showDialog(duration: Long) {
        branchClosingBottomsheetFragment = BranchClosingBottomsheetFragment()


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
        bundle.putLong(Constant.INTENT_EXTRA.DURATION,duration)
        branchClosingBottomsheetFragment!!.arguments = bundle

        branchClosingBottomsheetFragment!!.show(childFragmentManager,
            branchClosingBottomsheetFragment!!.tag)

    }


    private fun showBreakTimeDialog() {
        var message = resources.getString(R.string.break_alert,Utility.convertToArabic(waitingTime.toString()))
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




        bundle.putString(Constant.INTENT_EXTRA.MESSAGE_RESPONSE,message)
        branchBreakBottomsheetFragment!!.arguments = bundle

        branchBreakBottomsheetFragment!!.show(childFragmentManager,
            branchBreakBottomsheetFragment!!.tag)

//        val dialog = Dialog(requireContext())
////        val dialog = Dialog(requireContext(), R.style.Theme_Dialog)
//
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//        dialog.setCancelable(false)
//        dialog.setContentView(R.layout.dialog_branch_breaktime)
//        val imgClose = dialog.findViewById(R.id.imgClose) as AppCompatImageView
//        val txtProcced = dialog.findViewById(R.id.txtProcced) as CustomTextView
//        val noBtn = dialog.findViewById(R.id.txtCncle) as CustomTextView
//        val tvInfoMsg = dialog.findViewById(R.id.tvInfoMsg) as CustomTextView
//        var totalTime = breakTime?.plus(waitingTime!!)
//        var message = resources.getString(R.string.break_alert,Utility.convertToArabic(waitingTime.toString()))
//        tvInfoMsg.text = message
//        imgClose.setOnClickListener {
//            dialog.dismiss()
//        }
//        noBtn.setOnClickListener {
//            dialog.dismiss()
//        }
//        txtProcced.setOnClickListener {
//
//            mLastClickTime = SystemClock.elapsedRealtime();
//
//            if (checkIsLoginUser(requireActivity())) {
//                if (binding.llPeopleData.isVisible && binding.pgbFaster.isGone) {
//
//                    if (fromNearBy && serviceData != null && TextUtils.isEmpty(serviceData!!.description)) {
//                        callApi(4)
//                    } else
//                        gotoNextStep()
//
//                } else {
//                    Utils.makeMeShake(binding.txtInstantTicket)
//                }
//                setAnalyticsEvent(requireActivity(), Constant.APP_ACTION.confirmTicketBooking)
//            }
//            dialog.dismiss()
//
//        }
//        dialog.show()
//        val window: Window = dialog.getWindow()!!
//        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
//        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
    }


    /*  private fun getBranchWaitingTime(branchID:String) :Double?{
          val findedElement = fasterBranchList?.find {
              it!!.branchId == branchID
          }

          if (findedElement!=null)
          {
              return findedElement.waitTime
          }
          else return 0.0
      }*/
    private fun calculateFasterFromDistanceAndTime() {
        showLoaderFaster(true)
        //go to faster branch popup by true
        //check one more condition here for wait time ad no of people in que


        var selectedBranchId = serviceData!!.branchID


        var selectedBranchWaitTime = serviceData!!.currentBranchWaitTime

        /*  val findedElement = fasterBranchList?.find {
              it!!.branchId == selectedBranchId
          }

          if (findedElement != null) {
  */
        var finalFastestList = java.util.ArrayList<Branch>()
        try {
            prefManager.clearLogs()

            for ((index, valueNearBy) in nearByBranchList!!.withIndex()) {
                val logsStr = "\n||" +
                        "Merchant=" + valueNearBy.merchantName + "\n" +
                        "Name=" + valueNearBy.name + "\n" +
                        "Distance=" + valueNearBy.distanceStr + "\n" +
                        "Time=" + valueNearBy.durationValueInMinutes + "\n|| "
                prefManager.saveLogs(logsStr)
                var currentId = valueNearBy!!.id
                if ((!currentId.equals(selectedBranchId)) && valueNearBy.distance != null && ((valueNearBy.distance!! <= nearByCurrentBranchDistance) || (valueNearBy.durationValueInMinutes <= selectedBranchWaitTime!!))) {


                    val findedElement = fasterBranchList?.find {
                        it!!.branchId == currentId
                    }
                    if (findedElement != null) { //alueNearBy.durationValueInMinutes  <= nearByCurrentBranchMinutes!!)
                        var waitTime = findedElement.waitTime
                        if (waitTime!! < selectedBranchWaitTime!! || (waitTime!! < nearByCurrentBranchMinutes!! && (valueNearBy.durationValueInMinutes < nearByCurrentBranchMinutes))) {
                            if (valueNearBy.distance == nearByCurrentBranchDistance && valueNearBy.durationValueInMinutes == nearByCurrentBranchMinutes && waitTime!! < selectedBranchWaitTime!!) {
                            } else {
                                finalFastestList.add(valueNearBy)
                                break
                            }
                        }
                    }
                }
            }

        } finally {
            if (finalFastestList.size > 0) {

                var fastBranchBean = finalFastestList.get(0)
                if (serviceData!!.branchID.equals(fastBranchBean.id)) {
                    //same branch so no need to open faster
                    gotoNextStep()
                } else {
                    serviceData!!.nearByFastBranch = fastBranchBean
                    gotoNextStep(true)
                }
            } else gotoNextStep()

        }

        /* } else {
             //not found selected id
             gotoNextStep()
         }*/

    }

    private fun isAgentAvailable(): Boolean {

        if (fasterBranchList.size > 0) {
            var selectedBranchId = serviceData!!.branchID


            val findedElementBean = fasterBranchList?.find {
                it!!.branchId == selectedBranchId
            }
            //If selected branch agent is not online than return nearest available branch.
            return (findedElementBean != null)
        }
        return false

    }

    private fun calculateFasterFromTime() {
        showLoaderFaster(true)

        ///faster branch array
        var finalFastestList = java.util.ArrayList<Branch>()
        var isAgentAvailable: Boolean = false
        var selectedBranchId = serviceData!!.branchID
        var selectedBranchWaitTime = serviceData!!.currentBranchWaitTime
        try {

            /* let isAgentAvailable = arrServeBraches?.contains(where: { (result) -> Bool in
                     return selectedbranchID == (result["branchId"] as? String)
             })*/

            //if selected branch id in not in array then agent is not available for selected branch
            val findedElementBean = fasterBranchList?.find {
                it!!.branchId == selectedBranchId
            }
            //If selected branch agent is not online than return nearest available branch.
            isAgentAvailable = (findedElementBean != null)


            //make data for total time = duration(in minutes )+wait time (from faste branch api)
            for ((index, valueNearBy) in nearByBranchList!!.withIndex()) {
                val findedElement = fasterBranchList?.find {
                    it!!.branchId == valueNearBy.id
                }
                if (findedElement != null) {
                    var total = valueNearBy.durationValueInMinutes + findedElement.waitTime!!
                    valueNearBy.totalTimeInMinutes = total
                    valueNearBy.fasterBranchWaitTime = findedElement.waitTime!!
                }
            }
        } finally {
            //for loop for sort data with time = duration(in minutes )+wait time (from faste branch api)
            var tempNearByListSortedbyTime = java.util.ArrayList<Branch>()
            tempNearByListSortedbyTime.addAll(nearByBranchList)
            tempNearByListSortedbyTime.sortBy { it.totalTimeInMinutes }

            //now calcu;ate faster branch on total time
            for ((index, valueNearBy) in tempNearByListSortedbyTime!!.withIndex()) {
                if (!valueNearBy.id.equals(selectedBranchId) && valueNearBy.totalTimeInMinutes >= 0 && selectedBranchWaitTime!! >= 0) {
                    var currentBranchtime = valueNearBy.totalTimeInMinutes
                    var selectedBranchtime = selectedBranchWaitTime!! + nearByCurrentBranchMinutes
                    if (!isAgentAvailable || (currentBranchtime < selectedBranchtime)) {
                        Utils.print("faster currentBranchtime=" + currentBranchtime)
                        Utils.print("faster selectedBranchtime=" + selectedBranchtime)

                        finalFastestList.add(valueNearBy)
                        break
                    }
                }
            }
            if (finalFastestList.size > 0) {

                var fastBranchBean = finalFastestList.get(0)
                if (serviceData!!.branchID.equals(fastBranchBean.id)) {
                    //same branch so no need to open faster
                    gotoNextStep()
                } else {
                    // open faster
                    serviceData!!.nearByFastBranch = fastBranchBean
                    gotoNextStep(true)
                }
            } else gotoNextStep()
        }
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
        if (tag == 2) {
            if (!serviceData!!.isOpenBranch && !inBreak!! && !inGraceTime!!) {
//            if (!serviceData!!.isOpenBranch) {
                updater?.let { timerHandler.removeCallbacks(it) };
                showLoaderPeopleData(false)
                return
            }
            val hashMap = TicketCountRequestBean()
            var timeZone =
                if (!TextUtils.isEmpty(prefManager.getString(PrefConst.BRANCH_TIME_ZONE))) prefManager.getString(
                    PrefConst.BRANCH_TIME_ZONE
                ) else TimeZone.getDefault().getID()

            hashMap.timeZone = timeZone
            if (mainResponse?.shifts != null) {
//                Constant.TEMP_SHIFT = mainResponse?.shifts!!
                hashMap.shift = mainResponse?.shifts
            }
            hashMap.branchId = serviceData!!.branchID!!
            hashMap.serviceId = serviceData!!.id!!

            Log.e("HAaaaaa",
                "${hashMap.timeZone}---${hashMap.shift?.size}--${hashMap.branchId}----${hashMap.serviceId}")
            viewModel.requestGetTicketCountByBranchIdApiCall(mContext!!, hashMap)



            showLoaderPeopleData(showLoading)
            // showLoaderFaster(true)
        }
        else if (tag == 4) {
            val hashMap = HashMap<String, String>()
            hashMap.put("branchID", serviceData!!.branchID!!)
            showLoading()
            viewModel.requestCheckBranchApiCall(requireContext(), hashMap)
        } else if (tag == 5) {

            val hashMap = HashMap<String, Any>()
            hashMap.put("branchID", serviceData!!.branchID!!)
            hashMap.put("isFromSDK", true)
            showLoading()
            viewModel.requestGetBranchDetailsByBranchIdApiCall(requireContext(), hashMap)
        }
        else {


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
            beanToSend.merchantId = serviceData!!.merchantID!!
            beanToSend.merchantName = serviceData!!.merchantName!!
            beanToSend.merchantLogo = serviceData!!.logoURL!!
            beanToSend.approxTime = serviceData!!.servingTime!!
            beanToSend.branchOpenTime = commaSeperatedString
            beanToSend.maxRebookTime = serviceData!!.maxRebookTime!!
            beanToSend.queueTime =
                if (serviceData!!.isFasterSelected && serviceData!!.nearByFastBranch != null) serviceData!!.nearByFastBranch!!.fasterBranchWaitTime!! else serviceData!!.currentBranchWaitTime!!
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
            //var serviceNamePrefix= if (serviceData!=null && !TextUtils.isEmpty(serviceData!!.englishServiceName))serviceData!!.englishServiceName!!.first() else if (serviceData!=null && !TextUtils.isEmpty(serviceData!!.name))serviceData!!.name!!.first() else ""
            if (!TextUtils.isEmpty(segment)) {
                beanToSend.segment = segment
                beanToSend.prefix = (segmentPrifix + beanToSend.prefix).toUpperCase(Locale.ROOT)
            }

            /*Utils.print("check- prefix="+serviceData!!.prefix!!)
            Utils.print("check- branchID="+serviceData!!.branchID!!)
            Utils.print("check- branchName="+serviceData!!.branchName!!)
            Utils.print("check- id="+serviceData!!.id!!)
            Utils.print("check- name="+serviceData!!.name!!)
            Utils.print("check- approxTime="+beanToSend.approxTime)*/


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
//            var formDetail:qqqqqqq.FormDetail = Gson().fromJson(formFilledDetail, qqqqqqq.FormDetail::class.java)
//            beanToSend.formDetail = formFilledDetail

            if (formDetail!=null){
                beanToSend.formDetail = formDetail
                beanToSend.formId = formDetail!!.id
            }

            viewModel.requestAddTicketApiCall(requireContext(), beanToSend)
        }


    }


    private fun showLoaderPeopleData(showLoading: Boolean) {
        showLoaderFaster(showLoading)
        if (showLoading) {
            binding.progressLoadmorePeople.visibility = View.VISIBLE
            binding.llPeopleData.visibility = View.GONE
        } else {
            binding.progressLoadmorePeople.visibility = View.GONE


        }
    }

    private fun showLoaderFaster(showLoading: Boolean) {
        if (showLoading) {
            binding.pgbFaster.visibility = View.VISIBLE

        } else {
            binding.pgbFaster.visibility = View.GONE


        }
    }

    private fun setObserver() {


        viewModel.responseGetBranchByBranchIdApiCall().observe(this, androidx.lifecycle.Observer {

            if (it != null) {
                if (it.status == Constant.STATUS.SUCCESS) {
                    hideLoading()

                    mainResponse = it.data?.todayTime

                    commaSeperatedString =
                        it.data?.todayTime?.shifts?.joinToString(separator = ",") { it -> "${it.start},${it.end}" }
                            .toString()


                } else {

                    hideLoading()

                }
            } else {
                hideLoading()

                showErrorMsg(resources.getString(R.string.not_found_branch))


            }
        })
        viewModel.responseGetTicketCountByBranchIdApiCall()
            .observe(requireActivity(), androidx.lifecycle.Observer {
                showLoaderPeopleData(false)
                if (it != null) {
                    if (it.status == Constant.STATUS.SUCCESS) {
                        hideLoading()
                        try {
                            if (it.data != null) {

                                if (serviceData != null) {
                                    serviceData!!.currentBranchPeopleCount = it.data!!.count!!
                                    serviceData!!.currentBranchWaitTime = it.data!!.waitTime.toDouble()
                                }

                                var count = it.data!!.count!!.toString()
                                breakTime = it.data!!.breakTime
                                var time = it.data!!.waitTime
                                waitingTime = it.data!!.waitTime

                                binding.tvMember.setText(
                                    Utils.showMemberToDisplay(
                                        requireContext(),
                                        count
                                    )
                                )
                                if (inBreak!! || inGraceTime!!) {
                                    binding.tvTime.visibility = View.GONE
                                    binding.tvAwait?.text =
                                        context?.resources?.getText(R.string.awaiting_processing)

                                } else {
                                    if (it.data!!.agentCount==0){
                                        binding.tvTime.visibility = View.GONE
                                        binding.tvAwait?.text =
                                            context?.resources?.getText(R.string.awaiting_processing)
                                    }else{
                                        binding.tvTime.visibility = View.VISIBLE
                                    }

                                    try {
                                        binding.tvTime.text = Utils.showTimeToDisplay(
                                            requireActivity(),
                                            time.toDouble()
                                        )
                                    }catch (e:Exception){
                                        binding.tvTime.setText(
                                            Utils.showTimeToDisplay(
                                                requireContext(),
                                                time.toDouble()
                                            )
                                        )
                                    }

                                }


                                binding.llPeopleData.visibility = View.VISIBLE
                                binding.viewDoted.visibility = View.VISIBLE
                            } else {
                                binding.llPeopleData.visibility = View.GONE
                                binding.viewDoted.visibility = View.GONE
                            }
                        } catch (e: Exception) {
                        }

                    } else {
                        hideLoading()
                        binding.llPeopleData.visibility = View.GONE
                        binding.viewDoted.visibility = View.GONE
                        var message =
                            if (!TextUtils.isEmpty(it.message)) it.message!! else getString(R.string.queue_not_found)
                        showErrorMsg(message)
                    }
                } else {
                    hideLoading()
                    binding.llPeopleData.visibility = View.GONE
                    binding.viewDoted.visibility = View.GONE
                    // showSuccessMsg(resources.getString(R.string.in_error))
                    //   var message =  resgetString(R.string.queue_not_found)

                }
            })
        var args: Bundle
        viewModel.responseAddTicketApiCall()
            .observe(this ?: requireActivity(), androidx.lifecycle.Observer {
                hideLoading()
                if (it != null) {
                    if (it.status == Constant.STATUS.SUCCESS) {
                        //clear flag for segment
                        prefManager.setString(PrefConst.USER_CURRENT_SEGMENT, "")
                        prefManager.setString(PrefConst.USER_CURRENT_SEGMENT_PREFIX, "")

                        var data = it.data
                        if (data != null) {
                            data.logoURL = serviceData!!.logoURL!!
                            data.servingTime = serviceData!!.servingTime!!

//                            args = Bundle()
//                            args.putSerializable(Constant.INTENT_EXTRA.SHIFTSARRAYLIST, mainResponse?.shifts as ArrayList<Data.TodayTime.Shift>)

                            startActivity(
                                Intent(
                                    activity,
                                    CustomerConfirmationTicketActivity::class.java
                                )
//                                    .putExtra(Constant.INTENT_EXTRA.SHIFTS, args)
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
            hideLoading()
            if (it.status == Constant.STATUS.SUCCESS) {
                var bean: CheckBranchBean = it
                isOpenBranch = bean.data.isOpenBranch
                inBreak = bean.data.inBreak
                inGraceTime = bean.data.inGraceTime


                binding.txtInstantTicket.visibility =
                    if (serviceData!!.isOpenBranch || inBreak!! || inGraceTime!!) View.VISIBLE else View.GONE
                binding.tvClose.visibility =
                    if (serviceData!!.isOpenBranch || inBreak!! || inGraceTime!!) View.GONE else View.VISIBLE


                if (isOpenBranch!! || inBreak!! || inGraceTime!!) {
                    callApi(1)
                } else {
                    showErrorMsg(it.message)
                }
            } else {
                showErrorMsg(it.message)
            }
        })
    }


    fun validateSelectedBranch(): Boolean {


        var travelTime = nearByCurrentBranchMinutes
        var waitingTime = serviceData!!.currentBranchWaitTime!!
        var isError = false

        var errorMessage = ""

        Utils.print("validateSelectedBranch = travelTime=" + travelTime + "/ waitingTime=" + waitingTime)
        if (!isAgentAvailable()) {
            errorMessage = resources.getString(R.string.error_agent_not_available)

            isError = true
        } else if (travelTime > 5 && travelTime > waitingTime) {

            var waitTimeInt = waitingTime.toInt()
            errorMessage = resources.getString(
                R.string.error_msg_book_ticket,
                Utility.convertToArabic(waitTimeInt.toString()),
                serviceData!!.name
            )

            isError = true

        }

        if (isError) {
            showErrorMsg(errorMessage)
        }

        return isError
    }

    private fun gotoNextStep(gotFaster: Boolean = false) {
        showLoaderFaster(false)

       Log.e("commaSeperatedString","$commaSeperatedString <--")
       Log.e("commaSeperatedString","$mainResponse <--")
//        val bottomSheetFragmentFast = CustomerFastreBranchBottomSheetFragment()
        val bottomSheetFragment = ConfirmBookingTicketBottomSheetFragment()

        val bundle = Bundle()
        bundle.putSerializable(Constant.INTENT_EXTRA.FORM_FILLED_DETAILS_FORM_ID,formDetail)
        bundle.putSerializable(Constant.INTENT_EXTRA.FROM_NEAR_BY, fromNearBy)
        bundle.putSerializable(Constant.INTENT_EXTRA.BREAK_TIME, breakTime)
        bundle.putSerializable(Constant.INTENT_EXTRA.WAITING_TIME, waitingTime)
        bundle.putSerializable(Constant.INTENT_EXTRA.SERVICE_BEAN, serviceData)
        Log.e("merchantId22",serviceData!!.merchantID)
        bundle.putSerializable(Constant.INTENT_EXTRA.TIME_BEAN, bankTimeArray)
        bundle.putSerializable(Constant.INTENT_EXTRA.SELECTED_BRANCH_MINUTES,
            nearByCurrentBranchMinutes)
        bundle.putSerializable(Constant.INTENT_EXTRA.SELECTED_BRANCH_DISTANCE,
            nearByCurrentBranchDistance)

            if (!TextUtils.isEmpty(serviceData!!.oldBranchIDTemp)) {
                serviceData!!.branchID = serviceData!!.oldBranchIDTemp
                serviceData!!.branchName = serviceData!!.oldBranchNameTemp
            }
            bottomSheetFragment.arguments = bundle
            bottomSheetFragment.show(childFragmentManager, bottomSheetFragment.tag)
//        }
    }

}