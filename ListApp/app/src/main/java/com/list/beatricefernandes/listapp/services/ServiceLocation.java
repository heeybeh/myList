package com.list.beatricefernandes.listapp.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

/** Created by beatrice.fernandes on 31/03/18.
 */

public class ServiceLocation extends Service {

    private static final String TAG = "LocationService";
    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 1000;
    private static final float LOCATION_DISTANCE = 0f;


    public class LocationListener implements android.location.LocationListener {

        Location mLastLocation;

        private LocationListener(String provider) {

            this.mLastLocation = new Location(provider);

        }

        @Override
        public void onLocationChanged(Location location) {

            this.mLastLocation.set(location);

            SharedPreferences.Editor editor = getSharedPreferences("Location", MODE_PRIVATE).edit();
            editor.putString("Latitude", String.valueOf(location.getLatitude()));
            editor.putString("Longitude", String.valueOf(location.getLongitude()));
            editor.apply();

        }

        @Override
        public void onProviderDisabled(String provider) {

            Log.d(TAG, "onProviderDisabled: " + provider);

        }

        @Override
        public void onProviderEnabled(String provider) {

            Log.d(TAG, "onProviderEnabled: " + provider);

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

            Log.d(TAG, "onStatusChanged: " + provider);

        }
    }

    LocationListener[] mLocationListeners = new LocationListener[] {
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };


    @Override
    public IBinder onBind(Intent arg0)
    {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        super.onStartCommand(intent, flags, startId);
        return START_STICKY;

    }

    @Override
    public void onCreate() {

        initializeLocationManager();

        try {

            this.mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE, mLocationListeners[1]);

        } catch (java.lang.SecurityException ex) {

            Log.d(TAG, "fail to request location update, ignore", ex);

        } catch (IllegalArgumentException ex) {

            Log.d(TAG, "network provider does not exist, " + ex.getMessage());

        }

        try {

            this.mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE, mLocationListeners[0]);

        } catch (java.lang.SecurityException ex) {

            Log.d(TAG, "fail to request location update, ignore", ex);

        } catch (IllegalArgumentException ex) {

            Log.d(TAG, "gps provider does not exist " + ex.getMessage());

        }

    }

    @Override
    public void onDestroy() {

        super.onDestroy();

        if (this.mLocationManager != null) {

            for (LocationListener mLocationListener : this.mLocationListeners) {

                try {

                    this.mLocationManager.removeUpdates(mLocationListener);

                } catch (Exception ex) {

                    Log.d(TAG, "fail", ex);

                }

            }

        }

    }

    private void initializeLocationManager() {

        if (this.mLocationManager == null) {

            this.mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        }

    }

}


