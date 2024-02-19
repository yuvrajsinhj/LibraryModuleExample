package com.androhub.networkmodule.adapter

import androidx.annotation.IntDef
import com.androhub.networkmodule.mvvm.model.response.getbranchdetails.Data

/**
 * Created by Shahbaz Hashmi on 26/04/19.
 */
class RowModel {

    companion object {

        @IntDef(COUNTRY, STATE, CITY)
        @Retention(AnnotationRetention.SOURCE)
        annotation class RowType

        const val COUNTRY = 1
        const val STATE = 2
        const val CITY = 3
    }

    @RowType
    var type : Int

    lateinit var parentService : Data.ParentService

    lateinit var susbParetntService : Data.Child

    lateinit var subChieldService : Data.Child1

    var isExpanded : Boolean

    constructor(@RowType type : Int, country : Data.ParentService, isExpanded : Boolean = false){
        this.type = type
        this.parentService = country
        this.isExpanded = isExpanded
    }

    constructor(@RowType type : Int, state :  Data.Child, isExpanded : Boolean = false){
        this.type = type
        this.susbParetntService = state
        this.isExpanded = isExpanded
    }

    constructor(@RowType type : Int, city:  Data.Child1, isExpanded : Boolean = false){
        this.type = type
        this.subChieldService = city
        this.isExpanded = isExpanded
    }



}