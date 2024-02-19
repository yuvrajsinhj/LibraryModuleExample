package com.androhub.networkmodule.utils.frombuilder.dynamicFormBuilder


import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.OpenableColumns
import android.text.*
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.util.Log
import android.util.TypedValue
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.androhub.networkmodule.R
import com.androhub.networkmodule.base.BaseActivityMain
import com.androhub.networkmodule.databinding.ActivityGenerateFormBinding
import com.androhub.networkmodule.databinding.DialogOpenDocumentBinding
import com.androhub.networkmodule.databinding.FormButtonsLayoutBinding
import com.androhub.networkmodule.databinding.FormDocumentLayoutBinding
import com.androhub.networkmodule.local.Utility
import com.androhub.networkmodule.mvvm.model.request.AppointmentAddTicketRequestBean
import com.androhub.networkmodule.mvvm.model.request.DeleteImageRequestBean
import com.androhub.networkmodule.mvvm.model.response.getbranchbymerchant.Branch
import com.androhub.networkmodule.mvvm.model.response.getbranchdetails.BranchTime
import com.androhub.networkmodule.mvvm.model.response.getbranchdetails.Data
import com.androhub.networkmodule.mvvm.model.response.getbranchdetails.Service
import com.androhub.networkmodule.mvvm.model.response.getbranchdetails.ServiceForm
import com.androhub.networkmodule.mvvm.ui.fragment.CustomerBookingTicketAppointBottomSheetFragment
import com.androhub.networkmodule.mvvm.viewmodel.CustomerBookingTicketViewModel
import com.androhub.networkmodule.utils.Constant
import com.androhub.networkmodule.utils.ImageDisplayUitls
import com.androhub.networkmodule.utils.UtilsForm
import com.androhub.networkmodule.utils.UtilsForm.Companion.getCurrentDate
import com.androhub.networkmodule.utils.UtilsForm.Companion.getCustomColorStateList
import com.androhub.networkmodule.utils.UtilsForm.Companion.getDateStringToShow
import com.androhub.networkmodule.utils.UtilsForm.Companion.setMerginToviews
import com.androhub.networkmodule.utils.UtilsForm.Companion.setMerginToviewsTemp
import com.androhub.networkmodule.utils.dialog.ShowDialog
import com.androhub.networkmodule.utils.frombuilder.*
import com.androhub.networkmodule.utils.imagePickerUtils.setLocalImage
import com.androhub.networkmodule.utils.setSafeOnClickListener
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.jetbrains.anko.collections.forEachWithIndex
import org.jetbrains.anko.dimen
import java.io.File
import java.util.*


class DynamicGenerateFormActivity : BaseActivityMain() {

    lateinit var binding: ActivityGenerateFormBinding
    var mainResponse: Data? = null
    private lateinit var selectedBean: Service
    lateinit var viewModelLogin: CustomerBookingTicketViewModel
    private var branchBean: Branch? = null
    var isRequired: Boolean = false

    companion object {
        var submitPropertyArrayJsonTemp: JsonArray? = null


        private const val GALLERY_IMAGE_REQ_CODE = 102
        private const val CAMERA_IMAGE_REQ_CODE = 103
        val PICK_PDF_FILE = 104
        var clickOnButton: Boolean = false
        var customerBookingTicketAppointBottomSheetFragment: CustomerBookingTicketAppointBottomSheetFragment? =
            null
    }


    var formComponent: FormComponentTemp? = null

    private var merchantID = ""
    private var bankTimeArray = ArrayList<BranchTime>()
    var fromNearBy: Boolean = false
    var nearByCurrentBranchDistance: Double = -1.0
    var nearByBranchList = ArrayList<Branch>()
    var nearByCurrentBranchMinutes: Double = -1.0
    var formDetail: ServiceForm.FormId? = null
    val textColor = Color.parseColor("#000000")
    lateinit var language: String
    lateinit var viewModel: CustomerBookingTicketViewModel

    var beanToSend = AppointmentAddTicketRequestBean()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGenerateFormBinding.inflate(layoutInflater)
        setContentView(binding.root)
        submitPropertyArrayJsonTemp = JsonArray()
        formDetail = ServiceForm.FormId()
        viewModel = ViewModelProvider(this).get(CustomerBookingTicketViewModel::class.java)
        customerBookingTicketAppointBottomSheetFragment =
            CustomerBookingTicketAppointBottomSheetFragment()
        viewModelLogin = ViewModelProvider(this).get(CustomerBookingTicketViewModel::class.java)

        if (intent.hasExtra(Constant.INTENT_EXTRA.APPOINTMENT_BEAN)) {
            beanToSend =
                intent.getSerializableExtra(Constant.INTENT_EXTRA.APPOINTMENT_BEAN) as AppointmentAddTicketRequestBean
        }

