package ucast.com.deblueupdate.androidTools.downloaddemo.services;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import ucast.com.deblueupdate.androidTools.downloaddemo.db.ThreadDAO;
import ucast.com.deblueupdate.androidTools.downloaddemo.db.ThreadDAOImpl;
import ucast.com.deblueupdate.androidTools.downloaddemo.entities.FileInfo;
import ucast.com.deblueupdate.androidTools.downloaddemo.entities.ThreadInfo;

/**
 * Created by pj on 2016/11/22.
 */
public class DownloadTask {

    private Context mContext;
    private FileInfo mFileInfo;

    private ThreadDAO mDAO;

    private int mFinished;//用于更新UI的下载进度

    public boolean isPause;//判断是否正在下载
    public boolean isFinished;//判断是否下载完成

    public DownloadTask(Context context, FileInfo fileInfo) {
        super();
        this.mContext = context;
        this.mFileInfo = fileInfo;
        mDAO=new ThreadDAOImpl(context);
        isPause=false;
        isFinished=false;
    }

    public void download() {
        Log.d("测试", "download");

        //读取上次的线程信息
        List<ThreadInfo> list=mDAO.getThreads(mFileInfo.getUrl());
        ThreadInfo threadInfo=null;
        if(list.size()==0||list==null) {//有可能是第一次下载，数据库中还没有信息
            threadInfo=new ThreadInfo(0,mFileInfo.getUrl(),0,mFileInfo.getLength(),0);
        }
        else {
            //因为这里是单线程下载，所以这里直接get(0)就行了，下次会涉及多线程
            threadInfo=list.get(0);
        }
        new DownloadThread(threadInfo).start();
    }

    /**
     * 下载线程
     *
     */
    class DownloadThread extends Thread {
        private ThreadInfo mThreadInfo;
        public DownloadThread(ThreadInfo ThreadInfo) {
            mThreadInfo=ThreadInfo;
        }

        public void run() {
            Log.d("测试","DownloadThread");

            //向数据库中插入线程信息
            if(!mDAO.isExists(mThreadInfo.getUrl(), mThreadInfo.getId())) {
                mDAO.insertThreadInfo(mThreadInfo);
            }

            HttpURLConnection connection=null;
            RandomAccessFile raf=null;
            InputStream input=null;
            try {
                URL url=new URL(mThreadInfo.getUrl());
                connection=(HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(3000);
                connection.setRequestMethod("GET");

                //设置下载位置
                int start=mThreadInfo.getFinished();//上一次保存的下载进度即为这次要开始下载的地方

                connection.setRequestProperty("Range", "bytes="+start+"-"+mThreadInfo.getEnd());
                //设置请求属性，将field参数设置为Range（范围），newValues为指定的字节数区间

                //设置文件写入位置
                File file=new File(DownloadService.DOWNLOAD_PATH,mFileInfo.getFileName());
                raf=new RandomAccessFile(file, "rwd");
                raf.seek(start);
                //在读写的时候跳过设置的字节数，从下一个字节数开始读写
                //如seek(100)则跳过100个字节从第101个字节开始读写

                Intent intent=new Intent(DownloadService.ACTION_UPDATE);

                mFinished=start;

                //开始下载
                if(connection.getResponseCode()==200) {
                    //因为前面的RequestProperty设置的Range，服务器会认为进行部分的下载，所以这里判断是否成功连接要用SC_PARTIAL_CONTENT
                    //读取数据
                    input=connection.getInputStream();
                    byte[] buffer=new byte[1024*4];
                    int len=-1;//标记每次读取的长度

                    long time=System.currentTimeMillis();

                    while((len=input.read(buffer))!=-1) {
                        //写入文件
                        raf.write(buffer,0,len);

                        //把下载进度发送给Activity
                        mFinished += len;
                        if(System.currentTimeMillis()-time>500) {//因为该循环运行较快，所以这里减缓一下UI更新的频率
                            time=System.currentTimeMillis();
                            intent.putExtra("finished", mFinished * 100 / mFileInfo.getLength());
                            mContext.sendBroadcast(intent);
                            Log.d("测试", ""+mFinished * 100 / mFileInfo.getLength());

                            mDAO.updateThreadInfo(mThreadInfo.getUrl(), mThreadInfo.getId(), mFinished);
                            //如果隔断时间就更新一下数据库的内容
                            //可以防止没有按下暂停就关闭程序重进后要重新开始下载的问题
                            //而又不至于更新数据库太频繁，影响效率
                        }

                        //在下载暂停时，保存下载进度至数据库
                        if(isPause) {
                            mDAO.updateThreadInfo(mThreadInfo.getUrl(), mThreadInfo.getId(), mFinished);
                            intent.putExtra("finished", mFinished * 100 / mFileInfo.getLength());
                            mContext.sendBroadcast(intent);
                            raf.close();
                            input.close();
                            connection.disconnect();
                            return;
                        }
                    }
                    intent.putExtra("finished", mFinished * 100 / mFileInfo.getLength());
                    mContext.sendBroadcast(intent);
                    //因为有可能在刚好下载完成的时候没有进入到if(isPause)中，所以进度条会停在上次更新的时候，显示的时候还有一小段没有下载，但是实际已经下载完成了

                    //删除线程信息
                    mDAO.deleteThreadInfo(mThreadInfo.getUrl(), mThreadInfo.getId());
                    isFinished=true;
                }
            } catch (Exception e) {
            } finally {
                try {
                    raf.close();
                    input.close();
                    connection.disconnect();
                } catch (Exception e) {
                }
            }
        }
    }
}
