package com.example.zeroliam.mution;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.location.LocationListener;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private LocationListener locLis;
    private LocationManager locationManager;
    private SensorEvent sensor;
    private Speed ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        ll = new Speed();

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
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 300, 0, ll);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 300, 0, ll);


    }

    public void goToSensor(View v) {
        Intent sensorIntent = new Intent(getApplicationContext(), SensorActivity.class);
        startActivity(sensorIntent);
    }

    public void goToPlayer(View v) {
        Intent sensorIntent = new Intent(getApplicationContext(), PlayerActivity.class);
        startActivity(sensorIntent);
    }

    public class Speed implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {

            Toast.makeText(getApplication(), "SPEED : " + location.getSpeed(), Toast.LENGTH_SHORT).show();
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


    @Override
    public void onSensorChanged(SensorEvent event) {
//        sensor = event.sensor;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
