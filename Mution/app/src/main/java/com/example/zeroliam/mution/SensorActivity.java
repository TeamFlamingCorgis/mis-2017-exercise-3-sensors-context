package com.example.zeroliam.mution;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Color;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import android.os.Handler;

public class SensorActivity extends Activity implements SensorEventListener{

    private SeekBar sbRate = null;
    private SeekBar sbWindow = null;
    private TextView changeRateSample, changeWindowSize;
    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    private long prevTime = 0;
    private float prevX, prevY, prevZ;
    private static final int SHAKE_THRESHOLD = 600;
    private GraphView graph;
    //X, Y, Z Values
    private LineGraphSeries<DataPoint> valuesX, valuesY, valuesZ, valuesSpeed;

    //Let's manage the sensor here
    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor mySensor = event.sensor;

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            final float x = event.values[0];
            final float y = event.values[1];
            final float z = event.values[2];


            long currentTime = System.currentTimeMillis();

            if ((currentTime - prevTime) > 100) {
                long getBreak = (currentTime - prevTime);
                prevTime = currentTime;

                float speed = Math.abs(x + y + z - prevX - prevY - prevZ)/ getBreak * 10000;
                float magnitude = Math.abs(x + y + z - prevX - prevY - prevZ)/ getBreak;

                if(speed >= 0) {
                    double getCTime = Double.parseDouble(String.valueOf(currentTime));

                    DataPoint nex = new DataPoint(getCTime, x);
                    DataPoint ney = new DataPoint(getCTime, y);
                    DataPoint nez = new DataPoint(getCTime, z);
                    valuesX.appendData(nex, false, 100);
                    valuesX.setColor(Color.RED);
                    valuesY.appendData(ney, false, 100);
                    valuesY.setColor(Color.GREEN);
                    valuesZ.appendData(nez, false, 100);
                    valuesZ.setColor(Color.BLUE);
                }

                prevX = x;
                prevY = y;
                prevZ = z;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    protected void onPause() {
        super.onPause();
//        senSensorManager.unregisterListener(this);
    }

    protected void onResume() {
        super.onResume();
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        //Sensor data
        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_FASTEST);

        //SEEKBAR Sample Rate
        sbRate = (SeekBar) findViewById(R.id.sbSampleRate);
        sbWindow = (SeekBar) findViewById(R.id.sbWindowSize);
        changeRateSample = (TextView) findViewById(R.id.currentSampleRate);
        changeWindowSize = (TextView) findViewById(R.id.currentWindowSize);

        //Chart
        graph = (GraphView) findViewById(R.id.graph);


        valuesX = new LineGraphSeries<>();
        valuesY = new LineGraphSeries<>();
        valuesZ = new LineGraphSeries<>();

        graph.addSeries(valuesX);
        graph.addSeries(valuesY);
        graph.addSeries(valuesZ);



        updateCustomSeekbar(sbRate, changeRateSample);
        updateCustomSeekbar(sbWindow, changeWindowSize);

    }

    private void updateCustomSeekbar(SeekBar sb, final TextView textb){

        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChanged = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
                progressChanged = progress;
                SensorActivity.super.runOnUiThread(new workingSeekbar(getApplicationContext(), textb, progressChanged));
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public class workingSeekbar implements Runnable {
        //Make the global vars for this class
        Context context;
        TextView showTxtProgress;
        int currentProgress;

        //Make the constructor
        workingSeekbar(Context newContext, TextView newTxtProgress, int newProgress){
            context = newContext;
            currentProgress = newProgress;
            showTxtProgress = newTxtProgress;
        }

        @Override
        public void run() {
            //Make the Toast with the values received as parameters
            showTxtProgress.setText(String.valueOf(currentProgress));

        }
    }
}
