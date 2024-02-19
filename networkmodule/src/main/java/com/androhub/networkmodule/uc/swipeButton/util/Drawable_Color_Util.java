package com.androhub.networkmodule.uc.swipeButton.util;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;

/**
 * Created by Gratus on 02/10/18.
 */

public class Drawable_Color_Util {
    public static void setDrawableColor(Drawable drawable, int color) {
        drawable.mutate();
        if (drawable instanceof ShapeDrawable) {
            ShapeDrawable shapeDrawable = (ShapeDrawable) drawable;
            shapeDrawable.getPaint().setColor(color);
        } else if (drawable instanceof GradientDrawable) {
            GradientDrawable gradientDrawable = (GradientDrawable) drawable;
            gradientDrawable.setColor(color);
        } else if (drawable instanceof ColorDrawable) {
            ColorDrawable colorDrawable = (ColorDrawable) drawable;
            colorDrawable.setColor(color);
        }

    }

    public static void setDrawableStroke(Drawable drawable, int color) {
        if (drawable instanceof GradientDrawable) {
            GradientDrawable gradientDrawable = (GradientDrawable) drawable;
            gradientDrawable.mutate();
            gradientDrawable.setStroke(4, color);
        }
    }
}
