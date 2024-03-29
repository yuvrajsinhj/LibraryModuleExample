package com.androhub.networkmodule.local;

import static android.content.Context.MODE_PRIVATE;
import static android.os.Build.VERSION_CODES.JELLY_BEAN_MR1;
import static android.os.Build.VERSION_CODES.N;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.LocaleList;

import androidx.annotation.RequiresApi;


import com.androhub.networkmodule.AppManager;
import com.androhub.networkmodule.MyApplication;
import com.androhub.networkmodule.PrefConst;
import com.androhub.networkmodule.PrefManager;
import com.androhub.networkmodule.utils.Constant;
import com.androhub.networkmodule.utils.Utils;

import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;

/**
 * @author
 */
public class LocaleManager {

    public static final String LANGUAGE_ENGLISH = "en";
    public static final String LANGUAGE_ARABIC = "ar";
    private static final String LANGUAGE_KEY = PrefConst.CURRENT_LANGUAGE;

  //  private final SharedPreferences prefs;
    PrefManager prefManager;
    AppManager appManager;
    private SharedPreferences.Editor editor;
    private SharedPreferences sharedPreferences;
    public LocaleManager(Context mContext) {



        if (appManager == null) {
            appManager = AppManager.getInstance(mContext);
        }
        if (sharedPreferences == null) {
            sharedPreferences = mContext.getSharedPreferences(PrefConst.PREF_FILE, MODE_PRIVATE);
        }
        if (editor == null) {
            editor = sharedPreferences.edit();
        }
        if (prefManager == null) {
            prefManager = new PrefManager(sharedPreferences, editor);
        }else   prefManager = appManager.getPrefManager();
        appManager.setPrefManager(prefManager);
        //prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public Context setLocale(Context c) {
        return updateResources(c, getLanguage());
    }

    public Context setLocale(Context c,String lang) {
        return updateResources(c, lang);
    }

    public Context   setNewLocale(Context c, String language) {
        persistLanguage(language);
        return updateResources(c, language);
    }

    /*public String getLanguage() {
        return prefs.getString(LANGUAGE_KEY, LANGUAGE_ENGLISH);
    }*/

    public String getLanguage() {
        // in second param add default language
        if (prefManager!=null){
        String lang=prefManager.getLanguage();
        Utils.print("Lang","---->---"+lang);
        return lang;
        }else return Constant.LANGUAGE.ENGLISH;
   //     return prefs.getString(PrefConst.CURRENT_LANGUAGE,LANGUAGE_ENGLISH);
    }

    @SuppressLint("ApplySharedPref")
    private void persistLanguage(String language) {
        // use commit() instead of apply(), because sometimes we kill the application process
        // immediately that prevents apply() from finishing
        //prefs.edit().putString(PrefConst.CURRENT_LANGUAGE, language).commit();
        MyApplication.getAppManager().getPrefManager().setString(PrefConst.CURRENT_LANGUAGE,language.substring(0,2));
    }

    private Context updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources res = context.getResources();
        Configuration config = new Configuration(res.getConfiguration());
        if (Utility.isAtLeastVersion(N)) {
            setLocaleForApi24(config, locale);
            context = context.createConfigurationContext(config);
        } else if (Utility.isAtLeastVersion(JELLY_BEAN_MR1)) {
            config.setLocale(locale);
            context = context.createConfigurationContext(config);
        } else {
            config.locale = locale;
            res.updateConfiguration(config, res.getDisplayMetrics());
        }
        return context;
    }

    @RequiresApi(api = N)
    private void setLocaleForApi24(Configuration config, Locale target) {
        Set<Locale> set = new LinkedHashSet<>();
        // bring the target locale to the front of the list
        set.add(target);

        LocaleList all = LocaleList.getDefault();
        for (int i = 0; i < all.size(); i++) {
            // append other locales supported by the user
            set.add(all.get(i));
        }

        Locale[] locales = set.toArray(new Locale[0]);
        config.setLocales(new LocaleList(locales));
    }

    public static Locale getLocale(Resources res) {
        Configuration config = res.getConfiguration();
        return Utility.isAtLeastVersion(N) ? config.getLocales().get(0) : config.locale;
    }
}