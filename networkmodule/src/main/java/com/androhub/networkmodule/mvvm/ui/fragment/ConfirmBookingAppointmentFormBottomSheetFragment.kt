//package com.mylib.mvvm.ui.fragment
//
//import android.content.Context
//import android.content.DialogInterface
//import android.content.Intent
//import android.os.Bundle
//import android.os.SystemClock
//import android.text.TextUtils
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.databinding.DataBindingUtil
//import androidx.lifecycle.ViewModelProvider
//import com.mylib.PrefConst
//import com.androhub.networkmodule.R
//import com.androhub.networkmodule.databinding.BottomSheetConfirmBookTicketAppointmentBinding
//import com.mylib.interfaces.AppointmentBacktoServiceInterface
//import com.mylib.local.Utility
//import com.mylib.mvvm.model.request.AppointmentAddTicketRequestBean
//import com.mylib.mvvm.model.response.AddTicketResponseBean
//import com.mylib.mvvm.model.response.getbranchdetails.Service
//import com.mylib.mvvm.viewmodel.CustomerBookingTicketViewModel
//import com.mylib.utils.Constant
//
//import com.google.android.material.bottomsheet.BottomSheetBehavior
//
//class ConfirmBookingAppointmentFormBottomSheetFragment(var interfaceAppointment: AppointmentBacktoServiceInterface) : BaseFragmentBottomSheet(),
//    View.OnClickListener {
//    lateinit var binding: BottomSheetConfirmBookTicketAppointmentBinding
//
//
//    var serviceData: Service? = null
//
//    var fromNearBy: Boolean = false
//    var beanToSend = AppointmentAddTicketRequestBean()
//    lateinit var viewModel: CustomerBookingTicketViewModel
//
//
//
//
//
//    override fun onStart() {
//        super.onStart()
//        val behavior = BottomSheetBehavior.from(requireView().parent as View)
//        behavior.state = BottomSheetBehavior.STATE_EXPANDED
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?,
//    ): View {
//        binding = DataBindingUtil.inflate(
//            inflater,
//            R.layout.bottom_sheet_confirm_book_ticket_appointment,
//            container,
//            false
//        )
//
//        if (arguments != null) {
//
////            showLoaderPeopleData(true)
//            try {
//                if (requireArguments().containsKey(Constant.INTENT_EXTRA.APPOINTMENT_BEAN)){
//                    beanToSend =  requireArguments().getSerializable(Constant.INTENT_EXTRA.APPOINTMENT_BEAN) as AppointmentAddTicketRequestBean
//                }
//                serviceData =
//                    requireArguments().getSerializable(Constant.INTENT_EXTRA.SERVICE_BEAN) as Service?
//
//
//
//            } catch (e: Exception) {
//
//            }
//
//
//        }
//
//        viewModel = ViewModelProvider(this).get(CustomerBookingTicketViewModel::class.java)
//        initUi()
//        setObserver()
//
//        return binding.root
//    }
//
//
//    override fun onResume() {
//        super.onResume()
//        setAnalyticsAppScreen(activity, Constant.APP_SCREEN.confirmReBooking)
//    }
//
//    override fun onDismiss(dialog: DialogInterface) {
//
//        super.onDismiss(dialog)
//
//    }
//
//
//    override fun initUi() {
//        binding.imgClose2.setOnClickListener(this)
//        binding.txtConfirm2.setOnClickListener(this)
//        binding.txtBack2.setOnClickListener(this)
//
//        binding.tvInfoMsg.setText(
//            Utility.fromHtml(
//                resources.getString(
//                    R.string.confirm_book_ticket_info,
//                    serviceData!!.name,
//                    serviceData!!.branchName
//                )
//            )
//        )
//
//            binding.include.llInstruction.visibility = View.GONE
//            serviceData?.let {
//                if (!TextUtils.isEmpty(it.description)) {
//                    binding.include.llInstruction.visibility = View.VISIBLE
////                    binding.include.llInstruction.tvInstruction.text = it.description
//
//                }
//            }
//
//
//
//    }
//
//    override fun getLayoutId(): View {
//        return binding.root
//    }
//
//
//    override fun getContext(): Context? {
//        return activity
//    }
//
//    // variable to track event time
//    private var mLastClickTime: Long = 0
//    override fun onClick(view: View) {
//        when (view.id) {
//            R.id.txtBack2 -> {
//                interfaceAppointment.onBackpress(true)
//                dismiss()
//
//            }
//
//            R.id.imgClose2 -> {
//                dismiss()
//            }
//
//            R.id.txtConfirm2 -> {
//                if (SystemClock.elapsedRealtime() - mLastClickTime < 3000) {
//                    return;
//                }
//                mLastClickTime = SystemClock.elapsedRealtime();
//
//                callApi()
////                dismiss()
//
//
////                    setAnalyticsEvent(requireActivity(), Constant.APP_ACTION.confirmBooking)
////                    if (breakTime!! > 0) {
////                        showBreakTimeDialog()
////                    } else {
////                        callApi(2)
////                    }
//
//
//            }
//
//        }
//    }
//
//
//    private fun callApi(tag: Int = 1, showLoading: Boolean = true) {
//        if (!isConnected(context, true))
//            return
//        if (tag == 1) {
//
//            showLoaderPeopleData(true)
//            if (beanToSend.appointmentTime != null && beanToSend.appointmentTime!! > 0 && beanToSend.date!!.isNotEmpty() && beanToSend.date != null) {
//                viewModel.requestAddTicketAppointmentApiCall(requireContext(), beanToSend)
//            } else {
////                showLoaderPeopleData(false)
//                showErrorMsg("Please select")
//            }
//        }
//
//    }
//    private fun showLoaderPeopleData(showLoading: Boolean) {
//        if (showLoading) {
////            binding.llMainBottom.visibility = View.GONE
//            binding.progressLoadmorePeople.visibility = View.VISIBLE
//        } else {
////            binding.llMainBottom.visibility = View.VISIBLE
//            binding.progressLoadmorePeople.visibility = View.GONE
//
//
//        }
//    }
//
//
//    private fun setObserver() {
//        viewModel.responseAddTicketAppointmentApiCall().observe(this, androidx.lifecycle.Observer {
////            hideLoading()
//            showLoaderPeopleData(false)
//            if (it != null) {
//                if (it.status == Constant.STATUS.SUCCESS) {
//
//                    prefManager.setString(PrefConst.USER_CURRENT_SEGMENT, "")
//                    prefManager.setString(PrefConst.USER_CURRENT_SEGMENT_PREFIX, "")
//
//                    var data = it.data
//                    if (data != null) {
//                        data.logoURL = serviceData!!.logoURL
//                        data.servingTime = serviceData!!.servingTime!!
//                        var ticketData = AddTicketResponseBean.Data()
//                        ticketData.appointmentTime = data.appointmentTime.toString()
//                        ticketData.appointmentEndTime = data.appointmentEndTime.toString()
//
//                        ticketData.location = data.location
//                        ticketData.locationArabic = data.locationArabic
//                        ticketData.approxTime = data.approxTime
//                        ticketData.branchId = data.branchId
//                        ticketData.branchName = data.branchName
//                        ticketData.createdAt = data.createdAt
//                        ticketData.customerId = data.customerId
//                        ticketData.customerName = data.customerName
//                        ticketData.id = data.id
//                        ticketData.number = data.number
//                        ticketData.queueId = data.queueId
//                        ticketData.serviceId = data.serviceId
//                        ticketData.serviceName = data.serviceName
//                        ticketData.updatedAt = data.updatedAt
//                        ticketData.appointmentTime = data.appointmentTime.toString()
//                        ticketData.ticketType = data.ticketType
//                        ticketData.userId = data.userId
//                        ticketData.userName = data.userName
//                        ticketData.merchantName = data.merchantName
//                        ticketData.merchantLogo = data.merchantLogo
//                        ticketData.processedByUserId = data.processedByUserId
//
//
//                        ticketData.starttime = data.starttime
//                        ticketData.eventName = data.eventName
//                        ticketData.eventLogo = data.eventLogo
//
//                        ticketData.date = data.date
//                        ticketData.status = data.status
//                        ticketData.logoURL = data.logoURL
//                        ticketData.servingTime = data.servingTime
//                        ticketData.customersCount = data.customersCount
//                        ticketData.notCallDetailsAPI = data.notCallDetailsAPI
//                        ticketData.isFromPast = data.isFromPast
//                        ticketData.appointmentEndTime = data.appointmentEndTime.toString()
//                        startActivity(
//                            Intent(
//                                activity,
//                                CustomerConfirmationAppointmentActivity::class.java
//                            )
//                                .putExtra(Constant.INTENT_EXTRA.IN_GRACETIME, false)
//                                .putExtra(Constant.INTENT_EXTRA.IN_BREAK, false)
//                                .putExtra(Constant.INTENT_EXTRA.TICKET_BEAN, ticketData)
//                                .putExtra(Constant.INTENT_EXTRA.TICKET_ADDED, true)
//                        )
//                        requireActivity().finishAffinity()
//
//                    }
//                    else{
////                        callApi(2)
//                        showLoaderPeopleData(false)
//                        showErrorMsg(requireContext().resources.getString(R.string.no_results_were_found))
//                    }
//
//                } else {
////                    callApi(2)
//                    // Custom firebase performance SDK events tracking development
//                    var performanceRequestBean = PerformanceRequestBean()
//                    performanceRequestBean.eventName =
//                        Constant.PERFORMANCE.serviceMerchantUnavilableShown
//                    performanceRequestBean.UserID =
//                        prefManager.getString(PrefConst.PREF_USER_ID)
//
//                    performanceRequestBean.merchantId = serviceData!!.merchantID
//                    performanceRequestBean.merchantName = serviceData!!.merchantName
//                    performanceRequestBean.branchId = serviceData!!.branchID
//                    performanceRequestBean.branchName = serviceData!!.branchName
//                    performanceRequestBean.serviceId = serviceData!!.id
//                    performanceRequestBean.serviceName = serviceData!!.name
//
//                    trackPerformanceFirebase(performanceRequestBean)
//
//                    var message =
//                        if (!TextUtils.isEmpty(it.message)) it.message!! else getString(R.string.queue_not_found)
//                    showLoaderPeopleData(false)
//                    showErrorMsg(message)
//                    //  showDialogAsStatus(response.status, response.message)
//                }
//            } else {
//                callApi(2)
//                showSuccessMsg(resources.getString(R.string.in_error))
//            }
//        })
//
//
//    }
//
//}