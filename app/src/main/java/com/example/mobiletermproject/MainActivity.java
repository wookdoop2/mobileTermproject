package com.example.mobiletermproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity {

    private static final int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 1;
    private Intent intent;
    private Switch overlaySwitch;
    private Switch tapSwitch;
    private Switch volumeSwitch;
    private SeekBar seekTrans;
    private SharedPreferences transparency;
    private SharedPreferences.Editor editTrans;
    private SharedPreferences tapPref;
    private SharedPreferences.Editor editTap;
    private SharedPreferences volumePref;
    private SharedPreferences.Editor editVolume;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        overlaySwitch = findViewById(R.id.switch1);
        overlaySwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId() == R.id.switch1){
                    if(overlaySwitch.isChecked()){
                        intent = new Intent(MainActivity.this, MyService.class);
                        checkPermission();
                    } else {
                        stopService(intent);
                    }
                }
            }
        });

        tapPref = getSharedPreferences("tapSwitch", Context.MODE_PRIVATE);
        editTap = tapPref.edit();
        tapSwitch = findViewById(R.id.switch2);
        tapSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId() == R.id.switch2){
                    if(tapSwitch.isChecked()){
                        editTap.putInt("tapSwitch", 0);
                        editTap.apply();
                    } else{
                        editTap.putInt("tapSwitch", 1);
                        editTap.apply();
                    }
                }
            }
        });

        volumePref = getSharedPreferences("volumeSwitch", Context.MODE_PRIVATE);
        editVolume = volumePref.edit();
        volumeSwitch = findViewById(R.id.switch3);
        volumeSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId() == R.id.switch3){
                    if(volumeSwitch.isChecked()){
                        editVolume.putInt("volumeSwitch", 0);
                        editVolume.apply();
                    } else{
                        editVolume.putInt("volumeSwitch", 1);
                        editVolume.apply();
                    }
                }
            }
        });

        seekTrans = findViewById(R.id.seekTrans);
        seekTrans.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                transparency = getSharedPreferences("transparency", Context.MODE_PRIVATE);
                editTrans = transparency.edit();
                editTrans.putInt("transparency", progress);
                editTrans.apply();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {   // 마시멜로우 이상일 경우
            if (!Settings.canDrawOverlays(this)) {              // 체크
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE);
            } else {
                startService(intent);
            }
        } else {
            startService(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE){
            if (Settings.canDrawOverlays(this)) {              // 체크
                startService(intent);
            } else{
                checkPermission();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(intent);
    }
}
