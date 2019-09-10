package com.example.tianmaoautoclick;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.util.Log;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.yhao.floatwindow.FloatWindow;
import com.yhao.floatwindow.PermissionListener;
import com.yhao.floatwindow.Screen;

import java.util.List;


public class MyAccessibilityService extends AccessibilityService {

    AccessibilityEvent event;


    boolean isshoping  =false ;
    Long shoppingtime  = null ;
    private Button button;

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        AccessibilityServiceInfo config = new AccessibilityServiceInfo();
        //配置监听的事件类型为界面变化|点击事件
//        config.eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED | AccessibilityEvent.TYPE_VIEW_CLICKED;
//        config.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
        Log.i("天猫辅助器", "服务开启了");
//        if (Build.VERSION.SDK_INT >= 16) {
//            config.flags = AccessibilityServiceInfo.FLAG_INCLUDE_NOT_IMPORTANT_VIEWS;
//        }
//        setServiceInfo(config);
        click();
//
        new CountDownTimer(1000*60*60*60, 1) {
            @Override
            public void onTick(long l) {
                click();
            }

            @Override
            public void onFinish() {

            }
        }.start();


    }


    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        this.event = event;
        click();

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
//
//        }

    }

    /**
     * 自动清空购物车
     */
    private void click() {
        if (isshoping && shoppingtime!=null && System.currentTimeMillis()>=shoppingtime) {
            if (button!=null){
                button.setText("正在秒杀！");
            }
            AccessibilityNodeInfo rootInActiveWindow = getRootInActiveWindow();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                //确定清空
                try {
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
                } catch (Exception e) {

                }

                //滑动验证


                //提交订单
                try {
                    List<AccessibilityNodeInfo> tijiaodingdan = getRootInActiveWindow().findAccessibilityNodeInfosByText("提交订单");
                    for (AccessibilityNodeInfo tj : tijiaodingdan) {
                        tj.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    }

                } catch (Exception e) {

                }


            } else {
                Toast.makeText(this, "对不起您的手机版本过低", Toast.LENGTH_SHORT).show();
            }
        }else {
            if (button!=null && shoppingtime!=null){
                button.setText( shoppingtime-System.currentTimeMillis()+"");
            }
        }
    }





        private ActivityInfo tryGetActivity (ComponentName componentName){
            try {
                return getPackageManager().getActivityInfo(componentName, 0);
            } catch (PackageManager.NameNotFoundException e) {
                return null;
            }
        }

        @Override
        public void onInterrupt () {
        }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent!=null){
            long time = intent.getLongExtra("time",0);

               if (time != 0) {
                   isshoping = true;
                   shoppingtime = time;
                   showFloatingWindow();
               }

            Log.i("天猫辅助器", "onStartCommand: 秒杀时间为"+time);
        }else {
            Log.i("天猫辅助器", "onStartCommand: 秒杀传来的时间为null");
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        isshoping =false ;
        shoppingtime = null ;
        Toast.makeText(this, "天猫秒杀已关闭", Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }



    private void showFloatingWindow() {
        if (button==null) {
            // 新建悬浮窗控件
            button = new Button(getApplicationContext());
            button.setText("等待秒杀");
            button.setBackgroundColor(Color.BLACK);
            button.setTextColor(Color.WHITE);
            button.setPadding(10, 10, 10, 10);
            FloatWindow
                    .with(getApplicationContext())
                    .setView(button)
                    .setWidth(Screen.width, 0.2f)            //设置控件宽高
                    .setHeight(Screen.width, 0.2f)
                    .setX(100)                                   //设置控件初始位置
                    .setY(Screen.height, 0.3f)
                    .setDesktopShow(true)                        //桌面显示
//                .setViewStateListener(mViewStateListener)    //监听悬浮控件状态改变
                    .setPermissionListener(new PermissionListener() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onFail() {
                            Toast.makeText(MyAccessibilityService.this, "请您打开悬浮窗权限", Toast.LENGTH_SHORT).show();
                        }
                    })  //监听权限申请结果
                    .build();
        }
    }
}