        selectedBean = intent.getSerializableExtra(Constant.INTENT_EXTRA.SERVICE_BEAN) as Service
        mainResponse = intent.getSerializableExtra(Constant.INTENT_EXTRA.MAIN_RESPONSE) as Data
        if (intent.hasExtra(Constant.INTENT_EXTRA.BRANCH_BEAN)) {
            branchBean = intent.getSerializableExtra(Constant.INTENT_EXTRA.BRANCH_BEAN) as Branch?
        }
        if (intent.hasExtra(Constant.INTENT_EXTRA.MERCHANT_ID)) {
            merchantID = intent.getStringExtra(Constant.INTENT_EXTRA.MERCHANT_ID)!!
        }
        if (intent.hasExtra(Constant.INTENT_EXTRA.BANK_TIME_ARRAY)) {
            bankTimeArray =
                intent.getSerializableExtra(Constant.INTENT_EXTRA.BANK_TIME_ARRAY) as ArrayList<BranchTime>
        }
        if (intent.hasExtra(Constant.INTENT_EXTRA.FROM_NEAR_BY)) {
            fromNearBy = intent.getBooleanExtra(Constant.INTENT_EXTRA.FROM_NEAR_BY, false)
        }
        if (intent.hasExtra(Constant.INTENT_EXTRA.SELECTED_BRANCH_DISTANCE)) {
            nearByCurrentBranchDistance =
                intent.getDoubleExtra(Constant.INTENT_EXTRA.SELECTED_BRANCH_DISTANCE, -1.0)
        }

        if (intent.hasExtra(Constant.INTENT_EXTRA.NEAR_BY_BRANCH_LIST)) {
            nearByBranchList =
                (intent.getSerializableExtra(Constant.INTENT_EXTRA.NEAR_BY_BRANCH_LIST) as ArrayList<Branch>?)!!

            if (intent.hasExtra(Constant.INTENT_EXTRA.SELECTED_BRANCH_MINUTES)) {
                nearByCurrentBranchMinutes =
                    intent.getDoubleExtra(Constant.INTENT_EXTRA.SELECTED_BRANCH_MINUTES, -1.0)
            }

        }
        language = prefManager.language



        binding.imgToolbarLeft.setOnClickListener {
            finish()
        }
        setObserver()

        if (language == Constant.LANGUAGE.AREBIC) {
            binding.tvFormDescription.text = selectedBean.form?.formId?.arabicTitle
            binding.tvFormDescriptionMore.text = selectedBean.form?.formId?.arabicDescription
        } else {
            binding.tvFormDescription.text = selectedBean.form?.formId?.title
            binding.tvFormDescriptionMore.text = selectedBean.form?.formId?.description
        }

        binding.tvBankName.text = mainResponse?.merchantName
        binding.tvMerchantName.text = mainResponse?.branchName

        ImageDisplayUitls.displayImage(
            mainResponse!!.logoURL,
            this@DynamicGenerateFormActivity,
            binding.ivThumbDetails, mainResponse!!.merchantName
        )
        val list = selectedBean.form?.formId?.formDetails?.map {
            Gson().fromJson(it, JsonObject::class.java)
        }

        Log.e("eeeee", list.toString())
        list?.let {
            populateForm(it.toString())
        }


    }
