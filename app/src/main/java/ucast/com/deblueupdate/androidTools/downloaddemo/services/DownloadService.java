package ucast.com.deblueupdate.androidTools.downloaddemo.services;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by pj on 2016/11/22.
 */
public class DownloadService extends Service {
    public static final String ACTION_DOWNLOAD="DOWNLOAD";
    public static final String ACTION_STOP="STOP";
    public static final String ACTION_UPDATE="UPDATE";

    /**
     * 存放下载文件的文件夹路径
     */
    public static final String DOWNLOAD_PATH = Environment
            .getExternalStorageDirectory().getAbsolutePath()
            + "/DownloadsTest/";

    private static final int MSG_INIT=0;//代表创建本地文件完成

    private DownloadTask mTask;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
