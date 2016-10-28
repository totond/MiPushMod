package scut.mipushmod;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.Message;
import android.os.Process;

import com.xiaomi.mipush.sdk.MiPushClient;

import java.util.List;

/**
 * Created by yany on 2016/10/27.
 */
public class BaseApplication extends Application{
    // user your appid the key.
    private static final String APP_ID = "2882303761517520836";
    // user your appid the key.
    private static final String APP_KEY = "5931752033836";
    // 此TAG在adb logcat中检索自己所需要的信息， 只需在命令行终端输入 adb logcat | grep
    // com.xiaomi.mipushdemo
    public static final String TAG = "scut.mipushmod";

    //检测是否需要开启监听的变量
    public static boolean shouldListen = false;
    private static AppHandler sHandler = null;

    private NetWorkStateReceiver netWorkStateReceiver;
    //检测netWorkStateReceiver广播是否已注册的变量
    public static boolean isReceiverReg = false;


    @Override
    public void onCreate() {
        super.onCreate();

        // 注册push服务，注册成功后会向DemoMessageReceiver发送广播
        // 可以从DemoMessageReceiver的onCommandResult方法中MiPushCommandMessage对象参数中获取注册信息
        if(NetworkUtils.isWifiOrDataConn(this)){
            System.out.println("网络有连接，可以启动注册");
            startPush();
        }else {
            System.out.println("网络无连接，启动监听");
            shouldListen = true;
            regBroadcastReceiver();
        }
        //实例化Handler
        if (sHandler == null){
            sHandler = new AppHandler();
        }
    }

    @Override
    public void onTerminate() {
        // 程序终止的时候执行
        System.out.println("程序终止的时候执行");
        releaseHandler();
        unRegBroadcastReceiver();
        super.onTerminate();
    }
    @Override
    public void onLowMemory() {
        // 低内存的时候执行
        System.out.println("低内存的时候执行");
        releaseHandler();
        unRegBroadcastReceiver();
        super.onLowMemory();
    }
    @Override
    public void onTrimMemory(int level) {
        // 程序在内存清理的时候执行
        System.out.println("程序在内存清理的时候执行");
        releaseHandler();
        unRegBroadcastReceiver();
        super.onTrimMemory(level);
    }

    //检测需不需要初始化小米推送服务
    private boolean shouldInit() {
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getPackageName();
        int myPid = Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }

    //启动推送
    public void startPush(){
        if (shouldInit()) {
            System.out.println("启动注册");
            MiPushClient.registerPush(this, APP_ID, APP_KEY);
        }
    }

    //释放Handler，防止内存泄漏
    public static AppHandler getsHandler(){
        return sHandler;
    }


    //注册广播
    public void regBroadcastReceiver(){
        if (netWorkStateReceiver == null) {
            netWorkStateReceiver = new NetWorkStateReceiver();
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(netWorkStateReceiver, filter);
        isReceiverReg = true;
        System.out.println("注册广播");
    }

    //注销广播
    public void unRegBroadcastReceiver(){
        if (isReceiverReg) {
            if (netWorkStateReceiver.isInitialStickyBroadcast())
                unregisterReceiver(netWorkStateReceiver);
            System.out.println("注销广播");
            isReceiverReg = false;
        }
    }

    private void releaseHandler(){
        if (sHandler != null){
            sHandler.removeCallbacksAndMessages(null);
        }
    }

    //用于广播接收器开启回调
    class AppHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    System.out.println("从回调启动注册");
                    startPush();
                    break;
                case 0:
                    //注销广播
                    unRegBroadcastReceiver();
                    break;
                case 2:
                    //启动广播
                    regBroadcastReceiver();
                    break;
            }
        }
    }
}
