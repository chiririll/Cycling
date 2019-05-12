package com.sstive39.cycling;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class HomeFragment extends Fragment implements View.OnClickListener {

    public boolean start = false;
    public boolean paused = false;

    private Button startBtn;
    private Button pauseBtn;

    private TextView nowSpeedText;
    private TextView maxSpeedText;
    private TextView avgSpeedText;
    private TextView distanceText;
    private TextView durationText;

    private Communicator communicator;

    public interface Communicator {
        boolean startSpeedometerService();
        void stopSpeedometerService();
    }

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        startBtn = root.findViewById(R.id.btnStart);
        pauseBtn = root.findViewById(R.id.btnPause);

        nowSpeedText = root.findViewById(R.id.nowSpeed);
        maxSpeedText = root.findViewById(R.id.nowMaxSpeed);
        avgSpeedText = root.findViewById(R.id.avgSpeed);
        distanceText = root.findViewById(R.id.nowDistance);
        durationText = root.findViewById(R.id.nowDuration);

        startBtn.setOnClickListener(this);
        pauseBtn.setOnClickListener(this);

        return root;
    }

    @Override
    public void onClick (View v) {
        switch (v.getId()) {
            case R.id.btnStart:
                if (!start) {
                    if (!communicator.startSpeedometerService()) return;
                    start();
                } else
                    stop(true);
                break;
            case R.id.btnPause:
                if (paused) {
                    resume();
                } else
                    pause();
                break;
        }
    }

    public void start () {
        startBtn.setText(getString(R.string.stop));
        startBtn.setBackgroundColor(getResources().getColor(R.color.colorBtnStop));
        pauseBtn.setVisibility(View.VISIBLE);

        start = true;
    }

    public void stop (boolean stopService) {
        startBtn.setText(getString(R.string.start));
        startBtn.setBackgroundColor(getResources().getColor(R.color.colorBtnStart));
        pauseBtn.setVisibility(View.GONE);

        resume();
        updateUI("00:00:00", 0, "0,00", 0, "0,0");
        if (stopService)
            communicator.stopSpeedometerService();
        start = false;
    }

    public void pause () {
        pauseBtn.setText(getString(R.string.resume));
        pauseBtn.setBackgroundColor(getResources().getColor(R.color.colorBtnResume));

        paused = true;
    }

    public void resume () {
        pauseBtn.setText(getString(R.string.pause));
        pauseBtn.setBackgroundColor(getResources().getColor(R.color.colorBtnPause));

        paused = false;
    }

    public void updateUI (String duration, int speed, String distance, int maxSpeed, String avgSpeed) {
        nowSpeedText.setText(String.valueOf(speed));
        maxSpeedText.setText(String.valueOf(maxSpeed));
        avgSpeedText.setText(avgSpeed);
        distanceText.setText(distance);
        durationText.setText(duration);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach (context);
        if (context instanceof Communicator)
            communicator = (Communicator) context;
        else
            throw new RuntimeException(context.toString() + "must implement Communicator");
    }
}
