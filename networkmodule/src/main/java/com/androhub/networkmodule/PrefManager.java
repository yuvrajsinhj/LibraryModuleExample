package com.androhub.networkmodule;

import android.content.SharedPreferences;
import android.text.TextUtils;

import com.androhub.networkmodule.adapter.RowModel;
import com.androhub.networkmodule.mvvm.model.response.TicketBean;
import com.androhub.networkmodule.mvvm.model.response.getbranchdetails.Data;
import com.androhub.networkmodule.utils.Constant;
import com.androhub.networkmodule.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PrefManager {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public PrefManager(SharedPreferences sharedPreferences, SharedPreferences.Editor editor) {
        this.sharedPreferences = sharedPreferences;
        this.editor = editor;
    }
    public void saveTicketList(ArrayList<TicketBean> list) {
        if (list != null && list.size() > 0) {
            Gson gson = new Gson();
            String strList = gson.toJson(list);
            setString(PrefConst.TICKET_LIST_SLIDER, strList);
            Utils.print("tl saveTicketList=" + list.size());
        } else setString(PrefConst.TICKET_LIST_SLIDER, "");
    }

    public ArrayList<TicketBean> getSavedTicketList() {
        try {
            Gson gson = new Gson();
            String strList = getString(PrefConst.TICKET_LIST_SLIDER);
            if (TextUtils.isEmpty(strList)) return new ArrayList<>();

            Type type = new TypeToken<List<TicketBean>>() {
            }.getType();
            ArrayList<TicketBean> arrayList = gson.fromJson(strList, type);
            Utils.print("tl getSavedTicketList=" + arrayList.size());
            return arrayList;
        } catch (Exception e) {
            saveTicketList(null);
            Gson gson = new Gson();
            String strList = getString(PrefConst.TICKET_LIST_SLIDER);
            if (TextUtils.isEmpty(strList)) return new ArrayList<>();

            Type type = new TypeToken<List<TicketBean>>() {
            }.getType();
            ArrayList<TicketBean> arrayList = gson.fromJson(strList, type);
            Utils.print("tl getSavedTicketList=" + arrayList.size());
            return arrayList;
        }

    }
    public void clearLogs() {
        setString(PrefConst.LOGS_APP, "");
    }

    public String getLogs() {
        return getString(PrefConst.LOGS_APP);
    }

    public void saveLogs(String logs) {
        String oldString = getString(PrefConst.LOGS_APP);
        String newString = oldString + "\n\n" + logs;
        setString(PrefConst.LOGS_APP, newString);
    }

    public void setString(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public String getString(String key) {
        return sharedPreferences.getString(key, "");
    }

    public void saveBranchData(Data list) {
        if (list != null) {
            Gson gson = new Gson();
            String strList = gson.toJson(list);
            setString(PrefConst.BranchDATA, strList);

        } else setString(PrefConst.BranchDATA, "");
    }

    public Data getSavedBranchData() {
        try {
            Gson gson = new Gson();
            String strList = getString(PrefConst.BranchDATA);
            if (TextUtils.isEmpty(strList)) return new Data();

            Type type = new TypeToken<Data>() {
            }.getType();

            return gson.fromJson(strList, type);
        } catch (Exception e) {


            return null;
        }

    }

    public void saveBranchList(List<RowModel> list) {
//    public void saveBranchList(ArrayList<RowModel> list) {
        if (list != null && list.size() > 0) {
            Gson gson = new Gson();
            String strList = gson.toJson(list);
            setString(PrefConst.BranchList, strList);
            Utils.print("tl saveTicketList=" + list.size());
        } else setString(PrefConst.BranchList, "");
    }

    public ArrayList<RowModel> getSavedBranchList() {
        try {
            Gson gson = new Gson();
            String strList = getString(PrefConst.BranchList);
            if (TextUtils.isEmpty(strList)) return new ArrayList<>();

            Type type = new TypeToken<List<RowModel>>() {
            }.getType();
            ArrayList<RowModel> arrayList = gson.fromJson(strList, type);
            Utils.print("tl getSavedTicketList=" + arrayList.size());
            return arrayList;
        } catch (Exception e) {

            return null;
        }

    }



    public String getLanguage() {
       /* String currentLang= sharedPreferences.getString(PrefConst.CURRENT_LANGUAGE, Constant.LANGUAGE.AREBIC);
        //String deviceLang= Locale.getDefault().getLanguage();
        Utils.print("lang currentLang="+currentLang);
        //Utils.print("lang deviceLang="+deviceLang);
        if (!TextUtils.isEmpty(currentLang)) return currentLang;
        //else if (!TextUtils.isEmpty(deviceLang) && deviceLang.equalsIgnoreCase(Constant.LANGUAGE.ENGLISH)) return deviceLang;
        else return Constant.LANGUAGE.AREBIC;*/
        return sharedPreferences.getString(PrefConst.CURRENT_LANGUAGE, Constant.LANGUAGE.ENGLISH);
    }

    public void setInt(String key, int value) {
        editor.putInt(key, value);
        editor.commit();
    }


    public int getInt(String key) {
        return sharedPreferences.getInt(key, -1);
    }

    public void setBoolean(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.commit();
    }

    public boolean getBoolean(String key) {
        return sharedPreferences.getBoolean(key, false);
    }

    public boolean getBoolean(String key, boolean defaultVal) {
        return sharedPreferences.getBoolean(key, defaultVal);
    }

    public void setLong(String key, long value) {
        editor.putLong(key, value);
        editor.commit();
    }

    public long getLong(String key) {
        return sharedPreferences.getLong(key, -1);
    }

    public void removeValue(String key) {
        editor.remove(key);
        editor.commit();
    }

    public void clearAll() {
        editor = sharedPreferences.edit();
        editor.clear().apply();
    }

    public void clearExcept(List<String> keyList) {
        java.util.Map<String, ?> keys = sharedPreferences.getAll();
        for (java.util.Map.Entry<String, ?> entry : keys.entrySet()) {
            if (!keyList.contains(entry.getKey())) {
                removeValue(entry.getKey());
            }
        }
    }
}
