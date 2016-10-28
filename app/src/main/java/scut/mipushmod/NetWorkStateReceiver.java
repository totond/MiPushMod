package scut.mipushmod;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Message;
import android.widget.Toast;

/**
 * 监听网络变化的Receiver
 */
public class NetWorkStateReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        System.out.println("网络状态发生变化");
        if(NetworkUtils.isWifiOrDataConn(context)){
            System.out.println("网络有连接，可以启动注册");
            BaseApplication.shouldListen = false;
            //回调注册
            Message message = BaseApplication.getsHandler().obtainMessage();
            message.what = 1;
            BaseApplication.getsHandler().sendMessage(message);
        }else {
            System.out.println("网络无连接");
        }
    }
}
