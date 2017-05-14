package com.example.zeroliam.mution;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void goToSensor(){
        Intent sensorIntent = new Intent(MainActivity.this, SensorActivity.class);
        startActivity(sensorIntent);
    }

    public void goToPlayer(){
        Intent sensorIntent = new Intent(MainActivity.this, PlayerActivity.class);
        startActivity(sensorIntent);
    }
}
