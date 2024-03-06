package helloandroid.ut3.mini_projet;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Vibrator;
import android.content.Context;

import helloandroid.ut3.mini_projet.activity.DisplayImageActivity;

public class Shaker implements SensorEventListener {

    private SensorManager sensorManager;
    private Vibrator vibrator;
    private float shakeThreshold = 10.0f; // Seuil de détection de secousse

    private DisplayImageActivity displayImageActivity;

    public Shaker(DisplayImageActivity displayImageActivity) {
        sensorManager = (SensorManager) displayImageActivity.getSystemService(Context.SENSOR_SERVICE);
        vibrator = (Vibrator) displayImageActivity.getSystemService(Context.VIBRATOR_SERVICE);
        this.displayImageActivity=displayImageActivity;
    }

    public void start() {
        Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (accelerometerSensor != null) {
            sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_UI);
        }
    }

    public void stop() {
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            float accelerationSquareRoot = (x * x + y * y + z * z) / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);

            if (accelerationSquareRoot >= shakeThreshold) {
                // Secousse détectée
                vibrator.vibrate(100); // Fait vibrer le téléphone pendant 100 millisecondes
                displayImageActivity.onShakeDetected();
                // Ajoutez ici le code à exécuter lorsqu'une secousse est détectée
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
