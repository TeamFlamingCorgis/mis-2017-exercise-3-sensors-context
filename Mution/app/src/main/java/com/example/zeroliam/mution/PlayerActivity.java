package com.example.zeroliam.mution;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class PlayerActivity extends AppCompatActivity implements LocationListener{
    private MediaPlayer bikePlay, jogPlay;
    private int playing;
    private Button jog, bike, file;
    private TextView st;
    private final static int REQ_MP3_FILE = 1;
    //Player Example: Book Android Bootcamp for Developers by Corinne Hoisin

    // Declaring a Location Manager
    protected LocationManager locationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        st = (TextView) findViewById(R.id.stat);
        jog = (Button) findViewById(R.id.jog);
        bike = (Button) findViewById(R.id.bike);
        file = (Button) findViewById(R.id.file);
        file.setText("Choose file for both");
        bikePlay = new MediaPlayer();
        jogPlay = new MediaPlayer();
        bikePlay = MediaPlayer.create(this, R.raw.bike);
        jogPlay = MediaPlayer.create(this, R.raw.jog);
        playing = 0;
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);


    }

    public void goToSong(View v){
        String.valueOf(v.getTag());

        if(v.getId() == R.id.jog){
            switch(playing) {
                case 0:
                    jogPlay.start();
                    playing = 1;
                    jog.setText("Stop Jog Music");
                    st.setText("Playing while Jogging");
                    bike.setVisibility(View.INVISIBLE);
                    break;
                case 1:
                    jogPlay.pause();
                    jog.setText("JOGGING");
                    st.setText("Biking");
                    playing = 0;
                    bike.setText("BIKING");
                    bike.setVisibility(View.VISIBLE);
                    break;
            }
        }else if(v.getId() == R.id.bike){
            st.setText("Playing while Biking");
            switch(playing) {
                case 0:
                    bikePlay.start();
                    playing = 1;
                    bike.setText("Stop Bike Music");
                    st.setText("Playing while Biking");
                    jog.setVisibility(View.INVISIBLE);
                    break;
                case 1:
                    bikePlay.pause();
                    jog.setText("JOGGING");
                    playing = 0;
                    bike.setText("BIKING");
                    st.setText("Jogging");
                    jog.setVisibility(View.VISIBLE);
                    break;
            }
        }else if(v.getId() == R.id.file){

            if(bikePlay.isPlaying()){
                bikePlay.stop();
            }
            if(jogPlay.isPlaying()){
                jogPlay.stop();
            }
            //Choose a file
            //Source: http://android-er.blogspot.de/2012/06/start-intent-to-choice-audiomp3-using.html
            Intent intent = new Intent();
            intent.setType("audio/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(
                    intent, "Open Audio (mp3) file"), REQ_MP3_FILE);


        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(bikePlay.isPlaying()){
            bikePlay.stop();
        }
        if(jogPlay.isPlaying()){
            jogPlay.stop();
        }
        if (resultCode == RESULT_OK) {
            if (requestCode == REQ_MP3_FILE) {
                Uri audioFileUri = data.getData();

                bikePlay = MediaPlayer.create(this, audioFileUri);
                jogPlay = MediaPlayer.create(this, audioFileUri);
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        Float speed = location.getSpeed();
        Float newSp = (speed * 3600) / 1000;

        if(newSp > 0 && newSp < 15){
            switch(playing) {
                case 0:
                    jogPlay.start();
                    playing = 1;
                    jog.setText("Stop Jog Music");
                    st.setText("Playing while Jogging");
                    bike.setVisibility(View.INVISIBLE);
                    break;
                case 1:
                    jogPlay.pause();
                    jog.setText("JOGGING");
                    playing = 0;
                    bike.setText("BIKING");
                    bike.setVisibility(View.VISIBLE);
                    break;
            }
        }else if(newSp > 15){
            st.setText("Playing while Biking");
            switch(playing) {
                case 0:
                    bikePlay.start();
                    playing = 1;
                    bike.setText("Stop Bike Music");
                    st.setText("Playing while Biking");
                    jog.setVisibility(View.INVISIBLE);
                    break;
                case 1:
                    bikePlay.pause();
                    jog.setText("JOGGING");
                    playing = 0;
                    bike.setText("BIKING");
                    jog.setVisibility(View.VISIBLE);
                    break;
            }
            Toast.makeText(getApplication(),"SPEED : "+String.valueOf(speed)+"m/s",Toast.LENGTH_SHORT).show();
        }




    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //Destroys activity.
        if(bikePlay.isPlaying()){
            bikePlay.stop();
        }
        if(jogPlay.isPlaying()){
            jogPlay.stop();
        }
        finish();
        return true;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


        }



