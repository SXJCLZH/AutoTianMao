package com.example.tianmaoautoclick;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.IntentService;
import android.content.ComponentName;
import android.content.Intent;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.util.List;


public class MyAccessibilityService extends AccessibilityService {

    AccessibilityEvent event;
    int i = 10;

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        AccessibilityServiceInfo config = new AccessibilityServiceInfo();
        //配置监听的事件类型为界面变化|点击事件
        config.eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED | AccessibilityEvent.TYPE_VIEW_CLICKED;
        config.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;

//        if (Build.VERSION.SDK_INT >= 16) {
//            config.flags = AccessibilityServiceInfo.FLAG_INCLUDE_NOT_IMPORTANT_VIEWS;
//        }
//        setServiceInfo(config);
//        CountDownTimer start = new CountDownTimer(10000, 1000) {
//            @Override
//            public void onTick(long l) {
//                i = i - 1;
//                Toast.makeText(MyAccessibilityService.this, "距离定时还有" + i + "秒", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onFinish() {
//                Toast.makeText(MyAccessibilityService.this, "开始抢啦！！", Toast.LENGTH_SHORT).show();
//                onAccessibilityEvent(event);
//            }
//        }.start();
//

    }


    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        this.event = event;
        Toast.makeText(this, "开始执行", Toast.LENGTH_SHORT).show();
        AccessibilityNodeInfo rootInActiveWindow = getRootInActiveWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            List<AccessibilityNodeInfo> accessibilityNodeInfosByViewId = rootInActiveWindow.findAccessibilityNodeInfosByViewId("com.tmall.wireless:id/checkbox_charge");
            if (accessibilityNodeInfosByViewId.size() != 0) {
                AccessibilityNodeInfo accessibilityNodeInfo = accessibilityNodeInfosByViewId.get(0);
                if (!accessibilityNodeInfo.isChecked()) {
                    accessibilityNodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                }
            }

            List<AccessibilityNodeInfo> jiesuan = rootInActiveWindow.findAccessibilityNodeInfosByViewId("com.tmall.wireless:id/button_cart_charge");
            for (AccessibilityNodeInfo accessibilityNodeInfo : jiesuan) {
                 accessibilityNodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);

            }

            List<AccessibilityNodeInfo> tijiaodingdan = gettijiaodingdan(rootInActiveWindow);
            for (AccessibilityNodeInfo tj : tijiaodingdan) {
                tj.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }

        } else {
            Toast.makeText(this, "对不起您的手机版本过低", Toast.LENGTH_SHORT).show();
        }




//        AccessibilityNodeInfo nodeInfo = event.getSource();//当前界面的可访问节点信息
//        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {//界面变化事件
//            ComponentName componentName = new ComponentName(event.getPackageName().toString(), event.getClassName().toString());
//            ActivityInfo activityInfo = tryGetActivity(componentName);
//            boolean isActivity = activityInfo != null;
//            if (isActivity) {
//                Toast.makeText(this, "是否窗口"+isActivity, Toast.LENGTH_SHORT).show();
//
//            }else {
//
//                Toast.makeText(this, "获取不到程序信息", Toast.LENGTH_LONG).show();
//            }
//        }
//
//        //监听第三方点击事件
//        if (event.getEventType() == AccessibilityEvent.TYPE_VIEW_CLICKED) {
//            //View点击事件
//            Toast.makeText(this, nodeInfo.getText()+"被点击了", Toast.LENGTH_SHORT).show();
//            Log.i(TAG, "onAccessibilityEvent: " + nodeInfo.getText());
//            if ((nodeInfo.getText() + "").equals("模拟点击")) {
//                Toast.makeText(this, "这是来自监听Service的响应！", Toast.LENGTH_SHORT).show();
//                Log.i(TAG, "onAccessibilityEvent: 这是来自监听Service的响应！");
//            }
//        }
    }

    private List<AccessibilityNodeInfo> gettijiaodingdan(AccessibilityNodeInfo rootInActiveWindow) {
        List<AccessibilityNodeInfo> tijiao = rootInActiveWindow.findAccessibilityNodeInfosByText("提交订单");
        return tijiao;
    }

    private ActivityInfo tryGetActivity(ComponentName componentName) {
        try {
            return getPackageManager().getActivityInfo(componentName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }

    @Override
    public void onInterrupt() {
    }

}
