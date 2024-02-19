package com.androhub.networkmodule.utils.imagePickerUtils

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.databinding.DataBindingUtil
import com.androhub.networkmodule.R
import com.androhub.networkmodule.base.BaseActivityMain
import com.androhub.networkmodule.databinding.ActivityImageViewZoomBinding
import com.androhub.networkmodule.utils.Constant
import com.androhub.networkmodule.utils.ImageDisplayUitls
import com.androhub.networkmodule.utils.Utils.hideSoftInput

class ImageViewZoomActivity : BaseActivityMain() {

    lateinit var binding: ActivityImageViewZoomBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_image_view_zoom)
        binding.lifecycleOwner = this

        binding.imgBackIcon.setOnClickListener { onBackPressed()
            hideSoftInput(this)
        }

        if (intent.extras != null) {
            var imagePath = intent.getStringExtra(Constant.INTENT_EXTRA.IMAGE_PATH)
            var imageRes = intent.getIntExtra(Constant.INTENT_EXTRA.IMAGE_RES, 0)

            if (!TextUtils.isEmpty(imagePath)) {
                ImageDisplayUitls.displayImageFull(imagePath,this@ImageViewZoomActivity,binding.ivZoom)
            } else if (imageRes > 0) {
                binding.ivZoom.setImageResource(imageRes)
            } else {
                finish()
            }
        } else {
            finish()
        }


    }

    override fun initUi() {
        TODO("Not yet implemented")
    }

    override fun getContext(): Context? {
       return this
    }

    override fun getLayoutId(): View? {
       return binding.root
    }
}
