package com.androhub.networkmodule.utils.imageSlider.IndicatorView.animation.controller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.androhub.networkmodule.utils.imageSlider.IndicatorView.animation.data.Value;
import com.androhub.networkmodule.utils.imageSlider.IndicatorView.animation.type.ColorAnimation;
import com.androhub.networkmodule.utils.imageSlider.IndicatorView.animation.type.DropAnimation;
import com.androhub.networkmodule.utils.imageSlider.IndicatorView.animation.type.FillAnimation;
import com.androhub.networkmodule.utils.imageSlider.IndicatorView.animation.type.ScaleAnimation;
import com.androhub.networkmodule.utils.imageSlider.IndicatorView.animation.type.ScaleDownAnimation;
import com.androhub.networkmodule.utils.imageSlider.IndicatorView.animation.type.SlideAnimation;
import com.androhub.networkmodule.utils.imageSlider.IndicatorView.animation.type.SwapAnimation;
import com.androhub.networkmodule.utils.imageSlider.IndicatorView.animation.type.ThinWormAnimation;
import com.androhub.networkmodule.utils.imageSlider.IndicatorView.animation.type.WormAnimation;


public class ValueController {

    private ColorAnimation colorAnimation;
    private ScaleAnimation scaleAnimation;
    private WormAnimation wormAnimation;
    private SlideAnimation slideAnimation;
    private FillAnimation fillAnimation;
    private ThinWormAnimation thinWormAnimation;
    private DropAnimation dropAnimation;
    private SwapAnimation swapAnimation;
    private ScaleDownAnimation scaleDownAnimation;

    private UpdateListener updateListener;

    public interface UpdateListener {
        void onValueUpdated(@Nullable Value value);
    }

    public ValueController(@Nullable UpdateListener listener) {
        updateListener = listener;
    }

    @NonNull
    public ColorAnimation color() {
        if (colorAnimation == null) {
            colorAnimation = new ColorAnimation(updateListener);
        }

        return colorAnimation;
    }

    @NonNull
    public ScaleAnimation scale() {
        if (scaleAnimation == null) {
            scaleAnimation = new ScaleAnimation(updateListener);
        }

        return scaleAnimation;
    }

    @NonNull
    public WormAnimation worm() {
        if (wormAnimation == null) {
            wormAnimation = new WormAnimation(updateListener);
        }

        return wormAnimation;
    }

    @NonNull
    public SlideAnimation slide() {
        if (slideAnimation == null) {
            slideAnimation = new SlideAnimation(updateListener);
        }

        return slideAnimation;
    }

    @NonNull
    public FillAnimation fill() {
        if (fillAnimation == null) {
            fillAnimation = new FillAnimation(updateListener);
        }

        return fillAnimation;
    }

    @NonNull
    public ThinWormAnimation thinWorm() {
        if (thinWormAnimation == null) {
            thinWormAnimation = new ThinWormAnimation(updateListener);
        }

        return thinWormAnimation;
    }

    @NonNull
    public DropAnimation drop() {
        if (dropAnimation == null) {
            dropAnimation = new DropAnimation(updateListener);
        }

        return dropAnimation;
    }

    @NonNull
    public SwapAnimation swap() {
        if (swapAnimation == null) {
            swapAnimation = new SwapAnimation(updateListener);
        }

        return swapAnimation;
    }

    @NonNull
    public ScaleDownAnimation scaleDown() {
        if (scaleDownAnimation == null) {
            scaleDownAnimation = new ScaleDownAnimation(updateListener);
        }

        return scaleDownAnimation;
    }
}
