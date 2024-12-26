package com.example.stopwatch;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView timerTextView;
    private ImageButton startButton, pauseButton, resetButton;

    private Handler handler = new Handler();
    private long startTime = 0L;
    private boolean isRunning = false;
    private long elapsedTime = 0L;

    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime + elapsedTime;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;
            int milliseconds = (int) (millis % 1000);

            timerTextView.setText(String.format("%02d:%02d:%03d", minutes, seconds, milliseconds));
            handler.postDelayed(this, 10); // Update every 10 milliseconds
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI Components
        timerTextView = findViewById(R.id.timerTextView);
        startButton = findViewById(R.id.startButton);
        pauseButton = findViewById(R.id.pauseButton);
        resetButton = findViewById(R.id.resetButton);

        // Initialize Buttons with Click Listeners
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateButton(view);
                if (!isRunning) {
                    startTime = System.currentTimeMillis();
                    handler.post(timerRunnable);
                    isRunning = true;
                    Toast.makeText(MainActivity.this, "Stopwatch Started", Toast.LENGTH_SHORT).show();
                }
            }
        });

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateButton(view);
                if (isRunning) {
                    handler.removeCallbacks(timerRunnable);
                    elapsedTime += System.currentTimeMillis() - startTime;
                    isRunning = false;
                    Toast.makeText(MainActivity.this, "Stopwatch Paused", Toast.LENGTH_SHORT).show();
                }
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateButton(view);
                handler.removeCallbacks(timerRunnable);
                isRunning = false;
                startTime = 0L;
                elapsedTime = 0L;
                timerTextView.setText("00:00:000");
                Toast.makeText(MainActivity.this, "Stopwatch Reset", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Button Click Animation
    private void animateButton(View view) {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.button_click_animation);
        view.startAnimation(animation);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(timerRunnable);
    }
}
