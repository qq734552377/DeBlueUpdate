package ucast.com.deblueupdate.socket.MessageProtocol;


import android.util.Log;

import io.netty.channel.Channel;
import ucast.com.deblueupdate.socket.Message.ApkVersion;
import ucast.com.deblueupdate.socket.Message.AppVersion;
import ucast.com.deblueupdate.socket.Message.FileSendMessage;
import ucast.com.deblueupdate.socket.Message.FileSendNextMessage;
import ucast.com.deblueupdate.socket.Message.GeMAcMessage;
import ucast.com.deblueupdate.socket.Message.GetLogsMessage;
import ucast.com.deblueupdate.socket.Message.HeartBeat;
import ucast.com.deblueupdate.socket.Message.MessageBase;
import ucast.com.deblueupdate.socket.Message.UpdateMessage;
import ucast.com.deblueupdate.socket.Message.UploadLogMessage;

import static android.content.ContentValues.TAG;

/**
 * Created by Administrator on 2016/2/3.
 */
public class StationPackage extends Package {

    private StringBuffer sBuffer;

    public StationPackage(Channel _channel) {
        super(_channel);
        sBuffer = new StringBuffer();
    }

    @Override
    public void Import(byte[] buffer, int Offset, int count) throws Exception {
        sBuffer.append(new String(buffer));
        int offset = 0;
        while (sBuffer.length() > offset&&!mDispose) {
            int startIndex = sBuffer.indexOf("@", offset);
            if (startIndex == -1)
                break;

            int endIndex = sBuffer.indexOf("$", startIndex);
            if (endIndex == -1)
                break;
            int len = endIndex + 1;


            String value = sBuffer.substring(startIndex, len);
            OnMessageDataReader(value);
            offset = len;
        }
        sBuffer.delete(0, offset);
    }

    @Override
    public MessageBase MessageRead(byte[] data) {
        return null;
    }

    public MessageBase MessageRead(String value) throws Exception {
        String msg = value.substring(1, value.length() - 1);
        String[] item = msg.split(",");
        MessageBase mbase = null;
        switch (item[0]) {
            case "s001"://请求底座app的版本号
                mbase=new AppVersion();
                break;
            case "s002"://请求底座的apk文件的版本号
                mbase=new ApkVersion();
                break;
            case "s003"://更新底座apk
                mbase=new UpdateMessage();
                break;
            case "1105":
                mbase=new HeartBeat();
                break;
            case "s004"://请求底座的所有日志
                mbase=new GetLogsMessage();
                break;
            case "s005"://上传指定的日志文件
                mbase=new UploadLogMessage();
                break;
            case "sw001"://请求文件
                mbase=new FileSendMessage();
                break;
            case "sw002"://发送文件下一包
                mbase=new FileSendNextMessage();
                break;

            case "getmac"://获取网线Mac
                mbase=new GeMAcMessage();
                break;
            default:
                break;


        }
        if (mbase == null)
            return null;
        mbase.Load(item);
        return mbase;
    }

}
