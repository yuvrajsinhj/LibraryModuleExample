package com.androhub.networkmodule.mvvm.ui.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.androhub.networkmodule.PrefConst
import com.androhub.networkmodule.R
import com.androhub.networkmodule.databinding.BottomSheetCustLeaveQueueBinding
import com.androhub.networkmodule.mvvm.model.request.CancelTicketRequestBean
import com.androhub.networkmodule.mvvm.model.response.AddTicketResponseBean
import com.androhub.networkmodule.mvvm.ui.BranchActivity
import com.androhub.networkmodule.mvvm.viewmodel.CustomerBookingTicketViewModel
import com.androhub.networkmodule.utils.Constant

class CustomerCancelTicketBottomSheetFragment : BaseFragmentBottomSheet(), View.OnClickListener {
    lateinit var binding: BottomSheetCustLeaveQueueBinding
     var ticketData: AddTicketResponseBean.Data? = null
    lateinit var viewModel: CustomerBookingTicketViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.bottom_sheet_cust_leave_queue,
            container,
            false
        )
         viewModel = ViewModelProvider(this).get(CustomerBookingTicketViewModel::class.java)

        if (arguments != null) {
            ticketData =
                requireArguments().getSerializable(Constant.INTENT_EXTRA.TICKET_BEAN) as AddTicketResponseBean.Data?
        }
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        initUi()

    }
    override fun onResume() {
        super.onResume()
    }
    override fun initUi() {
        binding.imgClose.setOnClickListener(this)
        binding.txtCancelQueue.setOnClickListener(this)
        binding.txtLeaveQueue.setOnClickListener(this)
        if (ticketData?.ticketType?.toLowerCase() == Constant.TICKET_TYPE.ESERVICE){
            binding.tvLeaveHeader.text = requireContext().resources.getString(R.string.eservice)
            binding.tvCancleDesc.text = requireContext().resources.getString(R.string.cancel_e_service_desc)
            binding.txtCancelQueue.text = requireContext().resources.getString(R.string.cancel)
            binding.txtLeaveQueue.text = requireContext().resources.getString(R.string.cancel_booking)
        }

    }

    override fun getLayoutId(): View {
        return binding.root
    }


    override fun getContext(): Context? {
        return activity
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.imgClose -> {
                dismiss()
            }

            R.id.txtLeaveQueue -> {
                /*startActivity(Intent(getContext(), DashboardActivity::class.java))
                finishAffinity(getContext() as Activity)*/
                callApi()
            }

            R.id.txtCancelQueue -> {

                dismiss()
            }

        }
    }

    private fun callApi() {
        if (!isConnected(context,true))
            return
        if (ticketData != null) {
//            if (ticketData!!.ticketType==Constant.TICKET_TYPE.ESERVICE){
//                var bean= EServiceTicketCancleRequestBean()
//                bean.id = ticketData!!.id!!
//
//                showLoading()
//                viewModel.requestEserviceTicketCancle(requireContext(), bean)
//            }else{
                var beanToSend = CancelTicketRequestBean()
                beanToSend.id = ticketData!!.id!!
                beanToSend.userId = prefManager.getString(PrefConst.PREF_USER_ID)

                var fname = prefManager.getString(PrefConst.PREF_FIRST_NAME)
                var lname = prefManager.getString(PrefConst.PREF_LAST_NAME)
                var cName = getFullName(fname, lname)
                beanToSend.userName = cName

                beanToSend.processingTime = ticketData!!.servingTime!!
                beanToSend.status = Constant.CANCEL_TICKET_STATUS


                showLoading()
                viewModel.requestCancelTicketApiCall(requireContext(),beanToSend)



            setObserver()
        } else showErrorMsg(requireContext().resources.getString(R.string.no_results_were_found))
    }

    private fun setObserver() {

        viewModel.responseCancelTicketApiCall().observe(this, androidx.lifecycle.Observer {
            hideLoading()
            if (it != null) {
                if (it.status == Constant.STATUS.SUCCESS) {
                    showSuccessMsg(this.resources.getString(R.string.sucess_cancelled))

                    //prefManager.setBoolean(PrefConst.TICKET_BOOKED_HIDE_IMAGE_SLIDER,true)
                    val intent = Intent(activity, BranchActivity::class.java)

//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent)
                    requireActivity().finishAffinity()


                } else {
                     var message = if (!TextUtils.isEmpty(it.message))it.message!! else resources.getString(R.string.please_try_again)
                    showDialogAsStatus(it.status, message)
                    //  showDialogAsStatus(response.status, response.message)
                }
            } else {
                showSuccessMsg(resources.getString(R.string.in_error))
            }
        })
//        viewModel.responseEserviceTicketCancle().observe(this, androidx.lifecycle.Observer {
//            hideLoading()
//            if (it != null) {
//                if (it.status == Constant.STATUS.SUCCESS) {
//                    showSuccessMsg(context!!.resources.getString(R.string.sucess_cancelled))
//
//                    //prefManager.setBoolean(PrefConst.TICKET_BOOKED_HIDE_IMAGE_SLIDER,true)
//                    val intent = Intent(activity, DashboardActivity::class.java)
//
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(intent)
//                    activity!!.finishAffinity()
//
//
//                } else {
//                    var message = if (!TextUtils.isEmpty(it.message))it.message!! else resources.getString(R.string.please_try_again)
//                    showDialogAsStatus(it.status, message)
//                    //  showDialogAsStatus(response.status, response.message)
//                }
//            } else {
//                showSuccessMsg(resources.getString(R.string.in_error))
//            }
//        })

    }
}