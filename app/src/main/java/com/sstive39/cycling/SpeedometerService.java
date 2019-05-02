package com.sstive39.cycling;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.concurrent.TimeUnit;


public class SpeedometerService extends Service implements
        LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final long INTERVAL = 2000;
    private static final long FASTEST_INTERVAL = 1000;

    private final IBinder mBinder = new LocalBinder();

    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mCurrentLocation, lStart, lEnd;

    long startTime;
    long duration = 0;
    double distance = 0;
    double speed;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        createLocationRequest();

        startTime = System.currentTimeMillis();
        // TODO: create file/table with info about travel

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();
        return mBinder;
    }

    protected void createLocationRequest() {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }



    @Override
    public void onConnected(Bundle bundle) {
        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);
        } catch (SecurityException e) {}
    }


    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
        distance = 0;
    }



    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        if (lStart == null)
            lStart = mCurrentLocation;
        lEnd = mCurrentLocation;

        speed = location.getSpeed() * 18 / 5; // convert fom m/s to km/h
        distance += lStart.distanceTo(lEnd) / 1000.00;
        duration = TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis() - startTime);

        updateUI();
        saveData();

        lStart = lEnd;
    }

    //The live feed of Distance and Speed are being set in the method below .
    @Override
    public boolean onUnbind(Intent intent) {
        stopLocationUpdates();
        if (mGoogleApiClient.isConnected())
            mGoogleApiClient.disconnect();
        lStart = null;
        lEnd = null;
        distance = 0;
        return super.onUnbind(intent);
    }

    private void updateUI() {
        // TODO: Update values in Fragment
    }

    private void saveData() {
        // TODO: Save data if not paused
    }

    public class LocalBinder extends Binder {
        public SpeedometerService getService() {
            return SpeedometerService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {}
}