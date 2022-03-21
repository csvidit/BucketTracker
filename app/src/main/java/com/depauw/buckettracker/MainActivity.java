package com.depauw.buckettracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;

import com.depauw.buckettracker.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private static final int ADD_ONE = 1;
    private static final int ADD_TWO = 2;
    private static final int ADD_THREE = 3;
    private static final int DEFAULT_NUM_MINS = 20;
    private static final int MILLIS_PER_MIN = 60000;
    private static final int MILLIS_PER_SEC = 1000;
    private static final int SECS_PER_MIN = 60;

    ActivityMainBinding binding;

    private View.OnClickListener toggle_is_guest_clickListener = new View.OnClickListener(){

        @Override
        public void onClick(View view) {
            if(binding.toggleIsGuest.isChecked())
            {
                binding.labelGuest.setTextColor(getResources().getColor(R.color.red));
                binding.textviewGuestScore.setTextColor(getResources().getColor(R.color.red));
                binding.labelHome.setTextColor(getResources().getColor(R.color.black));
                binding.textviewHomeScore.setTextColor(getResources().getColor(R.color.black));
            }
            else
            {
                binding.labelHome.setTextColor(getResources().getColor(R.color.red));
                binding.textviewHomeScore.setTextColor(getResources().getColor(R.color.red));
                binding.labelGuest.setTextColor(getResources().getColor(R.color.black));
                binding.textviewGuestScore.setTextColor(getResources().getColor(R.color.black));
            }
        }
    };

    private View.OnLongClickListener button_add_score_longClickListener = new View.OnLongClickListener(){


        @Override
        public boolean onLongClick(View view) {
            int newScore = Integer.parseInt(binding.textviewGuestScore.getText().toString());
            {
                if(binding.checkboxAddOne.isChecked())
                {
                    newScore+=ADD_ONE;
                }
                if(binding.checkboxAddTwo.isChecked())
                {
                    newScore+=ADD_TWO;
                }
                if(binding.checkboxAddThree.isChecked())
                {
                    newScore+=ADD_THREE;
                }
            }

            if(binding.toggleIsGuest.isChecked())
            {
                binding.textviewGuestScore.setText(Integer.toString(newScore));
            }
            else
            {
                binding.textviewHomeScore.setText(Integer.toString(newScore));
            }
            binding.checkboxAddOne.setChecked(false);
            binding.checkboxAddTwo.setChecked(false);
            binding.checkboxAddThree.setChecked(false);
            binding.toggleIsGuest.performClick();
            return true;
        }
    };

    private CountDownTimer cdt;

    private View.OnClickListener switch_game_clock_clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(binding.switchGameClock.isChecked())
            {
                String timeRemaining[] = binding.textviewTimeRemaining.getText().toString().split(":");
                long totalLength = Long.parseLong(timeRemaining[0]) * MILLIS_PER_MIN
                        + Long.parseLong(timeRemaining[1]) * MILLIS_PER_SEC;
                cdt = getNewTimer(totalLength, MILLIS_PER_SEC);
                cdt.start();
            }
            else
            {
                String timeRemaining[] = binding.textviewTimeRemaining.getText().toString().split(":");
                long totalLength = Long.parseLong(timeRemaining[0]) * MILLIS_PER_MIN
                        + Long.parseLong(timeRemaining[1]) * MILLIS_PER_SEC;
                cdt.cancel();
                cdt = getNewTimer(totalLength, MILLIS_PER_SEC);
            }
        }
    };

    private CountDownTimer getNewTimer(long totalLength, long tickLength)
    {
        return new CountDownTimer(totalLength, tickLength) {
            @Override
            public void onTick(long totalLength) {
                String timeRemaining[] = binding.textviewTimeRemaining.getText().toString().split(":");
                /*if(timeRemaining[1].equals("00"))
                {
                    String mins = Long.toString(Long.parseLong(timeRemaining[0]) - 1);
                    String secs = Long.toString(SECS_PER_MIN - 1);
                    binding.textviewTimeRemaining.setText(
                            String.format("%tM", mins)
                            +":"
                            +String.format("%tS", secs)
                    );
                }
                else
                {
                    String mins = timeRemaining[0];
                    String secs = Long.toString(Long.parseLong(timeRemaining[1]) - 1);
                    binding.textviewTimeRemaining.setText(
                            String.format("%tM", mins)
                                    +":"
                                    +String.format("%tS", secs)
                    );
                }*/
                /*binding.textviewTimeRemaining.setText(
                        String.format("%TM", totalLength/MILLIS_PER_MIN)
                        +":"
                        +String.format("%TS", totalLength%MILLIS_PER_SEC)
                );*/
                long mins = (totalLength / MILLIS_PER_SEC) / SECS_PER_MIN;
                long secs = (totalLength / MILLIS_PER_SEC) % SECS_PER_MIN;
                binding.textviewTimeRemaining.setText(mins + ":" + secs);

                totalLength-=tickLength;
            }

            @Override
            public void onFinish() {
                binding.textviewTimeRemaining.setText("00:00");
                binding.switchGameClock.setChecked(false);
            }
        };
    }

    private View.OnClickListener button_set_time_clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(
                        (binding.edittextNumMins.getText().toString().equals("")
                    || Integer.parseInt(binding.edittextNumMins.getText().toString()) > 59
                    || Integer.parseInt(binding.edittextNumMins.getText().toString()) < 0)
                ||
                        (binding.edittextNumSecs.getText().toString().equals("")
                    || Integer.parseInt(binding.edittextNumSecs.getText().toString()) > SECS_PER_MIN
                    || Integer.parseInt(binding.edittextNumSecs.getText().toString()) < 0)
            )
            {
                binding.edittextNumMins.setTextColor(getResources().getColor(R.color.red));
                binding.edittextNumSecs.setTextColor(getResources().getColor(R.color.red));
            }
            else
            {
                if(binding.edittextNumSecs.getText().toString().equals("60"))
                {
                    binding.textviewTimeRemaining.setText(Integer.parseInt(binding.edittextNumMins.getText().toString())+1
                            + ":00");
                }
                else
                {
                    binding.textviewTimeRemaining.setText(binding.edittextNumMins.getText().toString()
                            + ":" + binding.edittextNumSecs.getText().toString());
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.toggleIsGuest.setOnClickListener(toggle_is_guest_clickListener);
        binding.buttonAddScore.setOnLongClickListener(button_add_score_longClickListener);
        binding.switchGameClock.setOnClickListener(switch_game_clock_clickListener);
        binding.buttonSetTime.setOnClickListener(button_set_time_clickListener);
        binding.toggleIsGuest.setChecked(false);
    }
}