package edu.washington.cs.ubicomplab.com.pvtapp.Activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import edu.washington.cs.ubicomplab.com.pvtapp.R;

public class MainActivity extends AppCompatActivity {

    private Timer firstTimer;
    private Timer secondTimer;
    private Handler handler = new Handler();
    TextView timerOneText;
    TextView timerTwoText;
    View startView;

    private boolean isSecondTimerRunning = false;
    private int maxMinutesFirstTimer = 5;

    private int rangeOfMinutesSecondTimer = 2;

    private String secondTimerDelay;
    private int millisecondsFirstTimer = 0;
    private int secondsFirstTimer = 0;
    private int minutesFirstTimer = 0;
    private String  firstTimerTime;
    private String  secondTimerTime;

    private int millisecondsSecondTimer = 0;
    private int secondsSecondTimer = 0;
    private int minutesSecondTimer = 0;

    private int i = 0;

    List<String> timeRecords = new ArrayList<>();

    private static final long NUMBER_MILLIS = 20000;
    private static final String MILLISECONDS_FORMAT = "%03d";
    private int secondsLeft = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        timerOneText = findViewById(R.id.timer);
        timerTwoText = findViewById(R.id.timer_two);
        startView = findViewById(R.id.startView);
        startView.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
        timerTwoText.setVisibility(View.INVISIBLE);
        startFirstTimer();
        generateSecondTimerRandomDelay();

        startView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSecondTimerRunning) {
                    timeRecords.add(secondTimerTime);
                    stopSecondTimer();
                    resetSecondTimer();
                }
            }
        });
    }

    private void startFirstTimer() {
        firstTimer = new Timer();
        firstTimer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {

                handler.post(new Runnable() {
                    @SuppressLint("DefaultLocale")
                    @Override
                    public void run() {

                        if (minutesFirstTimer == maxMinutesFirstTimer && i < 1) {
                                stopFirstTimer();
                                stopSecondTimer();
                                showResults();
                                i += 1;
                        } else {
                            if (millisecondsFirstTimer >= 1000) {
                                millisecondsFirstTimer = 0;
                                secondsFirstTimer++;
                            }
                            if (secondsFirstTimer >= 60) {
                                secondsFirstTimer = 0;
                                minutesFirstTimer++;
                            }
                            firstTimerTime = String.format("%02d:%02d:%03d",
                                    minutesFirstTimer, secondsFirstTimer, millisecondsFirstTimer);
                            timerOneText.setText(firstTimerTime);
                            millisecondsFirstTimer += 1;
                        }

                        manageSecondTimer(firstTimerTime);
                    }
                });
            }
        }, 100, 1); // initial delay, interval
    }
    @SuppressLint("DefaultLocale")
    private void manageSecondTimer(String firstTimerTime) {
        if (isSecondTimerRunning == false) {
            if (firstTimerTime.equals(secondTimerDelay)) {
                startView.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
                startSecondTimer();
            }
        }
    }
    private void startSecondTimer() {
        isSecondTimerRunning = true;
        secondTimer = new Timer();
        secondTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (millisecondsSecondTimer >= 1000) {
                            millisecondsSecondTimer = 0;
                            secondsSecondTimer++;
                        }
                        if (secondsSecondTimer >= 60) {
                            secondsSecondTimer = 0;
                            minutesSecondTimer++;
                        }
                        secondTimerTime = String.format("%02d:%02d:%03d", minutesSecondTimer, secondsSecondTimer, millisecondsSecondTimer);
                        millisecondsSecondTimer += 1;
                    }
                });
            }
        }, 100, 1); // initial delay, interval
    }
    private void stopFirstTimer() {
        if (firstTimer != null) {
            firstTimer.cancel();
        }
    }

    private void stopSecondTimer() {
        Log.wtf("--->", firstTimerTime);
        Log.wtf("--->", secondTimerTime);
        isSecondTimerRunning = false;
        generateSecondTimerRandomDelay();
        if (secondTimer != null) {
            secondTimer.cancel();
            startView.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
        }
    }

    private void resetFirstTimer() {
        millisecondsFirstTimer = 0;
        secondsFirstTimer = 0;
        minutesFirstTimer = 0;
        timerOneText.setText("00:00:000"); // Update the UI to display the reset time
    }

    private void resetSecondTimer() {
        millisecondsSecondTimer = 0;
        secondsSecondTimer = 0;
        minutesSecondTimer = 0;
    }

    private void generateSecondTimerRandomDelay() {
        Random random = new Random();
        int randomMinutes = random.nextInt(rangeOfMinutesSecondTimer);
            if (minutesFirstTimer > randomMinutes){
                int minutesDiff = minutesFirstTimer-randomMinutes;
                randomMinutes += minutesDiff;
            }
        int randomSeconds = random.nextInt(60);
        int randomMilliseconds = random.nextInt(1000);
        secondTimerDelay = String.format("%02d:%02d:%03d", randomMinutes, randomSeconds, randomMilliseconds);
        Log.i("minutes", String.valueOf(randomMinutes));
        Log.i("Seconds", String.valueOf(randomSeconds));
        Log.i("Milliseconds", String.valueOf(randomMilliseconds));
        Log.i("second time", secondTimerDelay);
    }

    private void showResults() {
        StringBuilder messageBuilder = new StringBuilder();
        int recordNumber = 1;
        for (String time : timeRecords) {
            messageBuilder.append(recordNumber).append(". ").append(time).append("\n");
            recordNumber++;
        }
        // Create an AlertDialog with the numbered list
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Time Records");

        builder.setMessage(messageBuilder.toString())
                .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                       ///-->
                    }
                });

        builder.setMessage(messageBuilder.toString())
                .setPositiveButton("Restart", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        restart();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void restart() {
        resetFirstTimer();
        resetSecondTimer();
        startFirstTimer();
        generateSecondTimerRandomDelay();
    }

}
