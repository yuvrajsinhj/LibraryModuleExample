package com.androhub.networkmodule.local;

import static android.os.Build.VERSION_CODES.P;

import android.os.Build;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;


import com.androhub.networkmodule.utils.Utils;

import java.lang.reflect.Method;

public class Utility {



    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html){
        if(html == null){
            // return an empty spannable if the html is null
            return new SpannableString("");
        }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // FROM_HTML_MODE_LEGACY is the behaviour that was used for versions below android N
            // we are using this flag to give a consistent behaviour
            return Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(html);
        }
    }



    public static void bypassHiddenApiRestrictions() {
        // http://weishu.me/2019/03/16/another-free-reflection-above-android-p/
        if (!isAtLeastVersion(P)) return;
        try {
            Method forName = Class.class.getDeclaredMethod("forName", String.class);
            Method getDeclaredMethod = Class.class.getDeclaredMethod("getDeclaredMethod",
                    String.class, Class[].class);

            Class<?> vmRuntimeClass = (Class<?>) forName.invoke(null, "dalvik.system.VMRuntime");
            Method getRuntime = (Method) getDeclaredMethod.invoke(vmRuntimeClass, "getRuntime",
                    null);
            Method setHiddenApiExemptions = (Method) getDeclaredMethod.invoke(vmRuntimeClass,
                    "setHiddenApiExemptions", new Class[]{ String[].class });
            Object sVmRuntime = getRuntime.invoke(null);

            setHiddenApiExemptions.invoke(sVmRuntime, new Object[]{ new String[]{ "L" } });
        } catch (Throwable e) {
//            Log.e(TAG, "Reflect bootstrap failed:", e);
        }
    }



    public static boolean isAtLeastVersion(int version) {
        return Build.VERSION.SDK_INT >= version;
    }



    public static String convertToArabic(String value)
    {
        if (Utils.isArLang()){
        String newValue =   (((((((((((value+"").replaceAll("1", "١")).replaceAll("2", "٢")).replaceAll("3", "٣")).replaceAll("4", "٤")).replaceAll("5", "٥")).replaceAll("6", "٦")).replaceAll("7", "٧")).replaceAll("8", "٨")).replaceAll("9", "٩")).replaceAll("0", "٠"));
        return newValue;
        }
        else return value;
    }


}