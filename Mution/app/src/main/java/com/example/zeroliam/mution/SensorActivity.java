package com.example.zeroliam.mution;

import com.example.zeroliam.mution.FFT;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;
import android.graphics.Color;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;


public class SensorActivity extends AppCompatActivity implements SensorEventListener{

    private SeekBar sbRate = null;
    private SeekBar sbWindow = null;
    private TextView changeRateSample, changeWindowSize;
    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    private long prevTime = 0;
    private float prevX, prevY, prevZ;
    private static final int SHAKE_THRESHOLD = 600;
    private int currentRate;
    private GraphView graph, fftgraph;
    //X, Y, Z Values
    private LineGraphSeries<DataPoint> valuesX, valuesY, valuesZ, valuesSpeed;
    //FFT for the X, Y, Z
    private LineGraphSeries<DataPoint> valuesFFTX, valuesFFTY, valuesFFTZ, valuesFFTSpeed;
    private FFT fftX,fftY, fftZ, fftSpeed;

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
                    fftX = new FFT(8);
//                    fftX.fft();
//                    DataPoint nexf = new DataPoint(getCTime, Double.parseDouble(String.valueOf(fftX)));
                    DataPoint ney = new DataPoint(getCTime, y);
                    DataPoint nez = new DataPoint(getCTime, z);
                    DataPoint nem = new DataPoint(getCTime, magnitude);

                    valuesX.appendData(nex, false, 100);
                    valuesX.setColor(Color.RED);
                    valuesX.setTitle("X");
//                    valuesFFTX.appendData(nexf, false, 100);
//                    valuesFFTX.setColor(Color.RED);
//                    valuesFFTX.setTitle("X FFT");

                    valuesY.appendData(ney, false, 100);
                    valuesY.setColor(Color.GREEN);
                    valuesY.setTitle("Y");

                    valuesZ.appendData(nez, false, 100);
                    valuesZ.setColor(Color.BLUE);
                    valuesZ.setTitle("Z");

                    valuesSpeed.appendData(nem, false, 100);
                    valuesSpeed.setColor(Color.BLACK);
                    valuesSpeed.setTitle("Speed");
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
        senSensorManager.unregisterListener(this);
    }

    protected void onResume() {
        super.onResume();
        senSensorManager.registerListener(this, senAccelerometer, getSensorRate(getCurrentRate()));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        //SEEKBAR Sample Rate
        currentRate = 0;
        sbRate = (SeekBar) findViewById(R.id.sbSampleRate);
        sbWindow = (SeekBar) findViewById(R.id.sbWindowSize);
        changeRateSample = (TextView) findViewById(R.id.currentSampleRate);
        changeWindowSize = (TextView) findViewById(R.id.currentWindowSize);
        changeRateSample.setText(getSensorRateName(getCurrentRate()));

        updateCustomSeekbar(sbRate, changeRateSample);
        updateCustomSeekbar(sbWindow, changeWindowSize);

        //Sensor data
        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer, getSensorRate(getCurrentRate()));

        //Chart for the accelerometer
        graph = (GraphView) findViewById(R.id.graph);

        valuesX = new LineGraphSeries<>();
        valuesY = new LineGraphSeries<>();
        valuesZ = new LineGraphSeries<>();
        valuesSpeed = new LineGraphSeries<>();

        graph.addSeries(valuesX);
        graph.addSeries(valuesY);
        graph.addSeries(valuesZ);
        graph.addSeries(valuesSpeed);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(-20);
        graph.getViewport().setMaxY(20);
        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
        staticLabelsFormatter.setHorizontalLabels(new String[] {"past", "read", "incoming"});
        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);

        //Chart for FFT
        fftgraph = (GraphView) findViewById(R.id.graphFFT);

        valuesFFTX = new LineGraphSeries<>();
//        valuesFFTY = new LineGraphSeries<>();
//        valuesFFTZ = new LineGraphSeries<>();
//        valuesFFTSpeed = new LineGraphSeries<>();

        fftgraph.addSeries(valuesFFTX);
//        fftgraph.addSeries(valuesFFTY);
//        fftgraph.addSeries(valuesFFTZ);
//        fftgraph.addSeries(valuesFFTSpeed);

    }

    private void updateCustomSeekbar(SeekBar sb, final TextView textb){

        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChanged = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
                progressChanged = progress;
                setCurrentRate(progress);
                SensorActivity.super.runOnUiThread(new workingSeekbar(getApplicationContext(), textb, progressChanged));
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void setCurrentRate(int newRate){
        currentRate = newRate;
    }

    public int getCurrentRate(){
        return currentRate;
    }

    public int getSensorRate(int prog){
        int ret = 0;
        switch (prog){
            case 0:
                ret = SensorManager.SENSOR_DELAY_FASTEST;
                break;
            case 1:
                ret = SensorManager.SENSOR_DELAY_GAME;
                break;
            case 2:
                ret = SensorManager.SENSOR_DELAY_NORMAL;
                break;
            case 3:
                ret = SensorManager.SENSOR_DELAY_UI;
                break;
            default:
                ret = SensorManager.SENSOR_DELAY_NORMAL;
                break;
        }

        return ret;
    }

    public String getSensorRateName(int prog){
        String ret = "";
        switch (prog){
            case 0:
                ret = "SENSOR_DELAY_FASTEST";
                break;
            case 1:
                ret = "SENSOR_DELAY_GAME";
                break;
            case 2:
                ret = "SENSOR_DELAY_NORMAL";
                break;
            case 3:
                ret = "SENSOR_DELAY_UI";
                break;
            default:
                ret = "SENSOR_DELAY_NORMAL";
                break;
        }

        return ret;
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
            setCurrentRate(newProgress);
            showTxtProgress = newTxtProgress;
        }

        @Override
        public void run() {
            //Make the Toast with the values received as parameters
            showTxtProgress.setText(String.valueOf(getSensorRateName(currentProgress)));

        }
    }
}
