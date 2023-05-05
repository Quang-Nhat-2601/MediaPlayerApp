package com.example.mediaplayerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Time;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    Button btn_play, btn_pause, btn_forward, btn_rewind;
    TextView time_text, title_text;
    SeekBar seekBar;

    // Media player
    MediaPlayer mediaPlayer;

    // Handlers
    Handler handler = new Handler();

    // Variables
    double startTime = 0;
    double finalTime = 0;
    int forwardTime = 10000;
    int backwardTime = 10000;
    static  int oneTimeOnly = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_play = findViewById(R.id.btn_play);
        btn_pause = findViewById(R.id.btn_pause);
        btn_forward = findViewById(R.id.btn_forward);

        btn_rewind = findViewById(R.id.btn_rewind);

        time_text = findViewById(R.id.tv_time);
        title_text = findViewById(R.id.tv_title);

        seekBar = findViewById(R.id.seekBar);

        title_text.setText(getResources().getIdentifier(
                "astronaut",
                "raw",
                getPackageName()
        ));

        // media player
        mediaPlayer = MediaPlayer.create(
                this,
                R.raw.astronaut
                );

        seekBar.setClickable(false);

        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlayMusic();
            }
        });

        btn_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.pause();
            }
        });

        btn_forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int temp = (int) startTime;
                if ((temp + forwardTime) <= finalTime) {
                    startTime = startTime + forwardTime;
                    mediaPlayer.seekTo((int) startTime);
                } else {
                    Toast.makeText(MainActivity.this, "Can not jump foward", Toast.LENGTH_SHORT).show();
                }
            }
        });

         btn_rewind.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 int temp = (int) startTime;
                 if ((temp - backwardTime) >= 0){
                     startTime = startTime - backwardTime;
                     mediaPlayer.seekTo((int) startTime);
                 } else {
                     Toast.makeText(MainActivity.this, "Can not jump backward", Toast.LENGTH_SHORT).show();
                 }

             }
         });
    }
    
    private void PlayMusic() {
        mediaPlayer.start();

        finalTime = mediaPlayer.getDuration();
        startTime = mediaPlayer.getCurrentPosition();

        if(oneTimeOnly == 0) {
            seekBar.setMax((int) finalTime);
            oneTimeOnly = 1;
        }

        time_text.setText(String.format(
                "%d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                TimeUnit.MILLISECONDS.toSeconds((long) finalTime)-
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes
                                ((long) finalTime))
        ));

        seekBar.setProgress((int) startTime);
        handler.postDelayed(UpdateSongTime, 100);
    }

    // Creating the Runnabel
    private Runnable UpdateSongTime = new Runnable() {
        @Override
        public void run() {
            startTime = mediaPlayer.getCurrentPosition();
            time_text.setText(String.format("%d min, %d sec",
                    TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) startTime)
                            - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) startTime))
            ));
            seekBar.setProgress((int) startTime);
            handler.postDelayed(UpdateSongTime, 100);
        }
    };

}