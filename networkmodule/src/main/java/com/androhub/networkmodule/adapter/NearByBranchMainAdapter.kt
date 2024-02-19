package com.androhub.networkmodule.adapter

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.RecyclerView
import com.androhub.networkmodule.R
import com.androhub.networkmodule.uc.CustomTextView
import com.androhub.networkmodule.utils.Constant
import com.androhub.networkmodule.utils.Utils.showTimeToDisplay
import org.jetbrains.anko.backgroundResource

class NearByBranchMainAdapter(
    private val mActivity: Activity,
    mArrayList: MutableList<RowModel>,
    branchInterfaceTempCuntry: BranchInterfaceTempCuntry?,
    branchInterfaceTempState: BranchInterfaceTempState?,
    branchInterfaceTempCity: BranchInterfaceTempCity?,
    fromNearBy: Boolean,
    isOpenBranch: Boolean,
    inGraceTime: Boolean,
    inBreak: Boolean,
    preventCustomerBookingOption: String
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var  branchInterfaceTempCuntry: BranchInterfaceTempCuntry? = null
    var branchInterfaceTempState: BranchInterfaceTempState? = null
    var  branchInterfaceTempCity: BranchInterfaceTempCity? = null

    var fromNearBy = false
    var inGraceTime = false
    var inBreak = false
    var isOpenBranch = false
    var mArrayList : MutableList<RowModel>
    var preventCustomerBookingOption: String = ""

    private var actionLock = false


    fun setList(
        mArrayList:  MutableList<RowModel>,
        fromNearBy: Boolean,
        isOpenBranch: Boolean,
        inGraceTime: Boolean,
        inBreak: Boolean
    ) {
        this.fromNearBy = fromNearBy
        this.isOpenBranch = isOpenBranch
        this.inBreak = inBreak
        this.inGraceTime = inGraceTime
        this.mArrayList.clear()
        this.mArrayList.addAll(mArrayList)
        notifyDataSetChanged()
    }

    fun setOpenCloseBranch(isOpenBranch: Boolean) {
        this.isOpenBranch = isOpenBranch
        notifyDataSetChanged()
    }



//    interface BranchInterfaceTemp {
//        fun onItemClickMain2(position: Int, view: View?)
//    }

    interface BranchInterfaceTempCuntry {
        fun onItemClickMainCuntry(position: Int, view: View?)
    }
    interface BranchInterfaceTempState {
        fun onItemClickMainState(position: Int, view: View?)
    }
    interface BranchInterfaceTempCity {
        fun onItemClickMainCity(position: Int, view: View?)
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder  {
        val viewHolder: RecyclerView.ViewHolder = when (viewType) {
            RowModel.COUNTRY -> CountryViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_branch_detail, parent, false))
            RowModel.STATE -> StateViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_branch_detail_service, parent, false))
            RowModel.CITY -> CityViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_branch_detail_sub_service, parent, false))
            else -> CountryViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_branch_detail, parent, false))
        }
        return  viewHolder


    }




    override fun getItemViewType(position: Int): Int {
        return mArrayList[position].type
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val row = mArrayList[position]

        when (row.type) {

            RowModel.COUNTRY -> {
                (holder as CountryViewHolder).nameTv.text = row.parentService.name
                holder.tvSubTitle.text = "" + showTimeToDisplay(mActivity, row.parentService.servingTime)


                if (isOpenBranch || inBreak || inGraceTime || row.parentService.type==Constant.ACTION_APPOINTMENT) {
                    holder.btnClosed.visibility = View.GONE
                    holder.btnMore.visibility = View.VISIBLE
                    if (row.parentService.child == null || row.parentService.child!!.size == 0) {
                     if (preventCustomerBookingOption.equals(Constant.PREVENT_CUSTOMER_BOOKING.PREVENT) && row.parentService.type!=Constant.ACTION_APPOINTMENT){
                         holder.btnMore.visibility = View.INVISIBLE
                     }else{
                         holder.btnMore.visibility = View.VISIBLE
                     }
                        holder.iv_service.visibility = View.GONE

                    }
                    else {

                            holder.btnMore.visibility = View.GONE
                            holder.iv_service.visibility = View.VISIBLE

                        if (row.isExpanded) {
                            holder.iv_service.backgroundResource = R.drawable.ic_up_arrow

                        } else {
                            holder.iv_service.backgroundResource = R.drawable.ic_down_arrow

                        }

                        if (holder.iv_service.visibility == View.VISIBLE){
                            holder.ll_branch.setOnClickListener {
                                if (!actionLock) {
                                    actionLock = true
                                    if (row.isExpanded) {
                                        row.isExpanded = false
                                        collapse(position)
                                    } else {
                                        row.isExpanded = true
                                        expand(position)
                                    }
                                }
                            }
                        }


                    }

                }
                else {
                    if (row.parentService.child == null || row.parentService.child!!.size == 0) {
                        holder.btnClosed.visibility = View.VISIBLE
                        holder.btnMore.visibility = View.GONE
                        holder.iv_service.visibility = View.GONE
                    }else{
                        holder.btnClosed.visibility = View.GONE
                        holder.btnMore.visibility = View.GONE
                        holder.iv_service.visibility = View.VISIBLE
                        if (row.isExpanded) {
                            holder.iv_service.backgroundResource = R.drawable.ic_up_arrow

                        } else {
                            holder.iv_service.backgroundResource = R.drawable.ic_down_arrow

                        }

                        holder.ll_branch.setOnClickListener {
                            if (!actionLock) {
                                actionLock = true
                                if (row.isExpanded) {
                                    row.isExpanded = false
                                    collapse(position)
                                } else {
                                    row.isExpanded = true
                                    expand(position)
                                }
                            }
                        }
                    }

                }

                if (holder.btnMore.visibility==View.VISIBLE)
                {
                    holder.ll_branch.setOnClickListener { v ->
                        Log.e("Clicked","adsad")
                        if (branchInterfaceTempCuntry != null) branchInterfaceTempCuntry!!.onItemClickMainCuntry(position, v)
                    }
                }

                holder.btnMore.setOnClickListener { v ->

                    Log.e("Clicked","adsad")
                    if (branchInterfaceTempCuntry != null) branchInterfaceTempCuntry!!.onItemClickMainCuntry(position, v)
                }
            }
            RowModel.STATE -> {
                (holder as StateViewHolder).nameTv.text = row.susbParetntService.name
                holder.tvSubTitle.text = "" + showTimeToDisplay(mActivity, row.susbParetntService.servingTime)

                if (isOpenBranch || inBreak || inGraceTime || row.susbParetntService.type==Constant.ACTION_APPOINTMENT) {
                    holder.btnClosed.visibility = View.GONE
                    holder.btnMore.visibility = View.VISIBLE

                    if (row.susbParetntService.child2 == null || row.susbParetntService.child2!!.isEmpty()) {
                        holder.iv_service.visibility = View.GONE

                        if (preventCustomerBookingOption.equals(Constant.PREVENT_CUSTOMER_BOOKING.PREVENT) && row.susbParetntService.type!=Constant.ACTION_APPOINTMENT){
                            holder.btnMore.visibility = View.INVISIBLE
                        }else{
                            holder.btnMore.visibility = View.VISIBLE
                        }

                    }
                    else {
                        holder.iv_service.visibility = View.VISIBLE
                        holder.btnMore.visibility = View.GONE

                        if (row.isExpanded) {
                            holder.iv_service.backgroundResource = R.drawable.ic_up_arrow

                        } else {
                            holder.iv_service.backgroundResource = R.drawable.ic_down_arrow

                        }


                    }
                } else {
//                    holder.btnClosed.visibility = View.VISIBLE
//                    holder.btnMore.visibility = View.GONE



                    if (row.susbParetntService.child2 == null || row.susbParetntService.child2!!.size == 0) {
                        holder.btnClosed.visibility = View.VISIBLE
                        holder.btnMore.visibility = View.GONE
                        holder.iv_service.visibility = View.GONE

                    }else{
                        holder.btnClosed.visibility = View.GONE
                        holder.btnMore.visibility = View.GONE
                        holder.iv_service.visibility = View.VISIBLE
                        if (row.isExpanded) {
                            holder.iv_service.backgroundResource = R.drawable.ic_up_arrow

                        } else {
                            holder.iv_service.backgroundResource = R.drawable.ic_down_arrow

                        }
                        holder.ll_branch.setOnClickListener {
                            if (!actionLock) {
                                actionLock = true
                                if (row.isExpanded) {
                                    row.isExpanded = false
                                    collapse(position)
                                } else {
                                    row.isExpanded = true
                                    expand(position)
                                }
                            }
                        }
                    }
                }



                if (holder.iv_service.visibility == View.VISIBLE){
                    holder.ll_branch.setOnClickListener {
                        if (!actionLock) {
                            actionLock = true
                            if (row.isExpanded) {
                                row.isExpanded = false
                                collapse(position)
                            } else {
                                row.isExpanded = true
                                expand(position)
                            }
                        }
                    }
                }

                holder.btnMore.setOnClickListener { v ->
                    if (branchInterfaceTempCuntry != null) branchInterfaceTempState!!.onItemClickMainState(position, v)

                }
                if (holder.btnMore.visibility==View.VISIBLE)
                {
                    holder.ll_branch.setOnClickListener { v ->
                        if (branchInterfaceTempCuntry != null) branchInterfaceTempState!!.onItemClickMainState(position, v)

                    }
                }

            }

            RowModel.CITY -> {
                (holder as CityViewHolder).nameTv.text = row.subChieldService.name
                holder.tvSubTitle.text = "" + showTimeToDisplay(mActivity, row.subChieldService.servingTime)

                if (isOpenBranch || inBreak || inGraceTime || row.subChieldService.type==Constant.ACTION_APPOINTMENT) {
                    holder.btnClosed.visibility = View.GONE
                    if (preventCustomerBookingOption.equals(Constant.PREVENT_CUSTOMER_BOOKING.PREVENT) && row.subChieldService.type!=Constant.ACTION_APPOINTMENT){
                        holder.btnMore.visibility = View.INVISIBLE
                    }else{
                        holder.btnMore.visibility = View.VISIBLE
                    }
                } else {
                    holder.btnClosed.visibility = View.VISIBLE
                    holder.btnMore.visibility = View.GONE
                }


                holder.btnMore.setOnClickListener { v ->
                    if (branchInterfaceTempCuntry != null) branchInterfaceTempCity!!.onItemClickMainCity(position, v)
                }

                if (holder.btnMore.visibility==View.VISIBLE)
                {
                    holder.ll_branch.setOnClickListener { v ->
                        if (branchInterfaceTempCuntry != null) branchInterfaceTempCity!!.onItemClickMainCity(position, v)
                    }

                }
            }

        }


    }

    override fun getItemCount(): Int {
        return mArrayList.size
    }


  inner  class CountryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val ll_branch: LinearLayoutCompat = itemView.findViewById(R.id.ll_branch) as LinearLayoutCompat
        internal val nameTv: CustomTextView = itemView.findViewById(R.id.tvTitle) as CustomTextView
        internal val iv_service: ImageView = itemView.findViewById(R.id.iv_service) as ImageView
        internal val btnMore : CustomTextView = itemView.findViewById(R.id.btnMore) as CustomTextView
        internal val btnClosed : CustomTextView = itemView.findViewById(R.id.btnClosed) as CustomTextView
        internal val tvSubTitle : CustomTextView = itemView.findViewById(R.id.tvSubTitle) as CustomTextView


      init {
            if (isOpenBranch || inBreak || inGraceTime) {
                btnClosed.visibility = View.GONE
                btnMore.visibility = View.VISIBLE
            } else {
                btnClosed.visibility = View.VISIBLE
                btnMore.visibility = View.GONE
            }
        }
    }
  inner  class StateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val ll_branch: LinearLayoutCompat = itemView.findViewById(R.id.ll_branch) as LinearLayoutCompat
        internal val nameTv: CustomTextView = itemView.findViewById(R.id.tvTitle) as CustomTextView
        internal val btnMore : CustomTextView = itemView.findViewById(R.id.btnMore) as CustomTextView
        internal val iv_service: ImageView = itemView.findViewById(R.id.iv_service) as ImageView
      internal val btnClosed : CustomTextView = itemView.findViewById(R.id.btnClosed) as CustomTextView
      internal val tvSubTitle : CustomTextView = itemView.findViewById(R.id.tvSubTitle) as CustomTextView

      init {
          if (isOpenBranch || inBreak || inGraceTime) {
              btnClosed.visibility = View.GONE

              btnMore.visibility = View.VISIBLE
          } else {
              btnClosed.visibility = View.VISIBLE
              btnMore.visibility = View.GONE
          }
      }
    }

   inner class CityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val nameTv: CustomTextView = itemView.findViewById(R.id.tvTitle) as CustomTextView
        internal val btnMore : CustomTextView = itemView.findViewById(R.id.btnMore) as CustomTextView
       internal val ll_branch: LinearLayoutCompat = itemView.findViewById(R.id.ll_branch) as LinearLayoutCompat
       internal val btnClosed : CustomTextView = itemView.findViewById(R.id.btnClosed) as CustomTextView
       internal val tvSubTitle : CustomTextView = itemView.findViewById(R.id.tvSubTitle) as CustomTextView

       init {
           if (isOpenBranch || inBreak || inGraceTime) {
               btnClosed.visibility = View.GONE
               if (preventCustomerBookingOption.equals(Constant.PREVENT_CUSTOMER_BOOKING.PREVENT)){
                   btnMore.visibility = View.GONE
               }else{
                   btnMore.visibility = View.VISIBLE
               }

           } else {
               btnClosed.visibility = View.VISIBLE
               btnMore.visibility = View.GONE
           }
       }
    }


    init {
        this.mArrayList = mArrayList
        this.branchInterfaceTempCuntry = branchInterfaceTempCuntry
        this.branchInterfaceTempCity = branchInterfaceTempCity
        this.branchInterfaceTempState = branchInterfaceTempState
        this.fromNearBy = fromNearBy
        this.inBreak = inBreak
        this.preventCustomerBookingOption = preventCustomerBookingOption
        this.inGraceTime = inGraceTime
        this.isOpenBranch = isOpenBranch
    }
    fun expand(position: Int) {

        var nextPosition = position

        val row = mArrayList[position]

        when (row.type) {

            RowModel.COUNTRY -> {

                /**
                 * add element just below of clicked row
                 */
                for (state in row.parentService.child!!) {
                    mArrayList.add(++nextPosition, RowModel(RowModel.STATE, state))
                }

                notifyDataSetChanged()
            }

            RowModel.STATE -> {

                /**
                 * add element just below of clicked row
                 */
                for (city in row.susbParetntService.child2!!) {
                    mArrayList.add(++nextPosition, RowModel(RowModel.CITY, city))
                }

                notifyDataSetChanged()
            }
        }

        actionLock = false
    }

    fun collapse(position: Int) {
        val row = mArrayList[position]
        val nextPosition = position + 1

        when (row.type) {

            RowModel.COUNTRY -> {

                /**
                 * remove element from below until it ends or find another node of same type
                 */
                outerloop@ while (true) {
                    if (nextPosition == mArrayList.size || mArrayList.get(nextPosition).type === RowModel.COUNTRY) {
                        break@outerloop
                    }

                    mArrayList.removeAt(nextPosition)
                }

                notifyDataSetChanged()
            }

            RowModel.STATE -> {

                /**
                 * remove element from below until it ends or find another node of same type or find another parent node
                 */
                outerloop@ while (true) {
                    if (nextPosition == mArrayList.size || mArrayList.get(nextPosition).type === RowModel.COUNTRY || mArrayList.get(nextPosition).type === RowModel.STATE
                    ) {
                        break@outerloop
                    }

                    mArrayList.removeAt(nextPosition)
                }

                notifyDataSetChanged()
            }
        }

        actionLock = false
    }


}