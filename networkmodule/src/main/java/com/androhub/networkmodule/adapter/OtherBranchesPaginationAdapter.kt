package com.androhub.networkmodule.adapter

import android.content.Context
import android.os.Handler
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.androhub.networkmodule.R
import com.androhub.networkmodule.databinding.ItemBranchBinding
import com.androhub.networkmodule.databinding.RawItemProgressBinding
import com.androhub.networkmodule.local.Utility
import com.androhub.networkmodule.mvvm.model.response.getbranchbymerchant.Branch
import com.androhub.networkmodule.utils.Constant

class OtherBranchesPaginationAdapter() :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var preventCustomerBookingOption: String? = null

    lateinit var context: Context
    lateinit var onClickListener: View.OnClickListener
    lateinit var list: ArrayList<Branch?>

    companion object {
        var VIEWTYPE_USER = 1
        var VIEWTYPE_LOADING = 2
    }

    constructor(
        context: Context,
        list: ArrayList<Branch?>,
        onClickListener: View.OnClickListener,
        preventCustomerBookingOption: String?
    ) : this() {
        this.list = list
        this.onClickListener = onClickListener
        this.context = context
        this.preventCustomerBookingOption = preventCustomerBookingOption
    }




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == VIEWTYPE_USER) {
            val binding = DataBindingUtil.inflate<ViewDataBinding>(
                LayoutInflater.from(context),
                R.layout.item_branch, parent, false
            ) as ItemBranchBinding
            return BranchItemtHolder(binding)

        } else {
            val progressBinding = DataBindingUtil.inflate<ViewDataBinding>(
                LayoutInflater.from(context),
                R.layout.raw_item_progress, parent, false
            ) as RawItemProgressBinding
            return BranchProgressBarHolder(progressBinding)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun getItem(pos: Int): Branch {
        return list.get(pos)!!
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is BranchItemtHolder) {
            val bean = list.get(position)!!
//            if (lang)

            if (preventCustomerBookingOption.equals(Constant.PREVENT_CUSTOMER_BOOKING.PREVENT)){

                holder.binding.llMain.setOnClickListener(onClickListener)
                holder.binding.llMain.tag = position
//                holder.binding.btnMore.setOnClickListener(onClickListener)

//                holder.binding.btnMore.tag = position

                holder.binding.tvTitle.text = bean.name!!
                holder.binding.tvSubTitle.text = Utility.convertToArabic(bean.distanceStr+ context.resources.getString(R.string.kimi))
                holder.binding.tvSubTitle.visibility =
                    if (!TextUtils.isEmpty(bean.distanceStr)) View.VISIBLE else View.GONE
                holder.binding.pgbKm.visibility =
                    if (!TextUtils.isEmpty(bean.distanceStr)) View.GONE else View.VISIBLE
                holder.binding.btnMore.visibility =View.GONE


            }else{
                holder.binding.btnMore.visibility =View.VISIBLE
                holder.binding.llMain.setOnClickListener(onClickListener)
                holder.binding.llMain.tag = position
                holder.binding.btnMore.setOnClickListener(onClickListener)

                holder.binding.btnMore.tag = position

                holder.binding.tvTitle.text = bean.name!!
                holder.binding.tvSubTitle.text = Utility.convertToArabic(bean.distanceStr+ context.resources.getString(R.string.kimi))
                holder.binding.tvSubTitle.visibility =
                    if (!TextUtils.isEmpty(bean.distanceStr)) View.VISIBLE else View.GONE
                holder.binding.pgbKm.visibility =
                    if (!TextUtils.isEmpty(bean.distanceStr)) View.GONE else View.VISIBLE
                holder.binding.btnMore.text =
                    if (!TextUtils.isEmpty(bean.distanceStr)) context.resources.getString(R.string.book) else context.resources.getString(
                        R.string.book)


            }

        } else {
            val progressBarHolder = holder as BranchProgressBarHolder
            progressBarHolder.progressBinding.progressLoadmore.visibility = View.VISIBLE
        }
    }


    /**
     * Add loading view.
     */
    fun addLoadingView() {
        Handler().post {
            list.add(null)
            if (list.size > 0) {
                notifyItemInserted(list.size - 1)
            }
        }
    }

    /**
     * Remove loading view.
     */
    fun removeLoadingView() {
        list.removeAt(list.size - 1)
        notifyItemRemoved(list.size)
    }

    override fun getItemViewType(position: Int): Int {
        return if (list.get(position) == null) VIEWTYPE_LOADING else VIEWTYPE_USER
    }


}

class BranchProgressBarHolder(internal var progressBinding: RawItemProgressBinding) :
    RecyclerView.ViewHolder(progressBinding.root)

class BranchItemtHolder(internal var binding: ItemBranchBinding) :
    RecyclerView.ViewHolder(binding.root)
