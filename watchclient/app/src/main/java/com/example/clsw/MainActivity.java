package com.example.clsw;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.clsw.databinding.ActivityMainBinding;

public class MainActivity extends Activity implements SensorEventListener {

    ActivityReporter reporter;

    private SensorManager sensorMan;
    private Sensor accelerometer;

    private double mAccel;
    private double mAccelCurrent;
    private double mAccelLast;

    String sessionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sessionId = SessionNameGenerator.generateSessionName();

        TextView mTextView = binding.textView;
        mTextView.setText(sessionId);
        reporter = new ActivityReporter(sessionId);

        sensorMan = (SensorManager)getSystemService(SENSOR_SERVICE);
        accelerometer = sensorMan.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorMan.registerListener((SensorEventListener) this, accelerometer,
                SensorManager.SENSOR_DELAY_UI);
    }


    @Override
    protected void onPause() {
        super.onPause();
        sensorMan.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            float[] mGravity = event.values.clone();
            // Shake detection
            float x = mGravity[0];
            float y = mGravity[1];
            float z = mGravity[2];
            mAccelLast = mAccelCurrent;
            mAccelCurrent = Math.sqrt(x*x + y*y + z*z);
            double delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta;
            if(mAccel > 1) {
                Log.d(TAG, "Move detected");
                new Thread(() -> {
                    reporter.reportToBackend();
                }).start();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // required method
    }
}