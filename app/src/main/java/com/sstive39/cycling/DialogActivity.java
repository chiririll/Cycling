package com.sstive39.cycling;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;

public class DialogActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MainActivity.homeFragment.stop(true);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder .setMessage(R.string.request_gps_on)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent openSettings = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(openSettings);
                        finish();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        builder.create().show();
    }
}
