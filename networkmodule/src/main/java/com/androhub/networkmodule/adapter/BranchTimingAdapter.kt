package com.androhub.networkmodule.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.androhub.networkmodule.R
import com.androhub.networkmodule.databinding.ItemBranchTimingBinding
import com.androhub.networkmodule.mvvm.model.response.getbranchdetails.Data
import com.androhub.networkmodule.mvvm.ui.BranchDetailActivity
import com.androhub.networkmodule.utils.Utils



class BranchTimingAdapter() :
    RecyclerView.Adapter<BranchTimingAdapter.ViewHolder>() {
//

    lateinit var context: Context
    lateinit var data: Data

    constructor(branchDetailActivity: BranchDetailActivity, it: Data) : this() {
        this.data = it
        this.context = branchDetailActivity
    }

    constructor(branchDetailActivity: FragmentActivity?, it: Data) : this() {
        this.data = it
        if (branchDetailActivity != null) {
            this.context = branchDetailActivity
        }
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        val binding = DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(context),
            R.layout.item_branch_timing, parent, false
        ) as ItemBranchTimingBinding
        return ViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        var timeZone = data.timeZone
//        var bean : Data.DisplayTime = data.displayTime[position]
        var bean: Data.WorkTime = data.workTime!![position]

//        var startTime = Utils.getLocalTime(startTime, mainResponse!!.timeZone)
        val sbtvDay = StringBuilder()
        val sbtvTime = StringBuilder()

        Log.e("Days full", bean.day)
        if (Utils.isArLang()) {

            val str = bean.day
            val delim = " "
            val list = str.split(delim)
            for (day in list) {
                if (day.trim() == "Monday") {
                    sbtvDay.append(context.resources.getString(R.string.monday))
                } else if (day.trim() == "Tuesday") {
                    sbtvDay.append(context.resources.getString(R.string.tuesday))
                } else if (day.trim() == "Wednesday") {
                    sbtvDay.append(context.resources.getString(R.string.wednesday))
                } else if (day.trim() == "Thursday") {
                    sbtvDay.append(context.resources.getString(R.string.thursday))
                } else if (day.trim() == "Friday") {
                    sbtvDay.append(context.resources.getString(R.string.friday))
                } else if (day.trim() == "Saturday") {
                    sbtvDay.append(context.resources.getString(R.string.satruday))
                } else if (day.trim() == "Sunday") {
                    sbtvDay.append(context.resources.getString(R.string.sunday))
                }
                if (day == data.todayTime?.day) {
                    holder.tvDay.setTextColor(ContextCompat.getColor(context, R.color.font_blue))
                    holder.tvDay.text = context.resources.getString(R.string.today)
                } else {
                    holder.tvDay.setTextColor(ContextCompat.getColor(context, R.color.font_grey))
                    holder.tvDay.text = sbtvDay
                }

            }

        } else {
            sbtvDay.append(bean.day)
            val today: String = sbtvDay.toString()
            Log.e("adad", "$today---${data.todayTime?.day}")
            if (today == data.todayTime?.day) {
                holder.tvDay.text = context.resources.getString(R.string.today)
                holder.tvDay.setTextColor(ContextCompat.getColor(context, R.color.font_blue))
            } else {
                holder.tvDay.setTextColor(ContextCompat.getColor(context, R.color.font_grey))
                holder.tvDay.text = sbtvDay
            }


        }

        try {
            if (bean.shifts!!.isNotEmpty()) {
//                sbtvDay.append(bean.name)

                if (bean.isWorking) {


                    for (s in bean.shifts!!.indices) {

                        sbtvTime.append(Utils.getLocalTime(bean.shifts!![s].start, timeZone))
                            .append("-").append(
                            Utils.getLocalTime(
                                bean.shifts!![s].end, timeZone
                            )
                        )
                        if (bean.shifts!!.size > 1 && s != bean.shifts!!.size - 1) {
                            sbtvTime.append("\n")
                            sbtvTime.append("\n")
                        }
                    }

                    holder.tvTime.text = sbtvTime

                    holder.tvTime.setTextColor(ContextCompat.getColor(context, R.color.font_black))


                } else {

                    holder.tvDay.text = sbtvDay

                    holder.tvTime.text = context.resources.getString(R.string.closed)
                    holder.tvTime.setTextColor(ContextCompat.getColor(context, R.color.font_red))
                    holder.tvDay.setTextColor(ContextCompat.getColor(context, R.color.font_red))

                }

//                Log.e("Days195",sbtvDay.toString())

            } else {

                holder.tvDay.text = sbtvDay

                holder.tvTime.text = context.resources.getString(R.string.closed)
                holder.tvDay.setTextColor(ContextCompat.getColor(context, R.color.font_red))
                holder.tvTime.setTextColor(ContextCompat.getColor(context, R.color.font_red))
            }
        } catch (e: Exception) {
            Log.e("adad", "---")
        }


    }

    override fun getItemCount(): Int {
        return data.workTime!!.size
    }

    class ViewHolder(itemView: ItemBranchTimingBinding) : RecyclerView.ViewHolder(itemView.root) {
        val tvDay = itemView.tvDay
        val tvTime = itemView.tvTime
    }

}