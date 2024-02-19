package com.androhub.networkmodule.utils.frombuilder.dynamicFormBuilder

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class FormComponentItemTemp: Serializable {
    @SerializedName("type")
    var type: ValueTemp? = null

    @SerializedName("id")
    var id: String? = null

    @SerializedName("question")
    var question: String? = null

    @SerializedName("questionArabic")
    var questionArabic: String? = null

    @SerializedName("required")
    var required: Boolean= false


    @SerializedName("note")
    var note: String? = null

    @SerializedName("options")
    var options: ArrayList<OptionTemp>?= null

    @SerializedName("answer")
    var answer: String=""
    @SerializedName("fileName")
    var fileName: String=""

    @SerializedName("multianswer")
    var multianswer: ArrayList<OptionTemp> =arrayListOf()


}


class OptionTemp: Serializable {
    @SerializedName("id")
    var id: String? = null
    @SerializedName("option")
    var option: String? = null
    @SerializedName("optionArabic")
    var optionArabic: String? = null
    val selected: Boolean=false
}


 class ValueTemp:Serializable {
     @SerializedName("label")
    var label: String? = null

    @SerializedName("value")
    var value: String? = null
}
