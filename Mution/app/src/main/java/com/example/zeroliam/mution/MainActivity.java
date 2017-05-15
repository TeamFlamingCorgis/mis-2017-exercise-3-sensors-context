package com.example.zeroliam.mution;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void goToSensor(View v){
        Intent sensorIntent = new Intent(getApplicationContext(), SensorActivity.class);
        startActivity(sensorIntent);
    }

    public void goToPlayer(View v){
        Intent sensorIntent = new Intent(getApplicationContext(), PlayerActivity.class);
        startActivity(sensorIntent);
    }
}
