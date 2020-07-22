package com.sedlo.mapp1.Activity;

import android.content.Intent;

import android.graphics.PixelFormat;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.john.waveview.WaveView;
import com.sedlo.mapp1.UserConnection.PrefManager;
import com.sedlo.mapp1.R;



public class MainActivity extends AppCompatActivity  {




    @Override
    protected void onResume() {

        super.onResume();


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        getWindow().setFormat(PixelFormat.TRANSLUCENT);



        final WaveView waveView =(WaveView)findViewById(R.id.waveView);

        waveView.setProgress(50);

        SensorManager sensorManager =
                (SensorManager) getSystemService(SENSOR_SERVICE);

        Sensor gyroscopeSensor =
                sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

       SensorEventListener gyroscopeSensorListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent)
            {

                waveView.setRotation(waveView.getRotation()+sensorEvent.values[2]/20);

            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
            }
        };


        sensorManager.registerListener(gyroscopeSensorListener,
                gyroscopeSensor, SensorManager.SENSOR_DELAY_FASTEST);


        init();
    }


    void init(){

        Button profile = findViewById(R.id.btn_profile);
        Button login = findViewById(R.id.btn_login);
        Button sign_up = findViewById(R.id.btn_sign_up);
        PrefManager prefManager = PrefManager.getInstance(MainActivity.this);
        if(prefManager.isLoggedIn())
        {
            profile.setText(prefManager.getUser().getUsername());
            login.setVisibility(View.GONE);
            sign_up.setVisibility(View.GONE);
            profile.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                }
            });
        } else {
            profile.setVisibility(View.GONE);
            login.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }
            });
            sign_up.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this, SignUpActivity.class));
                }
            });
        }
    }
}