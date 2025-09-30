package com.example.musicservice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button btnPlay, btnStop,btnPause;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnPlay = findViewById(R.id.btnPlay);
        btnPause = findViewById(R.id.btnPause);
        btnStop = findViewById(R.id.btnStop);
        final Boolean[] isOn = {false};
        final Boolean[] beenPressed = {false};

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent serviceIntent = new Intent(MainActivity.this, MusicActivity.class);
                ContextCompat.startForegroundService(MainActivity.this, serviceIntent);
                beenPressed[0] = true;
                Toast.makeText(getApplicationContext(), "Music Started.", Toast.LENGTH_SHORT).show();
            }
        });

        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (beenPressed[0]){
                    Intent serviceIntent = new Intent(MainActivity.this, MusicActivity.class);
                    serviceIntent.setAction("Pause");
                    ContextCompat.startForegroundService(MainActivity.this, serviceIntent);
                    if(isOn[0]){//Unpause
                        btnPause.setText("Pause");
                        isOn[0] = false;
                        Toast.makeText(getApplicationContext(), "Music Unpaused.", Toast.LENGTH_SHORT).show();
                    }else {//Pause
                        btnPause.setText("Unpause");
                        Toast.makeText(getApplicationContext(), "Music Paused.", Toast.LENGTH_SHORT).show();
                        isOn[0] = true;
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "You Must Start The Music If You Want To Pause.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (beenPressed[0]){
                Intent serviceIntent = new Intent(MainActivity.this, MusicActivity.class);
                stopService(serviceIntent);
                    Toast.makeText(getApplicationContext(), "Music Stopped.", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(), "You Must Start The Music If You Want To Pause.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent serviceIntent = new Intent(MainActivity.this, MusicActivity.class);
        stopService(serviceIntent);
    }
}
