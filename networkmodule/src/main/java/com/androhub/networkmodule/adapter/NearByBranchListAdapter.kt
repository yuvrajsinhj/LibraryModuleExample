package com.androhub.networkmodule.adapter

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.androhub.networkmodule.R
import com.androhub.networkmodule.databinding.ItemBranchBinding
import com.androhub.networkmodule.MyApplication
import com.androhub.networkmodule.PrefConst
import com.androhub.networkmodule.local.Utility
import com.androhub.networkmodule.mvvm.model.response.getbranchbymerchant.Branch
import com.androhub.networkmodule.utils.Constant


class NearByBranchListAdapter() :
    RecyclerView.Adapter<NearByBranchListAdapter.ViewHolder>() {

    lateinit var context: Context
    lateinit var onClickListener: BranchNameInter
    var list = ArrayList<Branch>()
    var sortedList = ArrayList<Branch>()
    var showKm: Boolean = true


    constructor(
        context: Context,
        onClickListener: BranchNameInter,
        list: ArrayList<Branch>
    ) : this() {
        this.list = list
        this.onClickListener = onClickListener
        this.context = context


    }



    interface BranchNameInter {
        fun onItemClickMain(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(context),
            R.layout.item_branch, parent, false
        ) as ItemBranchBinding

        return ViewHolder(binding)

    }

      fun setData(newList: ArrayList<Branch>) {
          this.list=ArrayList()
          this.list.addAll(newList)
          notifyDataSetChanged()

    }
    override fun getItemCount(): Int {
        return list.size
    }

    fun getItem(pos: Int): Branch {
        return list.get(pos)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var bean: Branch = list.get(position)


        holder.btnMore.setOnClickListener {
            onClickListener.onItemClickMain(position)
        }

        holder.llMain.setOnClickListener {
            onClickListener.onItemClickMain(position)
        }

        holder.tvTitle.setText(bean.name)

        var myLatStr =
            MyApplication.getAppManager().getPrefManager().getString(PrefConst.USER_CURRENT_LAT)
        var myLngStr =
            MyApplication.getAppManager().getPrefManager().getString(PrefConst.USER_CURRENT_LNG)


        var branchLat = bean.lat
        var branchLong = bean.long



        if (!showKm) {
            holder.pgbKm.visibility = View.GONE
            holder.tvSubTitle.visibility = View.GONE
        } else if (!TextUtils.isEmpty(bean.distanceStr) && bean.distanceStr.equals(Constant.ERROR_CODE.MAP_ERROR)) {
            holder.pgbKm.visibility = View.GONE
            holder.tvSubTitle.visibility = View.GONE
        } else if (!TextUtils.isEmpty(bean.distanceStr)) {
            holder.tvSubTitle.text = Utility.convertToArabic(bean.distanceStr+ context.resources.getString(R.string.kimi))
            holder.pgbKm.visibility = View.GONE
            holder.tvSubTitle.visibility = View.VISIBLE
        } else if (/*isCallApi &&*/ !TextUtils.isEmpty(myLatStr) && !TextUtils.isEmpty(myLngStr) && branchLat != null && branchLong != null) {

            if (holder.tvSubTitle.visibility==View.GONE)
            holder.pgbKm.visibility = View.VISIBLE
            else
            holder.pgbKm.visibility = View.GONE

        } else {
            holder.pgbKm.visibility = View.GONE
            holder.tvSubTitle.visibility = View.GONE
        }
    }

    class ViewHolder(itemView: ItemBranchBinding) :
        RecyclerView.ViewHolder(itemView.root) {

        val pgbKm = itemView.pgbKm
        val tvTitle = itemView.tvTitle
        val tvSubTitle = itemView.tvSubTitle
        val btnMore = itemView.btnMore
        val llMain = itemView.llMain



    }

}
