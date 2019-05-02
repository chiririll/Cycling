package com.sstive39.cycling;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HomeFragment homeFragment = new HomeFragment();

        getSupportFragmentManager().beginTransaction().add(R.id.pageHolder, homeFragment).commit();

        // TODO: Start speedometerService
    }

}
