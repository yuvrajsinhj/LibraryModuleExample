package com.androhub.networkmodule.utils.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import com.androhub.networkmodule.MyApplication

class ConnectivityReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, arg1: Intent) {
//        val cm = context
//            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//        val activeNetwork = cm.activeNetworkInfo
//        val isConnected = (activeNetwork != null
//
//                && activeNetwork.isConnectedOrConnecting)

        if (connectivityReceiverListener != null) {
            connectivityReceiverListener!!.onNetworkConnectionChanged( isLocationEnable(context))
        }
    }

    interface ConnectivityReceiverListener {
        fun onNetworkConnectionChanged(isConnected: Boolean)
    }

    companion object {
        @JvmField
        var connectivityReceiverListener: ConnectivityReceiverListener? = null
        val isConnected: Boolean
            get() {
                val lm =  MyApplication.getAppContext().applicationContext!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                var gps_enabled = false
                var network_enabled = false

                try {
                    gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
                } catch (ex: Exception) {
                }

                try {
                    network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
                } catch (ex: java.lang.Exception) {
                }

                if (!gps_enabled && !network_enabled)
                {
                    return false
                }
                return true

            }
    }

    private fun isLocationEnable(context: Context):Boolean
    {
        val lm = context!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var gps_enabled = false
        var network_enabled = false
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
        } catch (ex: Exception) {
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        } catch (ex: java.lang.Exception) {
        }

        if (!gps_enabled && !network_enabled)
        {
            return false
        }
        return true
    }
}