package ucast.com.deblueupdate.filemanage;

import android.graphics.Bitmap;
import android.util.Log;


import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import io.netty.channel.Channel;
import ucast.com.deblueupdate.socket.ArrayQueue;
import ucast.com.deblueupdate.socket.Common;

/**
 * Created by Administrator on 2016/2/16.
 */
public class ReadPicture {

    private boolean _mDispose;

    private ArrayQueue<String> fileName_queue = new ArrayQueue<String>(0x400);
    private ArrayQueue<Channel> channleQueue = new ArrayQueue<Channel>(0x400);

    // Methods
    public ReadPicture() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                WRun();
            }
        }).start();
    }

    /// <summary>
    /// 添加列队
    /// </summary>
    /// <param name="iObj"></param>
    public void Add(String fileName) {
        synchronized (fileName_queue) {
            fileName_queue.enqueue(fileName);
        }
    }

    /// <summary>
    /// 释放线程
    /// </summary>
    public void Dispose() {
        if (!_mDispose) {
            _mDispose = true;
        }
    }

    private String GetItem() {
        synchronized (fileName_queue) {
            if (fileName_queue.size() > 0) {
                return fileName_queue.dequeue();
            }
            return null;
        }
    }

    private Channel GetChannelItem() {
        synchronized (fileName_queue) {
            if (channleQueue.size() > 0) {
                return channleQueue.dequeue();
            }
            return null;
        }
    }

    private static final String TAG = "ReadPicture";

    private void OnRun() {

        if (ListPictureQueue.GetCount() > 1) {
            try {
                Thread.sleep(150);
                return;
            } catch (Exception e) {

            }

        }
        String fileName = GetItem();
        try {
            if (fileName != null) {
                Log.e(TAG, "OnRun: 开始处理" + fileName);
                //TODO 读取文件数据,在将文件放入发送队列
                FileModel fileModel = readFileByRandomAccess(fileName);
                if (fileModel == null) {
                    return;
                }
                fileModel.setOutTime(System.currentTimeMillis());
                fileModel.setFileName(fileName.substring(fileName.lastIndexOf("/") + 1));
                //接收完数据数据后 开始加入到打印队列里并发送
                if (ListPictureQueue.GetCount() <= 0) {
                    ListPictureQueue.Add(fileModel);
                    ListPictureQueue.SendFirst();
                    return;
                }
                ListPictureQueue.Add(fileModel);
                Thread.sleep(200);
            } else {
                Thread.sleep(50);
            }
        } catch (Exception e) {

        }
    }

    private void WRun() {
        while (!_mDispose) {
            OnRun();
        }
    }


    /**
     * 随机读取文件内容
     *
     * @param fileName 文件名
     */
    public FileModel readFileByRandomAccess(String fileName) {

        File file = new File(fileName);
        if (!file.exists()) {
            return null;
        }


        RandomAccessFile randomFile = null;
        FileModel fileModel = new FileModel();
        int index = 1;
        try {
            System.out.println("随机读取一段文件内容：");
            // 打开一个随机访问文件流，按只读方式
            randomFile = new RandomAccessFile(fileName, "r");
            // 将读文件的开始位置移到beginIndex位置。
            randomFile.seek(0);
            byte[] bytes = new byte[1024 * 1024];
            int byteread = 0;
            // 一次读10个字节，如果文件内容不足10个字节，则读剩下的字节。
            // 将一次读取的字节数赋给byteread
            while ((byteread = randomFile.read(bytes)) != -1) {
                //TODO 读取到的内容处理
                byte[] newbytes = new byte[byteread];
                System.arraycopy(bytes, 0, newbytes, 0, byteread);

                String data = Common.encode(newbytes);
                fileModel.fileDatas.add(data);
                fileModel.setTotal(index++);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (randomFile != null) {
                try {
                    randomFile.close();
                } catch (IOException e1) {
                }
            }
            return fileModel;
        }
    }


}