//


    override fun initUi() {

    }

    override fun getContext(): Context? {
        return this
    }

    override fun getLayoutId(): View? {
        return binding.root
    }

    private fun populateForm(json: String) {
        try {
            formComponent = Gson().fromJson(json, FormComponentTemp::class.java)

            var viewId = 1
            binding.miniAppFormContainer.visibility = View.VISIBLE

            //TODO:- GENERATE FORM LAYOUT
            formComponent?.let {
                it.forEachWithIndex { index, component ->

                    if (component.required && !isRequired) {
                        isRequired = true
                    }

                    val relativeLayout = LinearLayout(this)
                    relativeLayout.setPadding(5, 10, 5, 10)
                    val layoutParams = LinearLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                    )
//                    layoutParams.setMargins(3, 3, 3, 3)
                    relativeLayout.setBackgroundResource(R.drawable.card_bg)
                    relativeLayout.layoutParams = layoutParams
                    relativeLayout.orientation = LinearLayout.VERTICAL
                    relativeLayout.addView(createHeaderViewTwo(component))
                    when (component.type!!.value) {
                        WidgetItemsTemp.TEXT.label -> relativeLayout.addView(
                            createEditableTextWithLabel(component, viewId++, index)
                        )

                        WidgetItemsTemp.TEXTAREA.label -> relativeLayout.addView(
                            createEditableTextWithLabel2(component, viewId++, index)
                        )

                        WidgetItemsTemp.TEXTNUMBER.label -> relativeLayout.addView(
                            createEditableTextWithNumber(component, viewId++, index)
                        )

                        WidgetItemsTemp.RADIO_GROUP.label -> relativeLayout.addView(
                            createRadioGroup(
                                component,
                                viewId++, index
                            )
                        )

                        WidgetItemsTemp.SELECT.label -> relativeLayout.addView(
                            createSpinner(
                                component,
                                viewId++, index
                            )
                        )

                        WidgetItemsTemp.DATE.label -> relativeLayout.addView(
                            createDatePicker(
                                component, index
                            )
                        )

                        WidgetItemsTemp.CHECKBOX_GROUP.label -> relativeLayout.addView(
                            createCheckBoxGroupUtil(component, viewId++, index)
                        )

                        WidgetItemsTemp.FILES.label -> relativeLayout.addView(
                            createImagePicker(
                                component, index
                            )
                        )

                    }
//                    relativeLayout.addView(addSubmitButtonLayout())

                    binding.miniAppFormContainer.addView(relativeLayout)


                }

            }
            if (!isRequired) {
                binding.btnSkip.visibility = View.VISIBLE
            } else {
                binding.btnSkip.visibility = View.GONE
            }
            binding.btnSkip.setOnClickListener {
                try {
                    var formComponentTemp: FormComponentTemp? = formComponent
                    formComponentTemp?.let {
                        it.forEachWithIndex { index, component ->
                            component.answer = ""
                            component.fileName = ""
                        }
                    }



                    formDetail?.formDetails = formComponentTemp
                    formDetail!!.id = selectedBean.form!!.formId!!.id
                    formDetail!!.createdAt = selectedBean.form!!.formId!!.createdAt
                    formDetail!!.updatedAt = selectedBean.form!!.formId!!.updatedAt
                    formDetail!!.title = selectedBean.form!!.formId!!.title
                    formDetail!!.arabicTitle = selectedBean.form!!.formId!!.arabicTitle
                    formDetail!!.description = selectedBean.form!!.formId!!.description
                    formDetail!!.arabicDescription = selectedBean.form!!.formId!!.arabicDescription
                    formDetail!!.createdBy = selectedBean.form!!.formId!!.createdBy
                    formDetail!!.updatedBy = selectedBean.form!!.formId!!.updatedBy
                    formDetail!!.isDeleted = selectedBean.form!!.formId!!.isDeleted
                    formDetail!!.merchantID = selectedBean.form!!.formId!!.merchantID


                    if (!beanToSend.branchId.isNullOrEmpty()) {
                        callApi(2, "")
                    } else {
                        gotoNext()
                    }
                } catch (e: Exception) {

                }
            }
            addSubmitButtonLayout()
        } catch (e: Exception) {
            Log.e("eeee", e.toString())
        }

    }

    private fun showAddNoteDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val inflater =
            this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding: DialogOpenDocumentBinding = DialogOpenDocumentBinding.inflate(inflater)

        binding.tvCamera.setOnClickListener {
            closeKeyboard()
            pickCameraImage()
            dialog.dismiss()
        }
        binding.tvDocument.setOnClickListener {
            closeKeyboard()
            pickPdfDocument()
            dialog.dismiss()
        }
        binding.tvGallary.setOnClickListener {
            closeKeyboard()
            pickGalleryImage()
            dialog.dismiss()
        }


        (this as Activity).window.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE or
                    WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
        )
        dialog.setContentView(binding.root)
        val window = dialog.window
        window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        window.setGravity(Gravity.CENTER)
        dialog.show()

    }

    private fun createHeaderViewTwo(componentItem: FormComponentItemTemp): TextView {
        val txtHeader = TextView(this)
        if (language == Constant.LANGUAGE.AREBIC) {

            componentItem.questionArabic?.let {
                txtHeader.text = UtilsForm.fromHtml(it)
            }
        } else {
            componentItem.question?.let {
                txtHeader.text = UtilsForm.fromHtml(it)
            }
        }

        val params: LinearLayout.LayoutParams =
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

        params.setMargins(20, 0, 20, 0)
        txtHeader.layoutParams = params

        txtHeader.setTextColor(textColor)
        txtHeader.setTypeface(null, Typeface.BOLD)
        txtHeader.setTextSize(
            TypedValue.COMPLEX_UNIT_PX,
            this.resources.getDimension(dimen(10))
        );
        txtHeader.setPadding(25, 15, 25, 15)
        txtHeader.gravity = Gravity.START
        return txtHeader
    }

    private fun createDatePicker(component: FormComponentItemTemp, index: Int): RelativeLayout {
        val relativeLayout = RelativeLayout(this)
        relativeLayout.setPadding(5, 10, 5, 0)
        val layoutParams = LinearLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(40, 10, 40, 20)
        if (language == Constant.LANGUAGE.AREBIC) {
            layoutParams.layoutDirection = View.LAYOUT_DIRECTION_RTL
        }
        relativeLayout.layoutParams = layoutParams


        val layoutParams1 = LinearLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams1.gravity = Gravity.CENTER


        val txtDate = TextView(this)
        if (language == Constant.LANGUAGE.AREBIC) {
            txtDate.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.date_form, 0)
        } else {
            txtDate.setCompoundDrawablesWithIntrinsicBounds(R.drawable.date_form, 0, 0, 0)
        }

        txtDate.compoundDrawablePadding = 30
        txtDate.setTextColor(ContextCompat.getColor(this, R.color.edittext_selector))

