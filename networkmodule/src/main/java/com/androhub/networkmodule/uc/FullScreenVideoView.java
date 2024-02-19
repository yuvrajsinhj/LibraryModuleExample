package com.androhub.networkmodule.uc;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

public class FullScreenVideoView extends VideoView {
    public FullScreenVideoView(Context context) {
        super(context);
    }
 
    public FullScreenVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
 
    public FullScreenVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
 
    public FullScreenVideoView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
 
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Recalculate the height
        int width = getDefaultSize(0, widthMeasureSpec);
        int height = getDefaultSize(0, heightMeasureSpec);
        setMeasuredDimension(width, height);
    }
}
