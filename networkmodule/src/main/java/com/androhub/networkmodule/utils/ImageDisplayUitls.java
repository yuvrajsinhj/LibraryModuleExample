package com.androhub.networkmodule.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.androhub.networkmodule.R;
import com.androhub.networkmodule.utils.avatargenlib.AvatarGenerator;


/**
 * The type Image display uitls.
 */
public class ImageDisplayUitls {

    /**
     * Display image.
     *
     * @param image     the image
     * @param activity  the activity
     * @param imageView the image view
     */
    public static String displayServiceImage(String image, Context activity, ImageView imageView) {
        if (image == null || image.isEmpty())
            return "";
        Glide.with(activity)
                .load(image.contains("file://") ? image : image)
//                .error(R.drawable.horizontal_back_image)
//                .placeholder(R.drawable.horizontal_back_image)
//                .apply(new RequestOptions().override(200, 200))
//                .centerCrop()
                .into(imageView);

        return image;

    }
    public static String displayImage(String image, Context activity, ImageView imageView) {
        if (image == null || image.isEmpty())
            return "";
        Glide.with(activity)
                .load(image.contains("file://") ? image : image)
//                .error(R.drawable.horizontal_back_image)
//                .placeholder(R.drawable.horizontal_back_image)
                 .apply(new RequestOptions().override(200, 200))
                .centerCrop()
                .into(imageView);

        return image;

    }
    static int RECTANGLE = 0;



public static String displayImageSlider(String image, Context activity, ImageView imageView,String name) {
    if (image==null) image= "";
    BitmapDrawable nameImage= AvatarGenerator.avatarImage(
            activity,
            200,
            RECTANGLE,
            name
    );
    Glide.with(activity)
            .load(image.contains("file://") ? image : image)
            .error(nameImage)
            .placeholder(nameImage)
//            .apply(new RequestOptions().override(400, 200))
            .apply(new RequestOptions().override(400, 400))
            .centerCrop()
            .into(imageView);

    return image;

}
    public static String displayImage(String image, Context activity, ImageView imageView,String name) {
        if (image==null) image= "";
        BitmapDrawable nameImage= AvatarGenerator.avatarImage(
                activity,
                200,
                RECTANGLE,
                name
        );
        Glide.with(activity)
                .load(image.contains("file://") ? image : image)
                .error(nameImage)
                .placeholder(nameImage)
                 .apply(new RequestOptions().override(200, 200))
                .centerCrop()
                .into(imageView);

        return image;

    }
    public static String displayImageFull(String image, Context activity, ImageView imageView) {
        if (image == null || image.isEmpty())
            return "";
        Glide.with(activity)
                .load(image.contains("file://") ? image : image)
                .error(R.color.btn_default)
                .placeholder(R.color.btn_default)
                //.apply(new RequestOptions().override(200, 200))
                //.fit()
                .into(imageView);

        return image;

    }


    public static void displayImageWithDrawable(Integer image, Activity activity
            , ImageView imageView) {

        Glide.with(activity)
                .load(image)
                .error(R.drawable.ic_check_green)
                .placeholder(R.drawable.ic_check_green)
                .into(imageView);
    }

}
