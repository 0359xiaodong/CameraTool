package com.example.houwenbo.sensorlistener;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

/**
 * Created by houwenbo on 2014/10/21.
 */
public class PhoneSensorListener implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mRotationVectorSensor;
    private Context mContext;
    private float[] mRotationVector = new float[4];
    public final float[] mRotationMatrix = new float[9];

    public PhoneSensorListener(Context context)
    {
        mContext = context;
        enableSensor();
    }

    public void enableSensor() {

        mSensorManager = (SensorManager) mContext
                .getSystemService(Context.SENSOR_SERVICE);

        // find the rotation-vector sensor
        mRotationVectorSensor = mSensorManager.getDefaultSensor(
                Sensor.TYPE_ROTATION_VECTOR);

        if (mSensorManager == null) {
            Log.v("sensor..", "Sensors not supported");
        }

        mSensorManager.registerListener(this, mRotationVectorSensor,
                SensorManager.SENSOR_DELAY_NORMAL);

    }

    public void disableSensor() {
        if (mSensorManager != null) {
            mSensorManager.unregisterListener(this);
            mSensorManager = null;
        }
    }

    public void start() {
        // enable our sensor when the activity is resumed, ask for
        // 10 ms updates.
        mSensorManager.registerListener(this, mRotationVectorSensor, 10000);
    }

    public void stop() {
        // make sure to turn our sensor off when the activity is paused
        mSensorManager.unregisterListener(this);
    }

    public void onSensorChanged(SensorEvent event) {
        // we received a sensor event. it is a good practice to check
        // that we received the proper event
        if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            // convert the rotation-vector to a 4x4 matrix. the matrix
            // is interpreted by Open GL as the inverse of the
            // rotation-vector, which is what we want.
            SensorManager.getRotationMatrixFromVector(
                    mRotationMatrix , event.values);

            mRotationVector = event.values;
        }
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
