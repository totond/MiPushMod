package scut.mipushmod;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;

/**
 * Created by yany on 2016/10/27.
 */
public class NetworkUtils {
    //检测是否有Wifi或移动数据连接
    public static boolean isWifiOrDataConn(Context context){
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //根据手机的Android API版本来决定用哪种方式获取网络状态
        //检测API是不是小于21，因为到了API21之后getNetworkInfo(int networkType)方法被弃用
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
            //获取WIFI连接的信息
            NetworkInfo networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            boolean isWifiConn = networkInfo.isConnected();
            //获取移动数据连接的信息
            networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            boolean isMobileConn = networkInfo.isConnected();
            return isMobileConn || isWifiConn;
        }else {
            //因为还没有经过Android5.0测试调整，现在暂时只能检测是否有网络连接，包括Wifi，移动数据，蓝牙等
            boolean isConn = false;
            Network[] networks = connMgr.getAllNetworks();
            for (int i=0; i < networks.length; i++){
                NetworkInfo networkInfo = connMgr.getNetworkInfo(networks[i]);
                if (networkInfo.isConnected()){
                    isConn = true;
                    break;
                }
            }
            return isConn;
        }
    }


}