//        txtDate.gravity = Gravity.CENTER
        setMerginToviewsTemp(
            txtDate, 20,
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.MATCH_PARENT
        )
        if (component.type!!.value!!.equals(WidgetItemsTemp.TEXTAREA.label)) txtDate.gravity =
            Gravity.NO_GRAVITY
        txtDate.setPadding(20, 30, 20, 30)
        txtDate.setBackgroundResource(R.drawable.edit_text_background_form)


        txtDate.layoutParams = layoutParams1
        txtDate.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
        relativeLayout.addView(txtDate)

        relativeLayout.setOnClickListener {
            closeKeyboard()
            val calendar: Calendar = GregorianCalendar()
            val dateString = getCurrentDate()
            dateString?.let {
                calendar.time = it
                val datePickerDialog = DatePickerDialog(
                    this@DynamicGenerateFormActivity,
                    { view, year, month, dayOfMonth ->
                        val selectedDate = Calendar.getInstance()
                        selectedDate[year, month] = dayOfMonth
                        val datee = getDateStringToShow(selectedDate.time, "dd-MM-yyyy")

                        txtDate.text = Utility.convertToArabic(datee)

                        component.answer = txtDate.text.toString()

                    }, calendar[Calendar.YEAR], calendar[Calendar.MONTH],
                    calendar[Calendar.DAY_OF_MONTH]
                )
                datePickerDialog.show()
            }
        }
        return relativeLayout
    }

    var buttonViewBindingList = ArrayList<FormDocumentLayoutBinding>()
    var clickedItem = 0
    var clickedItemDelete = 0
    var clickedView = 0
    private fun createImagePicker(component: FormComponentItemTemp, index: Int): LinearLayout {
        val linearLayout = LinearLayout(this)
        val layoutParams = LinearLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        linearLayout.layoutParams = layoutParams
        val layoutInflater = LayoutInflater.from(this)
        val buttonViewBinding: FormDocumentLayoutBinding =
            FormDocumentLayoutBinding.inflate(layoutInflater)

        buttonViewBindingList.add(buttonViewBinding)
        buttonViewBinding.llDocument.layoutParams = layoutParams
        buttonViewBinding.tvTitleDoc.text = resources.getString(R.string.add_attachment)
//        if (language == Constant.LANGUAGE.AREBIC) {
        buttonViewBinding.tvNoteAttachment.text = resources.getString(R.string.doc_size)
//        } else {
//            buttonViewBinding.tvNoteAttachment.text = component.note
//        }


        buttonViewBinding.ivImageDoc.setOnClickListener {
            buttonViewBindingList.forEachWithIndex { i, formDocumentLayoutBinding ->
                if (formDocumentLayoutBinding.ivImageDoc == buttonViewBinding.ivImageDoc) {
                    clickedItem = i
                }
            }
            clickedView = index
            closeKeyboard()
            showAddNoteDialog()
        }
        buttonViewBinding.cvBaseView.setOnClickListener {
            buttonViewBindingList.forEachWithIndex { i, formDocumentLayoutBinding ->
                if (formDocumentLayoutBinding.ivImageDoc == buttonViewBinding.ivImageDoc) {
                    clickedItem = i
                }
            }
            clickedView = index
            closeKeyboard()
            showAddNoteDialog()
        }


        buttonViewBinding.ivCloseForm.setOnClickListener {
            clickedItemDelete = index
            buttonViewBinding.cvBaseView.visibility = View.VISIBLE
            buttonViewBinding.cvWithImage.visibility = View.GONE
            buttonViewBinding.cvWithpdf.visibility = View.GONE
            callApi(1, formComponent!![index].answer)
        }
        buttonViewBinding.ivCloseFormpdf.setOnClickListener {
            clickedItemDelete = index
            buttonViewBinding.cvBaseView.visibility = View.VISIBLE
            buttonViewBinding.cvWithImage.visibility = View.GONE
            buttonViewBinding.cvWithpdf.visibility = View.GONE
            callApi(1, formComponent!![index].answer)
        }


        linearLayout.addView(buttonViewBinding.llDocument)


        return linearLayout
    }


    fun callApi(tag: Int = 1, answer: String) {
        if (tag == 1) {
            if (answer.isNotEmpty()) {
                var bean = DeleteImageRequestBean()
                bean.link = listOf(answer)
                showLoading()
                viewModelLogin.requestDeleteImage(this, bean)
                showErrorMsg(resources.getString(R.string.sucess_delete_alert))
            }
        }
        if (tag == 2) {


            if (formDetail != null) {
                beanToSend.formDetail = formDetail
                beanToSend.formId = formDetail!!.id
            }

        }

    }


    fun pickPdfDocument() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/pdf"
        }

        startActivityForResult(intent, PICK_PDF_FILE)
    }

    @Suppress("UNUSED_PARAMETER")
    fun pickGalleryImage() {
        ImagePicker.with(this)
            // Crop Image(User can choose Aspect Ratio)
            .crop()
            // User can only select image from Gallery
            .galleryOnly()

            .galleryMimeTypes( // no gif images at all
                mimeTypes = arrayOf(
                    "image/png",
                    "image/jpg",
                    "image/jpeg"
                )
            )
            // Image resolution will be less than 1080 x 1920
            .compress(1024)
            .maxResultSize(1080, 1920)
            .start(GALLERY_IMAGE_REQ_CODE)
    }

    /**
     * Ref: https://gist.github.com/granoeste/5574148
     */
    @Suppress("UNUSED_PARAMETER")
    fun pickCameraImage() {
        showLoading()
        ImagePicker.with(this)
            // User can only capture image from Camera
            .cameraOnly()
            // Image size will be less than 1024 KB
            // .compress(1024)
            //  Path: /storage/sdcard0/Android/data/package/files
            .saveDir(getExternalFilesDir(null)!!)
            //  Path: /storage/sdcard0/Android/data/package/files/DCIM
            .saveDir(getExternalFilesDir(Environment.DIRECTORY_DCIM)!!)
            //  Path: /storage/sdcard0/Android/data/package/files/Download
            .saveDir(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)!!)
            //  Path: /storage/sdcard0/Android/data/package/files/Pictures
            .saveDir(getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!)
            //  Path: /storage/sdcard0/Android/data/package/files/Pictures/ImagePicker
            .saveDir(File(getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!, "ImagePicker"))
            //  Path: /storage/sdcard0/Android/data/package/files/ImagePicker
            .saveDir(getExternalFilesDir("ImagePicker")!!)
            //  Path: /storage/sdcard0/Android/data/package/cache/ImagePicker
            .saveDir(File(getExternalCacheDir(), "ImagePicker"))
            //  Path: /data/data/package/cache/ImagePicker
            .saveDir(File(getCacheDir(), "ImagePicker"))
            //  Path: /data/data/package/files/ImagePicker
            .saveDir(File(getFilesDir(), "ImagePicker"))
            .compress(1024)
            .maxResultSize(1080, 1920)
            // Below saveDir path will not work, So do not use it
            //  Path: /storage/sdcard0/DCIM
            //  .saveDir(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM))
            //  Path: /storage/sdcard0/Pictures
            //  .saveDir(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES))
            //  Path: /storage/sdcard0/ImagePicker
            //  .saveDir(File(Environment.getExternalStorageDirectory(), "ImagePicker"))

            .start(CAMERA_IMAGE_REQ_CODE)
    }

    var filename = ""
    private fun uploadFile(uri: Uri) {
        hideLoading()
        contentResolver.openInputStream(uri).use {
            val request = RequestBody.create("image/*".toMediaTypeOrNull(), it!!.readBytes())
            filename = uri.path!!
            val cut = filename.lastIndexOf('/')
            if (cut != -1) {
                filename = filename.substring(cut + 1)
            }

            val formUpload: RequestBody =
                RequestBody.create("text/plain".toMediaType(), "formUpload")
            val formId: RequestBody = RequestBody.create(
                "text/plain".toMediaType(),
                selectedBean.form?.formId?.id.toString()
            )

            val filePart = MultipartBody.Part.createFormData(
                "formUpload",
                filename.toString(),
                request
            )
            formComponent!![clickedView].fileName = filename
            try {
                showLoading()
                viewModelLogin.requestUploeadImage(this, formUpload, formId, filePart)
            } catch (e: Exception) { // if something happens to the network

            }
        }
    }

    @SuppressLint("Range")
    private fun uploadPdfFile(uri: Uri) {
//         contentResolver = applicationContext.contentResolver

        lateinit var displayName: String
        val cursor: Cursor? = contentResolver.query(
            uri, null, null, null, null, null
        )
        lateinit var size: String
        var mbSize: Double = 0.0
        cursor?.use {

            if (it.moveToFirst()) {
                displayName = it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                Log.e(TAG, "Display Name: $displayName")
//                filename = displayName
                formComponent!![clickedView].fileName = displayName
                val sizeIndex: Int = it.getColumnIndex(OpenableColumns.SIZE)

                size = if (!it.isNull(sizeIndex)) {
                    it.getString(sizeIndex)
                } else {
                    "Unknown"
                }
                buttonViewBindingList[clickedItem].tvPdfName.text = displayName
                mbSize = size.toDouble() / 1024.0 / 1024.0
                if (mbSize > 3) {
                    showErrorMsg(resources.getString(R.string.doc_size_alert))
                    formComponent!![clickedView].fileName = ""
                    buttonViewBindingList[clickedItem].cvBaseView.visibility = View.VISIBLE
                    buttonViewBindingList[clickedItem].cvWithImage.visibility = View.GONE
                    buttonViewBindingList[clickedItem].cvWithpdf.visibility = View.GONE
                    return
                }
            }
        }
        contentResolver.openInputStream(uri).use {
            val request =
                RequestBody.create("application/pdf".toMediaTypeOrNull(), it!!.readBytes())


            val formUpload: RequestBody =
                RequestBody.create("text/plain".toMediaType(), "formUpload")
            val formId: RequestBody = RequestBody.create(
                "text/plain".toMediaType(),
                selectedBean.form?.formId?.id.toString()
            )

            val filePart = MultipartBody.Part.createFormData(
                "formUpload",
                displayName,
                request
            )

            try {
                if (mbSize > 3) {
                    showErrorMsg(resources.getString(R.string.doc_size))
                    filename = ""
                    return
                }
                showLoading()
                viewModelLogin.requestUploeadImage(this, formUpload, formId, filePart)
            } catch (e: Exception) { // if something happens to the network

            }
        }
    }

    //    override fun onResume() {
