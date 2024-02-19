package com.androhub.networkmodule.utils.gpsLocation;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.androhub.networkmodule.MyApplication;
import com.androhub.networkmodule.PrefConst;
import com.androhub.networkmodule.utils.Constant;
import com.androhub.networkmodule.utils.Utils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;


public class GetLocationUpdate
        implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private Location location;
    public GoogleApiClient.OnConnectionFailedListener connectionCallbacksFail;
    public GoogleApiClient.ConnectionCallbacks connectionCallbacks;
    public GoogleApiClient googleApiClient;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    int DISTANCE_INTERVAL = Constant.DISTANCE_INTERVAL_DEFAULT;
    private LocationRequest locationRequest;
    private static final long UPDATE_INTERVAL = 10000, FASTEST_INTERVAL = 10000; // = 10 seconds
    // lists for permissions
    public ArrayList<String> permissionsToRequest;
    public ArrayList<String> permissionsRejected = new ArrayList<>();
    public ArrayList<String> permissions = new ArrayList<>();
    // integer for permissions results request
    public static final int ALL_PERMISSIONS_RESULT = 1011;
    private MyLocationListener locationListener;
    private Activity activity;
    boolean setCancelUpdate = false;

    public void setCancelUpdateCall(boolean flag) {
        setCancelUpdate = flag;
    }

    public GetLocationUpdate(Activity activity, MyLocationListener myLocationListener) {
        this.connectionCallbacksFail = this;
        this.connectionCallbacks = this;
        this.locationListener = myLocationListener;
        this.activity = activity;


/*if (!MyApplication.getAppManager().getPrefManager().getBoolean(PrefConst.ALLOW_LOCATION_PERMISSION)) {
            new PermissionDisclosureDialog(activity, new PermissionDisclosureDialog.OnPDialogClick() {
                @Override
                public void onConfirmClick() {
                    initLocation(true);
                }

                @Override
                public void onCancelClick() {
                    locationListener.onLocationUpdate(null);
                }
            }).show();}*/
        initLocation(false);

    }

    private void initLocation(boolean isNotStartedAlready) {
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        permissionsToRequest = permissionsToRequest(permissions);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionsToRequest.size() > 0) {
                activity.requestPermissions(permissionsToRequest.toArray(
                        new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
            }
        }
        // we build google api client
        googleApiClient = new GoogleApiClient.Builder(activity).
                addApi(LocationServices.API).
                addConnectionCallbacks(connectionCallbacks).
                addOnConnectionFailedListener(connectionCallbacksFail).build();
        if (isNotStartedAlready) {
            onStartLocation();
        }

        new GpsUtils(activity).turnGPSOn(new GpsUtils.onGpsListener() {
            @Override
            public void gpsStatus(boolean isGPSEnable) {
                //  Log.e("onLocationChanged", "gpsStatus : " + isGPSEnable);
            }
        });
    }

    private ArrayList<String> permissionsToRequest(ArrayList<String> wantedPermissions) {
        ArrayList<String> result = new ArrayList<>();
        for (String perm : wantedPermissions) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }
        return result;
    }

    public boolean hasPermission(String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return activity.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }

    public void onStartLocation(int distanceInterval) {
        DISTANCE_INTERVAL = distanceInterval;
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    public void onStartLocation() {

        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    public void onDestroy() {

        MyApplication.getAppManager().getPrefManager().setString(PrefConst.USER_CURRENT_LAT, "");
        MyApplication.getAppManager().getPrefManager().setString(PrefConst.USER_CURRENT_LNG, "");
        Utils.print("TAG", "Location is cleared");

        if (googleApiClient != null && googleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
            googleApiClient.disconnect();
        }
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(activity);

        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(activity, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST);
            } else {
            }
            return false;
        }
        return true;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

        if (location != null) {
//            Utils.print("Latitude : " + location.getLatitude() + "\nLongitude : " + location.getLongitude());

            if (locationListener != null)
                locationListener.onLocationUpdate(location);

        }

        startLocationUpdates();
    }

    private void startLocationUpdates() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);
        locationRequest.setSmallestDisplacement(DISTANCE_INTERVAL);

        if (ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(activity, "You need to enable permissions to display location !", Toast.LENGTH_SHORT).show();
        }
        if (googleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
//            Utils.print("Latitude : " + location.getLatitude() + "\nLongitude : " + location.getLongitude());
            Utils.print("onLocationChanged", "Latitude : " + location.getLatitude() + "- Longitude : " + location.getLongitude());
            MyApplication.getAppManager().getPrefManager().setString(PrefConst.USER_CURRENT_LAT, "" + location.getLatitude());
            MyApplication.getAppManager().getPrefManager().setString(PrefConst.USER_CURRENT_LNG, "" + location.getLongitude());
            MyApplication.getAppManager().getPrefManager().setString(PrefConst.USER_CURRENT_LAT_TEMP, "" + location.getLatitude());
            MyApplication.getAppManager().getPrefManager().setString(PrefConst.USER_CURRENT_LNG_TEMP, "" + location.getLongitude());
            if (locationListener != null && !setCancelUpdate)
                locationListener.onLocationUpdate(location);
        }
    }


}


