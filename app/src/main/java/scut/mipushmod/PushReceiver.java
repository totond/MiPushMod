package scut.mipushmod;

import android.content.Context;
import android.os.Message;
import android.util.Log;

import com.xiaomi.mipush.sdk.ErrorCode;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.xiaomi.mipush.sdk.MiPushCommandMessage;
import com.xiaomi.mipush.sdk.MiPushMessage;
import com.xiaomi.mipush.sdk.PushMessageReceiver;

import java.util.List;

/**
 * Created by yany on 2016/10/27.
 */
public class PushReceiver extends PushMessageReceiver {
    @Override
    public void onReceivePassThroughMessage(Context context, MiPushMessage miPushMessage) {
        //用来接收服务器向客户端发送的透传消息。
        System.out.println("收到透传消息");
    }

    @Override
    public void onNotificationMessageClicked(Context context, MiPushMessage miPushMessage) {
        //用来接收服务器向客户端发送的通知消息，这个回调方法会在用户手动点击通知后触发。
        System.out.println("点击了推送消息");
        System.out.println(miPushMessage.toString());
    }


    @Override
    public void onNotificationMessageArrived(Context context, MiPushMessage miPushMessage) {
        //用来接收服务器向客户端发送的通知消息，这个回调方法是在通知消息到达客户端时触发。另外应用在前台时不弹出通知的通知消息到达客户端也会触发这个回调函数
        System.out.println("收到通知信息消息");
    }

    //这里接收推送客户端想服务端发送命令的响应结果
    @Override
    public void onCommandResult(Context context, MiPushCommandMessage message) {
        String command = message.getCommand();
        List<String> arguments = message.getCommandArguments();
        String cmdArg1 = ((arguments != null && arguments.size() > 0) ? arguments.get(0) : null);
        if (MiPushClient.COMMAND_REGISTER.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                System.out.println("注册推送成功，mRegId =" + cmdArg1);
                Message msg = BaseApplication.getsHandler().obtainMessage();
                msg.what = 0;
                BaseApplication.getsHandler().sendMessage(msg);

            } else {
                System.out.println("注册推送失败，重新注册");
                Message msg = BaseApplication.getsHandler().obtainMessage();
                msg.what = 1;
                //延迟5秒后再重新注册
                BaseApplication.getsHandler().sendMessageDelayed(msg,5000);
            }
        } else {
            System.out.println("其他:" + message.getReason());
        }

    }
}
