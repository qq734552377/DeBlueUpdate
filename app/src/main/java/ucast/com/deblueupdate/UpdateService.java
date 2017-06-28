package ucast.com.deblueupdate;

import android.app.Dialog;
import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.Date;

import ucast.com.deblueupdate.androidTools.AppInfo;
import ucast.com.deblueupdate.androidTools.MyTools;
import ucast.com.deblueupdate.app.ExceptionApplication;
import ucast.com.deblueupdate.filemanage.ListPictureQueue;
import ucast.com.deblueupdate.mytime.MyTimeTask;
import ucast.com.deblueupdate.mytime.MyTimer;
import ucast.com.deblueupdate.socket.NioTcpServer;

import static ucast.com.deblueupdate.androidTools.MyTools.isNetworkAvailable;

/**
 * Created by pj on 2016/11/21.
 */
public class UpdateService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return this.START_STICKY;
    }

    @Override
    public void onCreate() {

        startForeground(0, new Notification());

        //TODO 开启底座服务

        super.onCreate();
        ListPictureQueue.StartTimer();
        startTimer();

        NioTcpServer tcpServer = new NioTcpServer(43700);
        new Thread(tcpServer).start();
    }


    /**
     * 当服务被杀死时重启服务
     * */
    public void onDestroy() {
        stopForeground(true);
        Intent localIntent = new Intent();
        localIntent.setClass(this, UpdateService.class);
        this.startService(localIntent);    //销毁时重新启动Service
    }

    public MyTimer timer;
    public void startTimer() {
        timer = new MyTimer(new MyTimeTask(new Runnable() {
            @Override
            public void run() {
                String url="http://130.180.2.100:12500";
                getSystemTime(url.trim());
            }
        }), 1000*60*2L, 1000*60*60L);
        timer.initMyTimer().startMyTimer();
    }

    private static final String TAG = "UpdateService";
    public void getSystemTime(String url){
        if (!isNetworkAvailable(ExceptionApplication.getInstance())) {
            return;
        }
        RequestParams params = new RequestParams(url);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                String time=result.replace("\"","").trim();
                Log.e(TAG, "onSuccess: " +time);
                setTime(time);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }




    public void setTime(String mytime){
        Date mydate=StringToDate(mytime);
        long curMs=mydate.getTime();
        boolean isSuc = SystemClock.setCurrentTimeMillis(curMs);//需要Root权限
        Log.e(TAG, "setTime: "+isSuc );
    }
    private Date StringToDate(String s){
        Date time=null;
        SimpleDateFormat sd=new SimpleDateFormat("yyyyMMddHHmmss");
        try {
            time=sd.parse(s);
        } catch (java.text.ParseException e) {
            System.out.println("输入的日期格式有误！");
            e.printStackTrace();
        }
        return time;
    }
}
