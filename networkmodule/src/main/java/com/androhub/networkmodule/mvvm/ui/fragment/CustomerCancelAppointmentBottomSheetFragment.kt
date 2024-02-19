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
import com.androhub.networkmodule.databinding.*
import com.androhub.networkmodule.mvvm.model.request.CancelTicketRequestBean
import com.androhub.networkmodule.mvvm.model.response.AddTicketResponseBean
import com.androhub.networkmodule.mvvm.ui.BranchActivity
import com.androhub.networkmodule.mvvm.viewmodel.AppointmentViewModel
import com.androhub.networkmodule.utils.Constant

class CustomerCancelAppointmentBottomSheetFragment : BaseFragmentBottomSheet(),
    View.OnClickListener {
    lateinit var binding: BottomSheetCustCancelAppointmentBinding
    lateinit var viewModel: AppointmentViewModel
    var ticketData: AddTicketResponseBean.Data? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.bottom_sheet_cust_cancel_appointment,
            container,
            false
        )
        viewModel = ViewModelProvider(this).get(AppointmentViewModel::class.java)
        if (arguments != null) {
            ticketData =
                requireArguments().getSerializable(Constant.INTENT_EXTRA.TICKET_BEAN) as AddTicketResponseBean.Data?
        }
        return binding.root
    }


    override fun onResume() {
        super.onResume()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        initUi()
    }

    override fun initUi() {
        binding.imgClose.setOnClickListener(this)
        binding.txtCancelQueue.setOnClickListener(this)
        binding.txtCancelAppointment.setOnClickListener(this)


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

            R.id.txtCancelAppointment -> {
                /*   startActivity(Intent(getContext(), DashboardActivity::class.java))
                   finishAffinity(getContext() as Activity)*/
                callApi()
            }

            R.id.txtCancelQueue -> {
                dismiss()
            }

        }
    }


    private fun callApi() {
        if (!isConnected(context, true))
            return
        if (ticketData != null) {
            var beanToSend = CancelTicketRequestBean()
            beanToSend.id = ticketData!!.id!!
            beanToSend.userId = prefManager.getString(PrefConst.PREF_USER_ID)

            var fname = prefManager.getString(PrefConst.PREF_FIRST_NAME)
            var lname = prefManager.getString(PrefConst.PREF_LAST_NAME)
            var cName = getFullName(fname, lname)
            beanToSend.userName = cName

            beanToSend.processingTime = ticketData!!.servingTime
            beanToSend.status = Constant.CANCEL_TICKET_STATUS


            showLoading()

//            if (ticketData!!.isCheckIn!!)
            viewModel.requestCancelTicketApiCall(requireActivity(), beanToSend)
//            else
//                viewModel.requestCancelAppointmentApiCall(context!!,beanToSend)

            setObserver()
        } else showErrorMsg(requireActivity().resources.getString(R.string.no_results_were_found))
    }

    private fun setObserver() {

        viewModel.responseCancelTicketApiCall().observe(this, androidx.lifecycle.Observer {
            hideLoading()
            if (it != null) {
                if (it.status == Constant.STATUS.SUCCESS) {
                    showSuccessMsg(requireContext().resources.getString(R.string.sucess_cancelled))

                    //prefManager.setBoolean(PrefConst.TICKET_BOOKED_HIDE_IMAGE_SLIDER,true)
                    val intent = Intent(activity, BranchActivity::class.java)

                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent)
                    requireActivity().finishAffinity()
                } else {
                    var message =
                        if (!TextUtils.isEmpty(it.message)) it.message!! else resources.getString(R.string.please_try_again)
                    showDialogAsStatus(it.status, message)
                    //  showDialogAsStatus(response.status, response.message)
                }
            } else {
                showSuccessMsg(resources.getString(R.string.in_error))
            }
        })

    }
    /*override fun showLoading() {
        binding.progressLoadmorePeople.visibility=View.VISIBLE
    }
      override fun hideLoading() {
      binding.progressLoadmorePeople.visibility=View.GONE
    }*/
}