//        super.onResume()
//
//    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            // Uri object will not be null for RESULT_OK
            val uri: Uri = data?.data!!
            when (requestCode) {


                GALLERY_IMAGE_REQ_CODE -> {
                    showLoading()


                    buttonViewBindingList[clickedItem].ivImageDocnew.setLocalImage(uri)
                    buttonViewBindingList[clickedItem].cvBaseView.visibility = View.GONE
                    buttonViewBindingList[clickedItem].cvWithpdf.visibility = View.GONE
                    buttonViewBindingList[clickedItem].cvWithImage.visibility = View.VISIBLE
                    uploadFile(uri)
                }

                CAMERA_IMAGE_REQ_CODE -> {
//                    showLoading()
                    buttonViewBindingList[clickedItem].ivImageDocnew.setLocalImage(uri)
                    buttonViewBindingList[clickedItem].cvBaseView.visibility = View.GONE
                    buttonViewBindingList[clickedItem].cvWithpdf.visibility = View.GONE
                    buttonViewBindingList[clickedItem].cvWithImage.visibility = View.VISIBLE
                    uploadFile(uri)
                }

                PICK_PDF_FILE -> {

                    buttonViewBindingList[clickedItem].ivImageDocnew.setLocalImage(uri)
                    buttonViewBindingList[clickedItem].cvBaseView.visibility = View.GONE
                    buttonViewBindingList[clickedItem].cvWithImage.visibility = View.GONE
                    buttonViewBindingList[clickedItem].cvWithpdf.visibility = View.VISIBLE
                    uploadPdfFile(uri)
                }
            }
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            hideLoading()
//            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            hideLoading()
//            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }


    private fun createEditableTextWithLabel(
        component: FormComponentItemTemp,
        viewId: Int, index: Int,
    ): EditText {
//        isLabelNull(component)
        val editText = EditText(this)
        var rows = 1

        setMerginToviews(
            editText, 20,
            LinearLayoutCompat.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        if (component.type!!.value.equals(WidgetItemsTemp.TEXTAREA.label)) editText.gravity =
            Gravity.NO_GRAVITY
        editText.maxLines = 1
        if (language == Constant.LANGUAGE.AREBIC) {
            editText.layoutDirection = View.LAYOUT_DIRECTION_RTL
        }
        editText.setPadding(20, 30, 20, 30)
        editText.setBackgroundResource(R.drawable.edit_text_background_form)
        editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
        editText.id = viewId


        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int, after: Int,
            ) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                component.answer = s.toString()
                formComponent!![index].answer = s.toString()
                Log.e("ComponentValue==", "Edit1==${component.answer}")
            }

            override fun afterTextChanged(s: Editable) {
            }
        })
        editText.setLines(rows)
        return editText
    }

    private fun createEditableTextWithLabel2(
        component: FormComponentItemTemp,
        viewId: Int, index: Int,
    ): EditText {
//        isLabelNull(component)
        val editText = EditText(this)
        var rows = 3

        setMerginToviews(
            editText, 20,
            LinearLayoutCompat.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        if (component.type!!.value.equals(WidgetItemsTemp.TEXTAREA.label)) editText.gravity =
            Gravity.NO_GRAVITY
        if (language == Constant.LANGUAGE.AREBIC) {
            editText.layoutDirection = View.LAYOUT_DIRECTION_RTL
        }
        editText.setPadding(20, 30, 20, 30)
        editText.setBackgroundResource(R.drawable.edit_text_background_form)
        editText.id = viewId
        editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)


        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int, after: Int,
            ) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                component.answer = s.toString()
                formComponent!![index].answer = s.toString()
                Log.e("ComponentValue==", "Edit2==${component.answer}")
            }

            override fun afterTextChanged(s: Editable) {
            }
        })
        editText.setLines(rows)
        return editText
    }


    @SuppressLint("RestrictedApi")
    private fun createRadioGroup(
        component: FormComponentItemTemp,
        viewId: Int,
        index: Int,
    ): RadioGroup {
//        createLabelForViews(component)

        val radioGroup = RadioGroup(this)
        radioGroup.id = View.generateViewId()
        radioGroup.orientation = LinearLayout.VERTICAL
        setMerginToviews(radioGroup)

        component.options.let {
            component.options!!.forEachWithIndex { index, v ->
                val value = v
                val radioButton = AppCompatRadioButton(this@DynamicGenerateFormActivity)

                if (language == Constant.LANGUAGE.AREBIC) {
                    radioButton.layoutDirection = View.LAYOUT_DIRECTION_RTL
                    radioButton.text = value.optionArabic
                } else {
                    radioButton.text = value.option
                }
                radioButton.setButtonDrawable(R.drawable.rb_selected_new);
                radioButton.setOnClickListener {

                    radioButton.setButtonDrawable(R.drawable.rb_selected_new);
                    component.multianswer.clear()
                    component.multianswer.add(component.options!![index])
                    closeKeyboard()
                }
                radioGroup.addView(radioButton)

            }
//            if (isRadioButtonSelected) (radioGroup.getChildAt(selectedItem) as RadioButton).isChecked =
//                true


        }
        return radioGroup
    }

    private fun createSpinner(component: FormComponentItemTemp, id: Int, index: Int): View {
        var selectedIndex = 0
//        createLabelForViews(component)
        val checkBoxContainer = LinearLayout(this)
        checkBoxContainer.id = id
        checkBoxContainer.orientation = LinearLayout.VERTICAL
        val layoutParams: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(40, 30, 40, 40)
        val spinner = Spinner(this)
        spinner.id = id
//        spinner.setBackgroundColor(Color.WHITE)
//        spinner.setBackgroundResource(R.drawable.edit_text_background)
        spinner.layoutParams = layoutParams
        //Spinner data source
        val spinnerDatasource = mutableListOf<Any?>()
        component.options.let {
            for (j in it!!.indices) {
                val value = it[j]
                spinnerDatasource.add(value.option)
                value.selected?.let { selected ->
                    if (selected)
                        selectedIndex = j
                }
            }

            val spinnerAdapter: ArrayAdapter<*> = ArrayAdapter<Any?>(
                applicationContext,
                R.layout.form_spinner_row, spinnerDatasource
            )
            spinner.adapter = spinnerAdapter
            spinner.setSelection(selectedIndex)
            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long,
                ) {
                    component.answer = component.options!![position].option.toString()
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
//                    showSuccessMsg("-------")
                }
            }
        }
        checkBoxContainer.addView(spinner)
        return checkBoxContainer
