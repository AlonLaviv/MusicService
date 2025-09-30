package com.example.musicservice;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button btnPlay, btnStop,btnPause;
    private Boolean isOn = false;
    private Boolean beenPressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnPlay = findViewById(R.id.btnPlay);
        btnPause = findViewById(R.id.btnPause);
        btnStop = findViewById(R.id.btnStop);

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent serviceIntent = new Intent(MainActivity.this, MusicService.class);
                ContextCompat.startForegroundService(MainActivity.this, serviceIntent);
                beenPressed = true;
                Toast.makeText(getApplicationContext(), "Music Started.", Toast.LENGTH_SHORT).show();
            }
        });

        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (beenPressed){
                    Intent serviceIntent = new Intent(MainActivity.this, MusicService.class);
                    serviceIntent.setAction("Pause");
                    ContextCompat.startForegroundService(MainActivity.this, serviceIntent);
                    if(isOn){//Unpause
                        btnPause.setText("Pause");
                        isOn = false;
                        Toast.makeText(getApplicationContext(), "Music Unpaused.", Toast.LENGTH_SHORT).show();
                    }else {//Pause
                        btnPause.setText("Unpause");
                        isOn = true;
                        Toast.makeText(getApplicationContext(), "Music Paused.", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "You Must Start The Music If You Want To Pause.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (beenPressed){
                Intent serviceIntent = new Intent(MainActivity.this, MusicService.class);
                stopService(serviceIntent);
                    beenPressed = false;
                    if(isOn){//Pause
                        btnPause.setText("Pause");
                        isOn = false;
                        Toast.makeText(getApplicationContext(), "Music Paused.", Toast.LENGTH_SHORT).show();
                    }
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
        Intent serviceIntent = new Intent(MainActivity.this, MusicService.class);
        stopService(serviceIntent);
    }
}
