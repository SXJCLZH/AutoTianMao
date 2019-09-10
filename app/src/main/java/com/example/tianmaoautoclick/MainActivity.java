package com.example.tianmaoautoclick;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.yhao.floatwindow.FloatWindow;
import com.yhao.floatwindow.PermissionListener;
import com.yhao.floatwindow.Screen;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {


    private Button button;
    private Intent tianMaoServerIntent;

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

                    tianMaoServerIntent = new Intent(MainActivity.this, MyAccessibilityService.class);
                    tianMaoServerIntent.putExtra("time", date.getTime());

                    startService(tianMaoServerIntent);
                    Toast.makeText(MainActivity.this, "请打开天猫购物车界面进行等待", Toast.LENGTH_SHORT).show();
//                    去购物车界面
                    try {
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.VIEW");
                        String url = "tmall://page.tm/cart";
                        Uri uri = Uri.parse(url);
                        intent.setData(uri);
                        startActivity(intent);
                    }catch (Exception e) {
                        Toast.makeText(MainActivity.this, "您未安装天猫APP！！！", Toast.LENGTH_SHORT).show();
                    }
                    setXuanFu();
                }


            }
        });

        findViewById(R.id.guanbi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tianMaoServerIntent!=null) {
                    stopService(tianMaoServerIntent);
                }
                System.exit(0);
            }
        });


        findViewById(R.id.setting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                startActivity(intent1);
            }
        });


    }

    /**
     * 设置悬浮窗
     */
    private void setXuanFu() {

    }
}
