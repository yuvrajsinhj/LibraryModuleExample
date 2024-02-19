package com.androhub.networkmodule.mvvm.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.androhub.networkmodule.R
import com.androhub.networkmodule.adapter.BranchTimingAdapter
import com.androhub.networkmodule.databinding.OpenhoursBottomsheetFragmentBinding
import com.androhub.networkmodule.mvvm.model.response.getbranchdetails.Data
import com.androhub.networkmodule.utils.Constant


class OpenHoursBottomsheetFragment : BaseFragmentBottomSheet(), View.OnClickListener {

    lateinit var binding: OpenhoursBottomsheetFragmentBinding
    var branchTimingAdapter: BranchTimingAdapter? = null
    var mainResponse: Data? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        binding = DataBindingUtil.inflate(inflater,
            R.layout.openhours_bottomsheet_fragment,
            container,
            false)

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initUi()
    }

    override fun getLayoutId(): View {
        return binding.root
    }

    override fun initUi() {

        binding.ivClose.setOnClickListener(this)
        binding.btnClose.setOnClickListener(this)
        binding.rvBranchTiming.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)


        if (arguments != null) {
            try {
                if (requireArguments().containsKey(Constant.INTENT_EXTRA.MAIN_RESPONSE)){
                    mainResponse = (requireArguments().getSerializable(Constant.INTENT_EXTRA.MAIN_RESPONSE)) as Data
                    setBranchTimingData()
                }
            } catch (e: Exception) {
            }
        }
    }

    override fun getContext(): Context? {
        return activity
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnClose->{
                dismiss()
            }
            R.id.ivClose->{
                dismiss()
            }
        }

    }

    private fun setBranchTimingData() {
        branchTimingAdapter = mainResponse?.let {
            BranchTimingAdapter(activity, it)
        }
        binding.rvBranchTiming?.adapter = branchTimingAdapter
    }
}