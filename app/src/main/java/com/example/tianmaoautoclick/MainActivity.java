package com.example.tianmaoautoclick;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {


    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!Utils.isAccessibilitySettingsOn(MainActivity.this, MyAccessibilityService.class.getCanonicalName())) {
            Intent intent1 = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            startActivity(intent1);
            Toast.makeText(MainActivity.this, "请开启辅助功能", Toast.LENGTH_SHORT).show();
        }

        final TimePicker timePicker = findViewById(R.id.timepicker);
        timePicker.setIs24HourView(false);
        button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer currentHour = timePicker.getCurrentHour();
                Integer currentMinute = timePicker.getCurrentMinute();
                Date date = new Date(System.currentTimeMillis());
                int s = 0;
                date.setMinutes(currentMinute);
                date.setHours(currentHour);
                date.setSeconds(s);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


                if (!Utils.isAccessibilitySettingsOn(MainActivity.this, MyAccessibilityService.class.getCanonicalName())) {
                    Intent intent1 = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                    startActivity(intent1);
                    Toast.makeText(MainActivity.this, "未开启辅助功能", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent2 = new Intent(MainActivity.this, MyAccessibilityService.class);
                    startService(intent2);


                    //去购物车界面
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    String url = "tmall://page.tm/cart";
                    Uri uri = Uri.parse(url);
                    intent.setData(uri);
                    startActivity(intent);
                }


            }
        });


    }
}
