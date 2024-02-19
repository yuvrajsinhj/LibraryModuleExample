package com.androhub.networkmodule.utils.avatargenlib

import android.content.Context
import android.graphics.*
import android.graphics.Bitmap.Config.ARGB_8888
import android.graphics.drawable.BitmapDrawable
import android.text.TextPaint
import android.text.TextUtils
import java.util.*

/**
 * Created by Korir on 1/21/20.
 */
class AvatarGenerator {
    companion object {
        private lateinit var uiContext: Context
        private var texSize = 0F
        @JvmStatic
        fun avatarImage(context: Context, size: Int, shape: Int, name: String): BitmapDrawable {
            return avatarImageGenerate(context, size, shape, name, AvatarConstants.COLOR500)
        }


        fun avatarImage(
                context: Context,
                size: Int,
                shape: Int,
                name: String,
                colorModel: Int
        ): BitmapDrawable {
            return avatarImageGenerate(context, size, shape, name, colorModel)
        }

        private fun avatarImageGenerate(
                context: Context,
                size: Int,
                shape: Int,
                name2: String,
                colorModel: Int
        ): BitmapDrawable {
            var name=""
            if (!TextUtils.isEmpty(name2))
                  name=name2.trim()
            uiContext = context

            texSize = calTextSize(size)
            val label = firstCharacter(name)
            val textPaint = textPainter()
            val painter = painter()
            painter.isAntiAlias = true



            val areaRect = Rect(0, 0, size, size)

            if (shape == 0) {
                val firstLetter = firstCharacter(name)
                val r = firstLetter[0]
             //   painter.color = RandomColors(colorModel).getColor()
                painter.color =Color.parseColor("#606C74")
            } else {
                painter.color = Color.TRANSPARENT
            }

            val bitmap = Bitmap.createBitmap(size, size, ARGB_8888)
            val canvas = Canvas(bitmap)
          //  canvas.drawRect(areaRect, painter)
            canvas.drawRoundRect( RectF(areaRect),15F,15F, painter)

            //reset painter
            if (shape == 0) {
                painter.color = Color.TRANSPARENT
            } else {
                val firstLetter = firstCharacter(name)
                val r = firstLetter[0]
                //painter.color = RandomColors(colorModel).getColor()
                painter.color =Color.parseColor("#606C74")
            }

            val bounds = RectF(areaRect)
            bounds.right = textPaint.measureText(label, 0, 1)
            bounds.bottom = textPaint.descent() - textPaint.ascent()

            bounds.left += (areaRect.width() - bounds.right) / 2.0f
            bounds.top += (areaRect.height() - bounds.bottom) / 2.0f
            bounds.top += 13f

            canvas.drawCircle(size.toFloat() / 2, size.toFloat() / 2, size.toFloat() / 2, painter)
            canvas.drawText(label, bounds.left, bounds.top - textPaint.ascent(), textPaint)
            return BitmapDrawable(uiContext.resources, bitmap)

        }


        private fun firstCharacter(name: String?): String {
            var result="M"
            if (!TextUtils.isEmpty(name))
            {
                val f:String?= name?.firstOrNull()?.toString()
                if (!TextUtils.isEmpty(f))
                    result= f!!.toUpperCase(Locale.ROOT)
            }
             return result
        }

        private fun textPainter(): TextPaint {
            val textPaint = TextPaint()
            textPaint.isAntiAlias = true
            var size = texSize * uiContext.resources.displayMetrics.scaledDensity
            textPaint.textSize = size-70
            var typeFace= Typeface.createFromAsset(uiContext.getAssets(), "font/neutrifpro_regular.ttf")
            textPaint.typeface=typeFace
             textPaint.color = Color.WHITE
            return textPaint
        }

        private fun painter(): Paint {
            return Paint()
        }

        private fun calTextSize(size: Int): Float {
            return (size / 3.125).toFloat()
        }
    }
}