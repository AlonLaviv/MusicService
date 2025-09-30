package com.example.musicservice;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import androidx.core.app.NotificationCompat;

public class MusicActivity extends Service {

    private static final String CHANNEL_ID = "AudioServiceChannel";
    private MediaPlayer mediaPlayer;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("MusicActivity", "onCreate called");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();
        if ("Pause".equals(action)) {
            pauseMusic();
            return START_NOT_STICKY;
        }
        int songId = intent.getIntExtra("song_id", R.raw.final_countdown);

        createNotificationChannel();

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Music Playing")
                .setContentText("Your song is playing")
                .setSmallIcon(android.R.drawable.ic_media_play)
                .build();

        startForeground(1, notification);

        mediaPlayer = MediaPlayer.create(this, songId);
        mediaPlayer.start();

        return START_NOT_STICKY;
    }
    //shai code
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Media Playback Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            serviceChannel.setSound(null,null); // Disable sound for this channel

            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(serviceChannel);
                Log.d("MediaService", "Notification channel '" + CHANNEL_ID + "' created with importance DEFAULT.");
            } else {
                Log.e("MediaService", "NotificationManager is null, channel not created.");
            }
        }
    }


    public void pauseMusic() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {mediaPlayer.pause();}
        else {mediaPlayer.start();}
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("MusicActivity", "onDestroy called");
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
