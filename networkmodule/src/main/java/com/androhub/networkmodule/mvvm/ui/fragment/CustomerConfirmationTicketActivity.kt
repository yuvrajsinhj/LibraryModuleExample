package com.androhub.networkmodule.mvvm.ui.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.androhub.networkmodule.PrefConst
import com.androhub.networkmodule.R
import com.androhub.networkmodule.base.BaseActivityMain
import com.androhub.networkmodule.databinding.ActivityCustomerConfirmationTicketBinding
import com.androhub.networkmodule.local.Utility
import com.androhub.networkmodule.mvvm.model.request.TicketCountRequestBean
import com.androhub.networkmodule.mvvm.model.response.AddTicketResponseBean
import com.androhub.networkmodule.mvvm.model.response.CheckBranchBean
import com.androhub.networkmodule.mvvm.model.response.getbranchdetails.Data
import com.androhub.networkmodule.mvvm.ui.BranchActivity
import com.androhub.networkmodule.mvvm.viewmodel.CustomerBookingTicketViewModel
import com.androhub.networkmodule.uc.swipeButton.controller.OnSwipeCompleteListener
import com.androhub.networkmodule.uc.swipeButton.iew.Swipe_Button_View
import com.androhub.networkmodule.utils.Constant
import com.androhub.networkmodule.utils.ImageDisplayUitls
import com.androhub.networkmodule.utils.Utils
import com.androhub.networkmodule.utils.Utils.getDate
import java.lang.Double
import java.util.TimeZone
import kotlin.Boolean
import kotlin.Exception
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.let
import kotlin.toString

class CustomerConfirmationTicketActivity : BaseActivityMain(), View.OnClickListener {
    lateinit var binding: ActivityCustomerConfirmationTicketBinding
    var ticketData: AddTicketResponseBean.Data? = null
    var doNotRefresh: Boolean = false
    lateinit var viewModel: CustomerBookingTicketViewModel
    var agentCount = 3
    var isOpenBranch: Boolean? = false
    var inBreak: Boolean? = false
    var inGraceTime: Boolean? = false
    var shifts: List<Data.Shift>? = null
    lateinit var language: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        language = prefManager.language
        prefManager.setBoolean(PrefConst.TICKET_BOOKED_HIDE_IMAGE_SLIDER, true)
        mActivity = this

        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_customer_confirmation_ticket)
        binding.lifecycleOwner = this
        viewModel = ViewModelProvider(this).get(CustomerBookingTicketViewModel::class.java)

//        showLoading()
        initUi()

        if (intent.extras != null && intent.hasExtra(Constant.INTENT_EXTRA.DO_NOT_REFRESH)) {
            doNotRefresh = intent.getBooleanExtra(Constant.INTENT_EXTRA.DO_NOT_REFRESH, false)
        }
        if (intent.extras != null && intent.hasExtra(Constant.INTENT_EXTRA.IN_GRACETIME)) {
            inGraceTime = intent.getBooleanExtra(Constant.INTENT_EXTRA.IN_GRACETIME, false)
        }
        if (intent.extras != null && intent.hasExtra(Constant.INTENT_EXTRA.IN_BREAK)) {
            inBreak = intent.getBooleanExtra(Constant.INTENT_EXTRA.IN_BREAK, false)
        }

//        val args = intent.getBundleExtra(Constant.INTENT_EXTRA.SHIFTS)
//        if (args != null) {
//            shifts = args.getSerializable(Constant.INTENT_EXTRA.SHIFTSARRAYLIST) as ArrayList<Data.TodayTime.Shift>
//
//        }

        if (inBreak!! || inGraceTime!!) {
            binding.tvTime.visibility = View.GONE
            binding.tvAwait.text = getContext()?.resources?.getText(R.string.awaiting_processing)

        }

        if (intent.extras != null && intent.hasExtra(Constant.INTENT_EXTRA.TICKET_BEAN)) {
            ticketData =
                intent.getSerializableExtra(Constant.INTENT_EXTRA.TICKET_BEAN) as AddTicketResponseBean.Data?
//            Utils.print("t status  " + ticketData?.status)
//            Utils.print("t status  " + ticketData?.customersCount)

        }
        setData()
