package com.sstive39.cycling;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity implements HomeFragment.Communicator {

    public static HomeFragment homeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        homeFragment = new HomeFragment();

        getSupportFragmentManager().beginTransaction().add(R.id.pageHolder, homeFragment).commit();
    }

    @Override
    public boolean startSpeedometerService() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder .setMessage(R.string.request_permission)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                                    Manifest.permission.ACCESS_FINE_LOCATION}, 0);
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            builder.create().show();
            return false;
        }
        Intent intent = new Intent(this, SpeedometerService.class);
        ContextCompat.startForegroundService(this, intent);
        return true;
    }

    public static void showDialog (Dialog d) {
        d.show();
    }

    @Override
    public void stopSpeedometerService() {
        Intent intent = new Intent(this, SpeedometerService.class);
        stopService(intent);
    }
}