//      return  formViewCollection.add(FormViewComponent(spinner, component))
    }

    private fun createEditableTextWithNumber(
        component: FormComponentItemTemp,
        viewId: Int, index: Int,
    ): EditText {
//        isLabelNull(component)
        val editText = EditText(this)
        var rows = 1

        setMerginToviews(
            editText, 20,
            LinearLayoutCompat.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        if (component.type!!.value.equals(WidgetItemsTemp.TEXTAREA.label)) editText.gravity =
            Gravity.NO_GRAVITY
        editText.maxLines = 1
        editText.inputType = InputType.TYPE_CLASS_NUMBER
        if (language == Constant.LANGUAGE.AREBIC) {
            editText.layoutDirection = View.LAYOUT_DIRECTION_RTL
        }
        editText.setPadding(20, 30, 20, 30)
        editText.setBackgroundResource(R.drawable.edit_text_background_form)
        editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
        editText.id = viewId


        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int, after: Int,
            ) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                component.answer = s.toString()
                formComponent!![index].answer = s.toString()
                Log.e("ComponentValue==", "Edit1==${component.answer}")
            }

            override fun afterTextChanged(s: Editable) {
            }
        })
        editText.setLines(rows)
        return editText
    }

    @SuppressLint("RestrictedApi")
    private fun createCheckBoxGroupUtil(
        component: FormComponentItemTemp,
        id: Int,
        ii: Int,
    ): View {
        val checkBoxContainer = LinearLayout(this)
        checkBoxContainer.id = id
        checkBoxContainer.orientation = LinearLayout.VERTICAL
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        if (language == Constant.LANGUAGE.AREBIC) {
            checkBoxContainer.layoutDirection = View.LAYOUT_DIRECTION_RTL
        }
        checkBoxContainer.layoutParams = layoutParams
        component.options.let {
            component.options!!.forEachWithIndex { index, v ->
                val value = it
                val checkBox = AppCompatCheckBox(this)
                if (language == Constant.LANGUAGE.AREBIC) {
                    checkBox.layoutDirection = View.LAYOUT_DIRECTION_RTL
                    checkBox.text = value?.get(index)!!.optionArabic
                } else {
                    checkBox.text = value?.get(index)!!.option
                }
                val layoutParams1 = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )


//                layoutParams1.setPadding(10, 8, 10, 8)
                layoutParams1.setMargins(30, 0, 30, 0)
                checkBox.layoutParams = layoutParams1

                checkBox.setOnClickListener {
                    closeKeyboard()
                    if (checkBox.isChecked) {
                        component.multianswer.add(component.options!![index])

                    } else {
                        component.multianswer.remove(component.options!![index])


                    }
                }
                checkBox.supportButtonTintList = getCustomColorStateList(this)
                checkBoxContainer.addView(checkBox)
            }


            return checkBoxContainer
//            }
        }
    }

    private fun closeKeyboard() {
        // Only runs if there is a view that is currently focused
        this.currentFocus?.let { view ->
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }


    private fun labelStringForRequiredField(label: String): SpannableStringBuilder {
        val username = SpannableString(label)
        val description = SpannableStringBuilder()
        username.setSpan(
            RelativeSizeSpan(1.1f), 0, username.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        username.setSpan(
            ForegroundColorSpan(textColor), 0, username.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        description.append(username)
        val commentSpannable = SpannableString(" *")
        commentSpannable.setSpan(
            ForegroundColorSpan(Color.RED), 0,
            commentSpannable.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        commentSpannable.setSpan(
            RelativeSizeSpan(1.0f), 0,
            commentSpannable.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        description.append(commentSpannable)
        return description
    }

    private fun addSubmitButtonLayout() {
        val layoutInflater = LayoutInflater.from(this)
        val buttonViewBinding: FormButtonsLayoutBinding =
            FormButtonsLayoutBinding.inflate(layoutInflater)
        binding.miniAppFormContainer.addView(buttonViewBinding.root)


        buttonViewBinding.btnSubmit.setSafeOnClickListener {
            it.requestFocus()
            try {
                formComponent?.let {
                    it.forEach { component ->

                        when (component.type?.value) {
                            WidgetItemsTemp.TEXT.label -> {
                                if (component.answer.isEmpty() && component.required) {
                                    showErrorMsgForm(component)
                                    return@setSafeOnClickListener
                                }
                            }

                            WidgetItemsTemp.TEXTAREA.label -> {
                                if (component.answer.isEmpty() && component.required) {

                                    showErrorMsgForm(component)
                                    return@setSafeOnClickListener

                                }
                            }

                            WidgetItemsTemp.CHECKBOX_GROUP.label -> {
                                if (component.multianswer.isEmpty() && component.required) {
                                    showErrorMsgForm(component)
                                    return@setSafeOnClickListener
                                }
                            }

                            WidgetItemsTemp.RADIO_GROUP.label -> {
                                if (component.multianswer.isEmpty() && component.required) {
                                    showErrorMsgForm(component)
                                    return@setSafeOnClickListener
                                }
                            }

                            WidgetItemsTemp.DATE.label -> {
                                if (component.answer.isEmpty() && component.required) {
                                    showErrorMsgForm(component)
                                    return@setSafeOnClickListener

                                }
                            }

                            WidgetItemsTemp.FILES.label -> {
//                                component.answer = imageUrl
//                                component.fileName = filename
                                if (component.answer.isEmpty() && component.required) {
                                    showErrorMsgForm(component)
                                    return@setSafeOnClickListener

                                }
                            }

                            WidgetItemsTemp.TEXTNUMBER.label -> {
                                if (component.answer.isEmpty() && component.required) {

                                    showErrorMsgForm(component)
                                    return@setSafeOnClickListener

                                }
                            }

                            WidgetItemsTemp.SELECT.label -> {
                                if (component.answer.isEmpty() && component.required) {
                                    showErrorMsgForm(component)
                                    return@setSafeOnClickListener
                                }
                            }

                        }
                    }

                }



                formDetail?.formDetails = formComponent
                formDetail!!.id = selectedBean.form!!.formId!!.id
                formDetail!!.createdAt = selectedBean.form!!.formId!!.createdAt
                formDetail!!.updatedAt = selectedBean.form!!.formId!!.updatedAt
                formDetail!!.title = selectedBean.form!!.formId!!.title
                formDetail!!.arabicTitle = selectedBean.form!!.formId!!.arabicTitle
                formDetail!!.description = selectedBean.form!!.formId!!.description
                formDetail!!.arabicDescription = selectedBean.form!!.formId!!.arabicDescription
                formDetail!!.createdBy = selectedBean.form!!.formId!!.createdBy
                formDetail!!.updatedBy = selectedBean.form!!.formId!!.updatedBy
                formDetail!!.isDeleted = selectedBean.form!!.formId!!.isDeleted
                formDetail!!.merchantID = selectedBean.form!!.formId!!.merchantID


                if (!beanToSend.branchId.isNullOrEmpty()) {
                    callApi(2, "")
                } else {
                    gotoNext()
                }


            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("eeee111111", e.message.toString())
            }
        }

    }

    private fun showErrorMsgForm(component: FormComponentItemTemp) {
        if (language == Constant.LANGUAGE.AREBIC) {
            showErrorMsg(component.questionArabic + " " + resources.getString(R.string.is_required))
        } else {
            showErrorMsg(component.question + " " + resources.getString(R.string.is_required))
        }
    }

    //    var imageUrl = ""
    private fun setObserver() {

        viewModelLogin.responseUploeadImageApiCall().observe(this, androidx.lifecycle.Observer {
            hideLoading()
            if (it != null) {
//                imageUrl = it.data
                formComponent!![clickedView].answer = it.data
            } else {
                buttonViewBindingList[clickedItem].cvBaseView.visibility = View.VISIBLE
                buttonViewBindingList[clickedItem].cvWithImage.visibility = View.GONE
            }

        })
        viewModelLogin.responseDeleteImage().observe(this, androidx.lifecycle.Observer {
            hideLoading()
            formComponent!![clickedItemDelete].answer = ""
        })


    }


    private fun showRequiredDialog(labelStr: String) {
        ShowDialog.customDialog(
            this,
            "Required",
            labelStr, null
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Respond to the action bar's Up/Home button
//        if (item.itemId == R.id.home) {
//            NavUtils.navigateUpFromSameTask(this)
//            return true
//        }
        return super.onOptionsItemSelected(item)
    }


    private fun gotoNext() {
        if (!mainResponse?.merchantActive!!) {
            showErrorMsg(resources.getString(R.string.inactive_merchant))
            clickOnButton = false
            onBackPressed()
            return

        }
//        selectedBean = service
        hideLoading()




        customerBookingTicketAppointBottomSheetFragment =
            CustomerBookingTicketAppointBottomSheetFragment()


        if (selectedBean != null) {
            //Utils.print("onItemClickMain 4 = "+mLastClickTime)
            if (mainResponse != null) {
                selectedBean.merchantID = merchantID
                selectedBean.merchantName = mainResponse!!.merchantName!!
                selectedBean.branchName = mainResponse!!.branchName!!
                selectedBean.branchName = mainResponse!!.branchName!!
                selectedBean.logoURL = mainResponse!!.logoURL!!
                selectedBean.isOpenBranch = mainResponse!!.isOpenBranch!!


            }
            val bundle = Bundle()
            bundle.putSerializable(Constant.INTENT_EXTRA.SERVICE_BEAN, selectedBean)
            Log.e("merchantId", selectedBean.merchantID)
            bundle.putSerializable(Constant.INTENT_EXTRA.FORM_FILLED_DETAILS_FORM_ID, formDetail)
            bundle.putSerializable(Constant.INTENT_EXTRA.TIME_BEAN, bankTimeArray)
            bundle.putBoolean(Constant.INTENT_EXTRA.FROM_NEAR_BY, fromNearBy)
            bundle.putBoolean(Constant.INTENT_EXTRA.IN_BREAK, mainResponse!!.inBreak!!)
            bundle.putBoolean(Constant.INTENT_EXTRA.IN_GRACETIME, mainResponse!!.inGraceTime!!)
            bundle.putDouble(
                Constant.INTENT_EXTRA.SELECTED_BRANCH_DISTANCE,
                nearByCurrentBranchDistance
            )
            if (nearByBranchList.size > 0) {
                bundle.putSerializable(Constant.INTENT_EXTRA.NEAR_BY_BRANCH_LIST, nearByBranchList)

                bundle.putDouble(
                    Constant.INTENT_EXTRA.SELECTED_BRANCH_MINUTES,
                    nearByCurrentBranchMinutes
                )
            }
            customerBookingTicketAppointBottomSheetFragment!!.arguments = bundle
        }
        customerBookingTicketAppointBottomSheetFragment!!.show(
            supportFragmentManager,
            customerBookingTicketAppointBottomSheetFragment!!.tag
        )

        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up)
//        }


    }

}