//        if (ticketData!!.ticketType != null && !ticketData?.ticketType.equals(Constant.NEW_EVENT.EVENT)) {
//            showLoading()
//            callApi("", 4)
//        }


        if (intent.extras != null && intent.hasExtra(Constant.INTENT_EXTRA.TICKET_ADDED)) {
            //This flag while added new tickets store in pref so flickering issue will solved in Home screen
            var ticketAdded = intent.getBooleanExtra(Constant.INTENT_EXTRA.TICKET_ADDED, false)
//            Utils.print("refreshWaitTimPeople 2 " + ticketAdded)
            if (ticketAdded) {
                //callApi("", 3)
            }
        }
//        if (ticketData!!.ticketType != null && ticketData?.ticketType.equals(Constant.NEW_EVENT.EVENT)) {
//            setData()
//        }

        callApi("", 3)
        Handler().postDelayed(Runnable {
            refreshData()
            binding.rlMainP.visibility = View.VISIBLE
        }, 1000)


        setStatusBarColor(R.color.bg_color_green)
    }

    companion object {
        var mActivity: CustomerConfirmationTicketActivity? = null
    }


    private fun setData() {
        if (ticketData != null) {
//            if (isOpenBranch!!){
            try {
                binding.llEvent.visibility = View.GONE
                binding.llMainTimer.visibility = View.VISIBLE
                if (ticketData!!.status == "cancelled") {
                    binding.llEvent.visibility = View.GONE
                    binding.llMainTimer.visibility = View.VISIBLE
                    binding.ivTimer.visibility = View.VISIBLE
                }
                if (ticketData!!.status == "completed") {
                    binding.llEvent.visibility = View.GONE
                    binding.llMainTimer.visibility = View.VISIBLE
                    binding.ivTimer.visibility = View.VISIBLE
                }
                if (inBreak!! || inGraceTime!!) {

                    binding.tvHeading.setText(
                        getContext()!!.getResources()
                            .getString(
                                R.string.your_ticket_withdrawal_service,
                                ticketData!!.serviceName
                            )
                    );
                    binding.tvTicketNumber.text = ticketData!!.number
                    binding.tvBankName.text = ticketData!!.merchantName
                    binding.tvBranchName.text = ticketData!!.branchName

                    //getServingTime()+mContext.getString(R.string.min_wait_time)


                    ImageDisplayUitls.displayImageSlider(
                        ticketData!!.logoURL,
                        getContext(),
                        binding.ivLogo, ticketData!!.merchantName
                    )

                    if (!ticketData!!.notCallDetailsAPI) {
                        callApi(ticketData!!.id!!)
                    } else {

                        binding.tvTime.visibility = View.GONE
                        binding.tvAwait?.text =
                            getContext()?.resources?.getText(R.string.awaiting_processing)

                    }
                    binding.tvMember.text =
                        Utils.showMemberToDisplay(
                            getContext()!!,
                            "" + (ticketData!!.customersCount)
                        )

                    setStatusAndCancelButton()


                } else {
                    if (agentCount == 0) {
                        binding.tvTime.visibility = View.GONE
                        binding.tvAwait.text =
                            getContext()?.resources?.getText(R.string.awaiting_processing)
                    } else {
                        binding.tvTime.visibility = View.VISIBLE
                        binding.tvAwait.text =
                            getContext()?.resources?.getText(R.string.waiting_time_in_queue)
                    }

                    if (ticketData != null) {
                        binding.tvHeading.setText(
                            getContext()!!.getResources()
                                .getString(
                                    R.string.your_ticket_withdrawal_service,
                                    ticketData!!.serviceName
                                )
                        );
                        binding.tvTicketNumber.text = ticketData!!.number
                        binding.tvBankName.text = ticketData!!.merchantName
                        binding.tvBranchName.text = ticketData!!.branchName

                        //getServingTime()+mContext.getString(R.string.min_wait_time)


                        ImageDisplayUitls.displayImageSlider(
                            ticketData!!.logoURL,
                            getContext(),
                            binding.ivLogo, ticketData!!.merchantName
                        )

                        if (!ticketData!!.notCallDetailsAPI) {
                            callApi(ticketData!!.id!!)
                        } else {
                            Log.e("tvTime--2", "showTimeToDisplay")
                            binding.tvTime.setText(
                                Utils.showTimeToDisplay(
                                    getContext()!!,
                                    ticketData!!.servingTime!!
                                )
                            )
                            binding.tvMember.text =
                                Utils.showMemberToDisplay(
                                    getContext()!!,
                                    "" + (ticketData!!.customersCount)
                                )
                        }

                        setStatusAndCancelButton()


                    }
                }
//                }

            } catch (e: Exception) {

            }


        } else {

        }

    }

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
        ) return context.resources.getString(R.string.status_in_processed)
        else if (serverStatus.equals(
                Constant.TICKET_STATUS.STATUS_ON_HOLD,
                true
            )
        ) return context.resources.getString(R.string.status_on_hold_two)
        else return serverStatus!!


    }

    fun setStatusButton() {
        Utils.print("t status 2  " + ticketData?.status)
        if (!binding.txtCancel.isVisible && !TextUtils.isEmpty(ticketData!!.status)) {
            binding.tvsStatus.visibility = View.VISIBLE
            var status = ""
            if (ticketData != null && ticketData!!.isFromPast) {
                status = Utils.getTicketStatus(this, ticketData!!.status)
            } else {
                status = getTicketStatus(this, ticketData!!.status)
            }
            var stat = resources.getString(R.string.status_in_processed)
            if (status == stat) {
                binding.llInstantCounter!!.visibility = View.VISIBLE
                binding.llPeopleData.visibility = View.GONE

                binding.llMainTimer.visibility = View.GONE
                Log.e("Counter", ticketData!!.counter.toString() + "---")
                binding.tvsStatus.visibility = View.GONE
                binding.tvGotoCounter!!.text =
                    Utility.fromHtml(
                        resources.getString(
                            R.string.go_to_counter_str,
                            ticketData!!.counter
                        )
                    )
                if (language == Constant.LANGUAGE.AREBIC) {
                    binding.tvLocation.setText(ticketData!!.locationArabic)
                } else {
                    binding.tvLocation.setText(ticketData!!.location)
                }
            } else {
                binding.tvsStatus.visibility = View.VISIBLE
                binding.llPeopleData.visibility = View.VISIBLE
                binding.llInstantCounter!!.visibility = View.GONE
                binding.tvsStatus.text = status
            }

//            binding.tvLocation.text = ticketData?.location


            if (ticketData!!.status == Constant.TICKET_STATUS.STATUS_ON_HOLD) {
                binding.llPeopleData.visibility = View.GONE
            } else binding.llPeopleData.visibility = View.VISIBLE
        } else {
            binding.tvsStatus.visibility = View.GONE
        }
    }

    fun setStatusAndCancelButton() {
        try {
            val serverCreatedDate: Long = ticketData!!.createdAt!!
            binding.tvDate.text = getDate(serverCreatedDate)
        } catch (e: Exception) {
            Log.e("err", e.message.toString())
        }
        if (ticketData!!.isFromPast) {

            binding.txtCancel.visibility = View.GONE

            binding.tvDate.text = ticketData!!.date
            setStatusButton()

        } else {
//            binding.txtCancel.visibility = View.VISIBLE
//            binding.tvDate.visibility = View.GONE
            //binding.tvsStatus.visibility = View.GONE

            binding.txtCancel.visibility =
                if (!TextUtils.isEmpty(ticketData!!.processedByUserId) || ticketData!!.status.equals(
                        Constant.TICKET_STATUS.STATUS_NO_SHOW
                    )
                )
                    View.GONE
                else
                    View.VISIBLE
            setStatusButton()
        }
    }

    override fun initUi() {

        binding.ivClose.setOnClickListener(this)
        binding.txtCancel.setOnSwipeCompleteListener_forward_reverse(object :
            OnSwipeCompleteListener {
            override fun onSwipe_Forward(swipeView: Swipe_Button_View) {
                onCancelEvent()
            }

            override fun onSwipe_Reverse(swipeView: Swipe_Button_View) {
                //inactive function
            }
        })

    }

    override fun getContext(): Context? {
        return this
    }

    //
    override fun getLayoutId(): View? {
        return binding.root
    }

    override fun onClick(view: View) {
        when (view.id) {
            /* R.id.txtCancel -> {

                 onCancelEvent()

             }*/
            R.id.ivClose -> {
                onBackPressedSupport()

            }

        }
    }

    private fun onCancelEvent() {

        if (ticketData?.ticketType != null) {
//            if (ticketData?.ticketType.equals(Constant.NEW_EVENT.EVENT)) {
//                if (ticketData!!.starttime != null) {
//                    val min = Utils.getEventStartTime(ticketData!!.starttime!!)
//                    if (min >= 0) {
//                        var bean = EventTicketCancleRequestBean()
//                        bean.id = ticketData?.id
//                        bean.status = Constant.TICKET_STATUS.STATUS_CANCELLED
//                        viewModel2.requetNewEventCancleApiCall(this, bean)
//                    } else {
////                        showErrorMsg("No")
//                    }
//                }
//
//
//            } else {

            val bottomSheetFragment =
                CustomerCancelTicketBottomSheetFragment()
            if (ticketData != null) {


                val bundle = Bundle()
                bundle.putSerializable(Constant.INTENT_EXTRA.TICKET_BEAN, ticketData)
                bottomSheetFragment.arguments = bundle
            }
            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
            //                startActivity(Intent(getContext(), CustomerLeaveQueueActivity::class.java))
            //startActivity(Intent(getContext(), DashboardActivity::class.java))
            //finishAffinity()
//            }
        } else {

            val bottomSheetFragment =
                CustomerCancelTicketBottomSheetFragment()
            if (ticketData != null) {


                val bundle = Bundle()
                bundle.putSerializable(Constant.INTENT_EXTRA.TICKET_BEAN, ticketData)
                bottomSheetFragment.arguments = bundle
            }
            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
            //                startActivity(Intent(getContext(), CustomerLeaveQueueActivity::class.java))
            //startActivity(Intent(getContext(), DashboardActivity::class.java))
            //finishAffinity()
        }
    }


    private fun callApi(ticketID: String = "", tag: Int = 1) {
        if (!isConnected(true))
            return

        if (tag == 1) {
//            showLoading()
            val hashMap = TicketCountRequestBean()
            hashMap.ticketId = ticketID

            var timeZone =
                if (!TextUtils.isEmpty(prefManager.getString(PrefConst.BRANCH_TIME_ZONE))) prefManager.getString(
                    PrefConst.BRANCH_TIME_ZONE
                ) else TimeZone.getDefault().getID()

            hashMap.timeZone = timeZone
            shifts = Constant.TEMP_SHIFT
            if (shifts != null) {
                hashMap.shift = shifts
            }

//            hashMap.put("ticketId", ticketID)
            viewModel.requestGetTicketCountByBranchIdApiCall(
                getContext()!!,
                hashMap
            )   // -- waitetime included in this
//            showLoading()
        } else if (tag == 2) {
            val hashMap = HashMap<String, String>()

            hashMap.put("id", ticketID)

            viewModel.requestGetTicketByIdListApiCall(getContext()!!, hashMap)
            //  showLoading()
        } else if (tag == 3) {
//            showLoading()
            Utils.print("custAPI 4")
            var branchStatus = "close"
            if (inBreak!!) {
                branchStatus = "break"
            } else if (inGraceTime!!) {
                branchStatus = "grace"
            } else if (isOpenBranch!!) {
                if (inBreak!!) {
                    branchStatus = "break"
                } else {
                    branchStatus = "open"
                }
            }
            val hashMap = HashMap<String, String>()
            hashMap.put("customerId", prefManager.getString(PrefConst.PREF_USER_ID))
            hashMap.put("branchStatus", branchStatus)

            viewModel.requestGetTicketByCustomerListApiCall(getContext()!!, hashMap)

        } else if (tag == 4) {


            if (ticketData?.branchId != null) {
                val hashMap = HashMap<String, String>()
                hashMap.put("branchID", ticketData?.branchId!!)


//                showLoading()
                viewModel.requestCheckBranchApiCall(this, hashMap)
            }

        }
        setObserver()

    }

    private fun setObserver() {
//        viewModel2.responseNewEventCancleApiCall().observe(this, androidx.lifecycle.Observer {
//            hideLoading()
//            if (it != null) {
//                val intent = Intent(activity, DashboardActivity::class.java)
//                startActivity(intent)
//                finish()
//            }
//        })
        viewModel.responseGetTicketByCustomerListApiCall()
            .observe(this, androidx.lifecycle.Observer {
                hideLoading()
                if (it != null && it.data != null) {
                    if (it.status == Constant.STATUS.SUCCESS) {

                        var ticketList = it.data!!.ticket
                        Utils.print("ticket added List:" + ticketList.size)
//                        prefManager.saveTicketList(null)
//                        prefManager.saveTicketList(ticketList)

                    } else {
//                        prefManager.saveTicketList(null)

                    }
                } else {
//                    prefManager.saveTicketList(null)

                }
            })
        viewModel.responseGetTicketByIdListApiCall()
            .observe(this, androidx.lifecycle.Observer { it ->
                hideLoading()
                if (it != null) {
                    if (it.status == Constant.STATUS.SUCCESS) {
                        if (it.data != null && it.data!!.ticket != null) {


                            var ticketDataBean = it.data!!.ticket

                            Log.e("branchStatus", it.data?.ticket?.branchStatus.toString())

                            ticketDataBean?.let { it ->
                                ticketData!!.serviceId = it.serviceId
                                ticketData!!.serviceName = it.serviceName
                                ticketData!!.status = it.status
                                ticketData!!.counter = it.counter
                                ticketData!!.eventLogo = it.eventLogo
                                ticketData!!.eventName = it.eventName
                                ticketData!!.endTime = it.endTime

                                ticketData!!.textUnderTime = it.textUnderTime
                                ticketData!!.arabicTextUnderTime = it.arabicTextUnderTime
                                ticketData!!.textUnderTimeFinish = it.textUnderTimeFinish
                                ticketData!!.arabicTextUnderTimeFinish =
                                    it.arabicTextUnderTimeFinish
                                ticketData!!.eventLogo = it.eventLogo
                                ticketData!!.eventName = it.eventName
                                ticketData!!.starttime = it.starttime
                                ticketData!!.endTime = it.endTime
                                ticketData!!.locationArabic = it.locationArabic
                                ticketData!!.location = it.location
                                agentCount = it.agentCount
                                Log.e("agentCount", agentCount.toString() + "----")
                                setStatusButton()
                                setData()
                            }

                        }

                    } else {
                        var message =
                            if (!TextUtils.isEmpty(it.message)) it.message!! else resources.getString(
                                R.string.please_try_again
                            )
                        showDialogAsStatus(it.status, message)
                    }
                } else {
                    showSuccessMsg(resources.getString(R.string.in_error))
                }
            })
        viewModel.responseGetTicketCountByBranchIdApiCall()
            .observe(this, androidx.lifecycle.Observer {
                hideLoading()
                if (it != null) {
                    if (it.status == Constant.STATUS.SUCCESS) {
                        if (it.data != null) {
                            var count = it.data!!.count!!.toString()
                            var time = it.data!!.waitTime!!
                            binding.tvMember.setText(
                                Utils.showMemberToDisplay(
                                    getContext()!!,
                                    count
                                )
                            )
                            binding.tvTime.setText(
                                Utils.showTimeToDisplay(
                                    getContext()!!,
                                    time.toDouble()
                                )
                            )
                            Log.e("tvTime--1", "$time")
                        }

                    } else {
                        var message =
                            if (!TextUtils.isEmpty(it.message)) it.message!! else resources.getString(
                                R.string.please_try_again
                            )
                        showDialogAsStatus(it.status, message)
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
                    setData()

                } else {
                    showErrorMsg(it.message)
                }
            }
        })
    }

    override fun onBackPressedSupport() {
        if (doNotRefresh || (ticketData != null && ticketData!!.isFromPast)) {
            overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
            super.onBackPressedSupport()
        } else {

            //super.onBackPressedSupport()
            /*   intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);*/
            try {
                startActivity(
                    Intent(
                        this,
                        BranchActivity::class.java
                    ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                )

                finishAffinity()
            } catch (e: java.lang.NullPointerException) {

            } catch (e: java.lang.RuntimeException) {
            }

        }
    }

    override fun onResume() {
        super.onResume()

    }

    override fun onDestroy() {
        super.onDestroy()
        mActivity = null
        stopRepeatOpetation()
        try {
            stopRepeatingTask()
        } catch (e: Exception) {

        }

    }

    //update UI for Notification

    override fun showLoading() {
        binding.llProgressBar.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        binding.llProgressBar.visibility = View.GONE
    }

    //Refresh
    var updater: Runnable? = null
    val timerHandler = Handler()

    private fun refreshData() {
        Utils.print("refreshData  ")
//        updater = Runnable {
//            Utils.print("refreshData 2 ")
//
//
//
//            timerHandler.postDelayed(updater!!, Constant.REFRESH_TIME_HOME)
//        }
//        timerHandler.post(updater!!)
    }

    private fun stopRepeatOpetation() {
        updater?.let { timerHandler.removeCallbacks(it) };
    }


    fun startRepeatingTask() {
        mHandlerTask.run()
    }

    fun stopRepeatingTask() {
        mHandler.removeCallbacks(mHandlerTask)
    }


    private val INTERVAL = 1000 * 60 * 1 //1 minutes

    var mHandler = Handler()

    var mHandlerTask: Runnable = object : Runnable {
        override fun run() {
            doSomething()
            mHandler.postDelayed(this, INTERVAL.toLong())
        }
    }

    private fun doSomething() {


        Log.e("EEe", "Running timer")

        try {


            val min = Utils.getEventStartTime(ticketData!!.starttime!!)



            Log.e("min---", min.toString() + "<<<")
            if (language == Constant.LANGUAGE.AREBIC) {

                if (min == 0L || min < 0) {
                    if (min == 0L) {
                        binding.tvTime1.text = this.resources.getString(R.string.less_then_miniut)
                        binding.lblTimeFinish1.text = ticketData!!.arabicTextUnderTime
                    } else {
                        binding.lblTimeFinish1.text = ticketData!!.arabicTextUnderTimeFinish
                        binding.txtCancel.visibility = View.GONE
                        val min2 = Utils.getEventEndTime(ticketData!!.endTime!!)

                        if (min2 == 0L) {
                            binding.tvTime1.text =
                                this.resources.getString(R.string.less_then_miniut)
                            binding.lblTimeFinish1.text = ticketData!!.arabicTextUnderTimeFinish
                        } else {
                            binding.txtCancel.visibility = View.GONE
                            binding.tvTime1.text =
                                Utils.showTimeToDisplay(this, Double.valueOf(min2.toDouble()))
                        }

                    }

                } else {
                    binding.lblTimeFinish1.text = ticketData!!.arabicTextUnderTime
                    binding.tvTime1.text =
                        Utils.showTimeToDisplay(this, Double.valueOf(min.toDouble()))
                }

            } else {
                if (min == 0L || min < 0) {

                    if (min == 0L) {
                        binding.tvTime1.text = this.resources.getString(R.string.less_then_miniut)
                        binding.lblTimeFinish1.text = ticketData!!.textUnderTime
                    } else {
                        binding.lblTimeFinish1.text = ticketData!!.textUnderTimeFinish
                        binding.txtCancel.visibility = View.GONE
                        val min2 = Utils.getEventEndTime(ticketData!!.endTime!!)

                        binding.tvTime1.text =
                            Utils.showTimeToDisplay(this, Double.valueOf(min2.toDouble()))
                        if (min2 == 0L) {
                            binding.tvTime1.text =
                                this.resources.getString(R.string.less_then_miniut)
                            binding.lblTimeFinish1.text = ticketData!!.textUnderTimeFinish
                        } else {
                            binding.tvTime1.text =
                                Utils.showTimeToDisplay(this, Double.valueOf(min2.toDouble()))
                            binding.lblTimeFinish1.text = ticketData!!.textUnderTimeFinish
                        }
                        if (min2 < 0L) {
                            binding.llEvent.visibility = View.GONE
                        }
                    }


                } else {
                    binding.lblTimeFinish1.text = ticketData!!.textUnderTime
                    binding.tvTime1.text =
                        Utils.showTimeToDisplay(this, Double.valueOf(min.toDouble()))
                }


            }


        } catch (e: Exception) {

            Log.e("Exception", e.message.toString())
        }
    }

}