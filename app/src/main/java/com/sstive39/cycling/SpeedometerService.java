package com.sstive39.cycling;

import android.Manifest;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;

import static com.sstive39.cycling.App.CHANNEL_ID;


public class SpeedometerService extends Service {

    private LocationManager locationManager;
    private Location lStart = null, lEnd = null;

    private long startTime;
    private String duration;
    private int speed;
    private int speedCount = 1;
    private int maxSpeed = 0;
    private double distance = 0;
    private double speedSum = 0;
    private boolean paused = false;

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            lEnd = location;
            if (lStart == null)
                lStart = location;
            countValues();
            lStart = lEnd;
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {
            Intent i = new Intent(getApplicationContext(), DialogActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }
    };

    private void saveData() {
        // TODO: Save data if not paused
    }

    private void notification() {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Intent stopIntent = new Intent(this, MainActivity.class);
        PendingIntent stopPendingIntent = PendingIntent.getActivity(this, 0, stopIntent, 0);

        Intent pauseIntent = new Intent(this, MainActivity.class);
        PendingIntent pausePendingIntent = PendingIntent.getActivity(this, 0, pauseIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(getString(R.string.app_name))
                .setContentText("Hello, World!")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentIntent(pendingIntent)
                .addAction(R.drawable.ic_media_stop_light, getString(R.string.stop), stopPendingIntent)
                .addAction(R.drawable.ic_media_pause_light, getString(R.string.pause), pausePendingIntent)
                .build();

        startForeground(23546, notification);
    }

    private void countValues () {

        if (lStart != null && lEnd != null) {
            speed = Math.round(lEnd.getSpeed() * 18 / 5);
            speedSum += speed;
            speedCount++;
            if (speed > maxSpeed) maxSpeed = speed;

            distance += lStart.distanceTo(lEnd);
        }

        int duration = (int) Math.floor(System.currentTimeMillis()/1000 - startTime);
        int h, m, s;

        s = duration % 60;
        duration /= 60;
        m = duration % 60;
        duration /= 60;
        h = duration;

        this.duration = String.format("%02i:%02i:%02i", h, m, s);

        MainActivity.homeFragment.updateUI(this.duration, speed, String.format("%.2f", distance/1000), maxSpeed, String.format("%.1f", speedSum/speedCount));

        saveData();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        notification();
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {return;}

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListener);
        startTime = System.currentTimeMillis()/1000;
    }

    @Override
    public void onDestroy() {
        locationManager.removeUpdates(locationListener);
    }
}