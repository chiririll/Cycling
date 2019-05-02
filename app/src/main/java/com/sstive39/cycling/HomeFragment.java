package com.sstive39.cycling;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private boolean start = false;
    private boolean paused = false;
    private Button startBtn;
    private Button pauseBtn;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        startBtn = root.findViewById(R.id.btnStart);
        pauseBtn = root.findViewById(R.id.btnPause);
        startBtn.setOnClickListener(this);
        pauseBtn.setOnClickListener(this);

        return root;
    }

    @Override
    public void onClick (View v) {
        switch (v.getId()) {
            case R.id.btnStart:
                start();
                break;
            case R.id.btnPause:
                pause();
                break;
        }
    }

    public void start () {
        if (start) {
            // Stopping (set text&color start)
            startBtn.setText(getString(R.string.start));
            startBtn.setBackgroundColor(getResources().getColor(R.color.colorBtnStart));
            pauseBtn.setVisibility(View.GONE);
        } else {
            // Starting (set text&color stop)
            startBtn.setText(getString(R.string.stop));
            startBtn.setBackgroundColor(getResources().getColor(R.color.colorBtnStop));
            pauseBtn.setVisibility(View.VISIBLE);
        }

        start = !start;
        paused = true;
        pause();
    }

    public void pause () {
        if (paused) {
            // Resuming (set text&color pause)
            pauseBtn.setText(getString(R.string.pause));
            pauseBtn.setBackgroundColor(getResources().getColor(R.color.colorBtnPause));
        } else {
            // Pausing (set text&color resume)
            pauseBtn.setText(getString(R.string.resume));
            pauseBtn.setBackgroundColor(getResources().getColor(R.color.colorBtnResume));
        }
        paused = !paused;
    }

    public void displayData (long duration, double speed, double distance) {
        // TODO: Display data
    }

}
