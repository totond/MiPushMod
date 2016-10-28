package scut.mipushmod;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println("MainActivity Create！");
    }

    @Override
    protected void onResume() {
        if (BaseApplication.shouldListen){
            if (!BaseApplication.isReceiverReg){
                Message msg = BaseApplication.getsHandler().obtainMessage();
                msg.what = 2;
                BaseApplication.getsHandler().sendMessage(msg);
                BaseApplication.isReceiverReg = true;
            }
        }
        super.onResume();
    }
}
