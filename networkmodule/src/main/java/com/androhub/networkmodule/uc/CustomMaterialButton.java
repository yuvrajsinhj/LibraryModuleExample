package com.androhub.networkmodule.uc;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.androhub.networkmodule.R;
import com.google.android.material.button.MaterialButton;


public class CustomMaterialButton extends MaterialButton {

    private int fontTag = 0;
    public static final int REGULAR = 1;
    public static final int BOLD = 2;
    public static final int SEMI_BOLD = 3;
    public static final int MEDIUM = 4;
    public static final int LIGHT = 5;
    public static final int MEDIUM_ITALIC = 6;
    public static final int SEMI_BOLD_ITALIC = 7;
    public CustomMaterialButton(Context context) {
        super(context);
        init(context, null);
    }

    public CustomMaterialButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CustomMaterialButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            final TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.CustomView);
            fontTag = attributes.getInteger(R.styleable.CustomView_custom_font, fontTag);
            setFont(context, fontTag,this);
            attributes.recycle();
        }
    }

    public void setFont(Context context, int tag, MaterialButton textView) {
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "font/neutrifpro_regular.ttf");
        switch (tag) {
            case REGULAR:
                typeface = Typeface.createFromAsset(context.getAssets(), "font/neutrifpro_regular.ttf");
                break;
            case BOLD:
                typeface = Typeface.createFromAsset(context.getAssets(), "font/neutrifpro_bold.ttf");
                break;
            case SEMI_BOLD:
                typeface = Typeface.createFromAsset(context.getAssets(), "font/neutrifpro_semibold.ttf");
                break;
            case MEDIUM:
                typeface = Typeface.createFromAsset(context.getAssets(), "font/neutrifpro_semibold.ttf");
                break;
            case LIGHT:
                typeface = Typeface.createFromAsset(context.getAssets(), "font/Poppins-Light.ttf");
                break;
            case MEDIUM_ITALIC:
                typeface = Typeface.createFromAsset(context.getAssets(), "font/Poppins-MediumItalic.ttf");
                break;
            case SEMI_BOLD_ITALIC:
                typeface = Typeface.createFromAsset(context.getAssets(), "font/Poppins-MediumItalic.ttf");
                break;
        }
        textView.setTypeface(typeface);

    }

}
