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
import com.numetriclabz.numandroidcharts.ChartData;
import com.numetriclabz.numandroidcharts.MultiLineChart;

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
    private static final int SHAKE_THRESHOLD = 200;
    private GraphView graph;
    private Runnable clearData;
    private Handler graphHandler;
    private final Handler mHandler = new Handler();
    private Runnable mTimer1;
    private Runnable mTimer2;
    //X, Y, Z Values
    private LineGraphSeries<DataPoint> valuesX, valuesX2, valuesY, valuesZ, valuesSpeed;

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

                if(speed >= 0){
                    double getCTime = Double.parseDouble(String.valueOf(currentTime));
                    mTimer1 = new clearUpdate(getApplicationContext(), valuesX, getCTime, x);
                    mHandler.postDelayed(mTimer1, 300);

                    mTimer2 = new workingUpdate(getApplicationContext(), graph, valuesX, getCTime, x, false, 100, Color.RED);
                    mHandler.postDelayed(mTimer2, 1000);

//                    (new Thread(){
//                        public void run(){
//                            long currentTime = System.currentTimeMillis();
//                            double getCTime = Double.parseDouble(String.valueOf(currentTime));
//                            SensorActivity.super.runOnUiThread(new workingToast(getApplicationContext(), "X: " + String.valueOf(x) + " - Y: " + String.valueOf(y) + " - Z: " + String.valueOf(z), Toast.LENGTH_SHORT));
//                            SensorActivity.super.runOnUiThread(new workingUpdate(getApplicationContext(), graph, valuesX, getCTime, x, false, 100, Color.RED));
//
//                        }
//                    }).start();
                }

//                if (speed > SHAKE_THRESHOLD) {
//                    (new Thread(){
//                        public void run(){
//                            long currentTime = System.currentTimeMillis();
//                            double getCTime = Double.parseDouble(String.valueOf(currentTime));
//                            SensorActivity.super.runOnUiThread(new workingToast(getApplicationContext(), "X: " + String.valueOf(x) + " - Y: " + String.valueOf(y) + " - Z: " + String.valueOf(z), Toast.LENGTH_SHORT));
//                            SensorActivity.super.runOnUiThread(new workingUpdate(getApplicationContext(), valuesX, getCTime, x, false, 100, Color.RED));
//
//                        }
//                    }).start();
//                    double getCTime = Double.parseDouble(String.valueOf(currentTime));
                    //Change the curves here!
                    //createChart(x,y,z,speed,Float.parseFloat(String.valueOf(getBreak)));
//                    SensorActivity.super.runOnUiThread(new workingToast(getApplicationContext(), "X: " + String.valueOf(x) + " - Y: " + String.valueOf(y) + " - Z: " + String.valueOf(z), Toast.LENGTH_SHORT));
//                    SensorActivity.super.runOnUiThread(new workingUpdate(getApplicationContext(), valuesX, getCTime, x, false, 100, Color.RED));

//                    valuesX.appendData(new DataPoint(getCTime, x), false, 100);
//                    valuesX.setColor(Color.RED);
//                    valuesY.appendData(new DataPoint(getCTime, y), false, 100);
//                    valuesY.setColor(Color.GREEN);
//                    valuesZ.appendData(new DataPoint(getCTime, z), false, 100);
//                    valuesZ.setColor(Color.BLUE);
//                    valuesSpeed.appendData(new DataPoint(getCTime, magnitude), false, 100);
//                    valuesSpeed.setColor(Color.BLACK);

//                }else if(speed == 0){
//                    (new Thread(){
//                        public void run(){
//                            long currentTime = System.currentTimeMillis();
//                            double getCTime = Double.parseDouble(String.valueOf(currentTime));
//                            SensorActivity.super.runOnUiThread(new workingToast(getApplicationContext(), "X: " + String.valueOf(x) + " - Y: " + String.valueOf(y) + " - Z: " + String.valueOf(z), Toast.LENGTH_SHORT));
//                            SensorActivity.super.runOnUiThread(new workingUpdate(getApplicationContext(), valuesX, getCTime, x, false, 100, Color.RED));
//
//                        }
//                    }).start();

//                }

                prevX = x;
                prevY = y;
                prevZ = z;
            }
        }
    }


    public class workingToast implements Runnable {
        //Make the global vars for this class
        Context context;
        CharSequence text;
        int duration;

        //Make the constructor
        workingToast(Context newContext, CharSequence newText, int newDuration){
            context = newContext;
            text = newText;
            duration = newDuration;
        }

        @Override
        public void run() {
            //Make the Toast with the values received as parameters
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
    }


    public class workingUpdate implements Runnable {
        //Make the global vars for this class
        Context context;
        GraphView graphy;
        LineGraphSeries series;
        double h;
        double v;
        boolean scrollable;
        int tops;
        int color;

        //Make the constructor
        workingUpdate(Context newContext, GraphView newGraph, LineGraphSeries newSeries, double newH, double newV, boolean newScrollable, int newTops, int newColor){
            context = newContext;
            graphy = newGraph;
            series = newSeries;
            h = newH;
            v = newV;
            scrollable = newScrollable;
            tops = newTops;
            color = newColor;
        }

        @Override
        public void run() {
            //Make the Toast with the values received as parameters
            createGraph(graphy, series, h, v, scrollable, tops, color);
        }
    }

    public class clearUpdate implements Runnable {
        //Make the global vars for this class
        Context context;
        LineGraphSeries series;
        double h;
        double v;

        //Make the constructor
        clearUpdate(Context newContext, LineGraphSeries newSeries, double newH, double newV){
            context = newContext;
            series = newSeries;
            h = newH;
            v = newV;
        }

        @Override
        public void run() {
            //Make the Toast with the values received as parameters
            clearGraph(series, h, v);
            mHandler.postDelayed(this, 300);
        }
    }


    public void createGraph(GraphView graphy, LineGraphSeries series, double h, double v, boolean scrollable, int tops, int color){
        //valuesX.appendData(new DataPoint(getCTime, x), false, 100);
        series.appendData(new DataPoint(h,v),scrollable,tops);
        series.setColor(color);

        graphy.addSeries(series);
    }

    public void clearGraph(LineGraphSeries series, double h, double v){
        //valuesX.appendData(new DataPoint(getCTime, x), false, 100);
        series.resetData(new DataPoint[]{new DataPoint(0,0)});
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    protected void onPause() {
        super.onPause();
        senSensorManager.unregisterListener(this);
    }

    protected void onResume() {
        super.onResume();
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        //Sensor data
        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        //SEEKBAR Sample Rate
        sbRate = (SeekBar) findViewById(R.id.sbSampleRate);
        sbWindow = (SeekBar) findViewById(R.id.sbWindowSize);
        changeRateSample = (TextView) findViewById(R.id.currentSampleRate);
        changeWindowSize = (TextView) findViewById(R.id.currentWindowSize);

        //Chart
        graph = (GraphView) findViewById(R.id.graph);
        GraphView graph2 = (GraphView) findViewById(R.id.graph2);
        valuesX2 = new LineGraphSeries<>();
        graph2.addSeries(valuesX2);
        graph2.getViewport().setXAxisBoundsManual(true);
        graph2.getViewport().setMinX(0);
        graph2.getViewport().setMaxX(40);

        valuesX = new LineGraphSeries<>();
        valuesY = new LineGraphSeries<>();
        valuesZ = new LineGraphSeries<>();
        valuesSpeed = new LineGraphSeries<>();

        graph.addSeries(valuesX);
        graph.addSeries(valuesY);
        graph.addSeries(valuesZ);
        graph.addSeries(valuesSpeed);



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
