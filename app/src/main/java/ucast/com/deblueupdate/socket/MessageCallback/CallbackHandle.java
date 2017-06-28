package ucast.com.deblueupdate.socket.MessageCallback;

import android.os.Environment;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import ucast.com.deblueupdate.UpdateService;
import ucast.com.deblueupdate.androidTools.ApkInfo;
import ucast.com.deblueupdate.androidTools.AppInfo;
import ucast.com.deblueupdate.androidTools.MyTools;
import ucast.com.deblueupdate.androidTools.ResponseEntity;
import ucast.com.deblueupdate.app.ExceptionApplication;
import ucast.com.deblueupdate.filemanage.ListPictureQueue;
import ucast.com.deblueupdate.filemanage.ReadPicture;
import ucast.com.deblueupdate.filemanage.ReadPictureManage;
import ucast.com.deblueupdate.socket.Common;
import ucast.com.deblueupdate.socket.Message.ApkVersion;
import ucast.com.deblueupdate.socket.Message.FileSendMessage;
import ucast.com.deblueupdate.socket.Message.FileSendNextMessage;
import ucast.com.deblueupdate.socket.Message.GetLogsMessage;
import ucast.com.deblueupdate.socket.Message.HeartBeat;
import ucast.com.deblueupdate.socket.Message.MessageBase;
import ucast.com.deblueupdate.socket.Message.UpdateMessage;
import ucast.com.deblueupdate.socket.Message.UploadLogMessage;

import static android.R.attr.path;

/**
 * Created by Administrator on 2016/2/4.
 */
public class CallbackHandle implements IMsgCallback {
    public static final String LOG_PATH = Environment.getExternalStorageDirectory().toString() + "/Ucast";

    public void Receive(Channel _channel, Object obj) {
        if (obj == null)
            return;
        if (!(obj instanceof MessageBase))
            return;
        MessageBase msgbase = (MessageBase) obj;
        switch (msgbase.Cmd) {
            case "s001":
                //TODO 发送当前的底座版本号
                String version = AppInfo.getVersionName(ExceptionApplication.getInstance(), "jni.ucab.ucast.deblue");
                String str = "@p001," + version + "$";
                Common.ServicesAllSend(str.getBytes());
                break;
            case "s002":
                //TODO 查询底座是否有最新的apk文件
                doQueryApk((ApkVersion) msgbase);
                break;
            case "s003":
                doUpdate((UpdateMessage) msgbase);
                break;
            case "1105":
                SendHeartBeat((HeartBeat) obj, _channel);
                break;
            case "s004"://请求底座的所有日志
                doQuerryLogsFile();
                break;
            case "s005"://上传指定的日志文件
                doUpload((UploadLogMessage) msgbase);
                break;
            case "sw001"://请求文件
                doSendFile((FileSendMessage) msgbase);
                break;
            case "sw002"://发送文件下一包
                int index=((FileSendNextMessage) msgbase).number_bao;
                Log.e(TAG, "Receive: 发送文件下一包  "+index );
                ListPictureQueue.Send(index);
                break;
            case "getmac"://获取网线Mac
                String mac=MyTools.getMacAddress();
                String str1="@mac,"+mac+"$";
                Common.ServicesAllSend(str1.getBytes());
                break;
            default:
                break;
        }
    }

    private void doSendFile(FileSendMessage msg) {
        String fileName = msg.fileName;
        String path = LOG_PATH + "/" + fileName;
        ReadPicture picture = ReadPictureManage.GetInstance().GetReadPicture(0);
        if (picture == null)
            return;
        picture.Add(path);
    }


    private void doUpload(UploadLogMessage msg) {
//        String url = "http://192.168.0.32:12997/Log/zy/dizuo";
        List<String> logs = msg.logs;
        String url=msg.url;
        for (String log : logs) {
            String path = LOG_PATH + "/" + log;
            Log.e(TAG, "doUpload: " + path);
            MyTools.upload(path, url);

        }

    }

    private void doQuerryLogsFile() {
        File path_file = new File(LOG_PATH);
        File[] files = path_file.listFiles();
        List<String> log_files = new ArrayList<>();
        for (File f : files) {
            if (f.isFile() && (f.getName().contains(".log"))) {
                log_files.add(f.getName());
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append("@p005,");
        for (int i = 0; i < log_files.size(); i++) {
            if (i == log_files.size() - 1) {
                sb.append(log_files.get(i) + "$");
            } else {
                sb.append(log_files.get(i) + ",");
            }
        }
        String logs_str = sb.toString();
        Log.e(TAG, "doQuerryLogsFile: " + logs_str);
        Common.ServicesAllSend(logs_str.getBytes());
    }

    private static final String TAG = "CallbackHandle";

    private void doQueryApk(ApkVersion msgbase) {
        String str1 = "@p003,1$";

        String new_version = msgbase.version;
        String url = msgbase.url;
        long new_size = msgbase.size;
        long apk_size = 0;
        Log.e(TAG, "doQueryApk " + new_version + "  " + new_size + "  " + url);
        try {
            apk_size = MyTools.getFileSizes(ApkInfo.apkPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (new_size == apk_size) {
            String apk_version = MyTools.getApkversion();
            if (apk_version.equals(new_version)) {
                Common.ServicesAllSend(str1.getBytes());
            } else {
                //版本不对下载文件
                Log.e(TAG, "doQueryApk 版本不对下载文件" + url);
                MyTools.downloadFile(url, ApkInfo.apkPath);
            }
        } else {
            //TODO 下载最新的apk文件
            Log.e(TAG, "doQueryApk 开始下载" + url);
            MyTools.downloadFile(url, ApkInfo.apkPath);
        }
    }

    private void doUpdate(UpdateMessage msgbase) {
        if (msgbase.isUpdate) {
            //TODO 执行更新操作
            MyTools.detele("jni.ucab.ucast.deblue");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            MyTools.install(ApkInfo.apkPath);
        } else {
            //TODO 什么也不做

        }

    }


    private void SendHeartBeat(HeartBeat msg, Channel channel) {
        String heart = "@1005," + "1223334444" + "$";
        ByteBuf resp = Unpooled.copiedBuffer(heart.getBytes());
        channel.writeAndFlush(resp);
    }

}
