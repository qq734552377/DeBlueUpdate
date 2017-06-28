package ucast.com.deblueupdate.filemanage;


import android.util.Log;
import android.util.Size;


import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import ucast.com.deblueupdate.mytime.MyTimeTask;
import ucast.com.deblueupdate.mytime.MyTimer;
import ucast.com.deblueupdate.socket.Common;


public class ListPictureQueue {

    private static List<FileModel> list = new ArrayList();

    private static MyTimer timer;

    private static boolean onOff;


    private static boolean isSendSucess = false;

    public static void StartTimer() {
        timer = new MyTimer(new MyTimeTask(new Runnable() {
            @Override
            public void run() {
                if (!onOff)
                    onOff = true;
                try {
                    synchronized (list) {
                        if (list.size() <= 0)
                            return;
                        FileModel info = list.get(0);
                        long time = (long) (System.currentTimeMillis() - info.getOutTime()) / 1000;
                        if (time < 15) {
                            return;
                        }
//                        if (isSendSucess) {
//                            Remove();
//                            isSendSucess = false;
//                        }
                        Log.e(TAG, "run: 尝试发送");
                        //从队列中取出图片 发送下一张图片
                        sendNext();
                    }
                    onOff = false;
                } catch (Exception e) {
                    sendNext();
                    onOff = false;
                }
            }
        }), 2000L, 8000L);
        timer.initMyTimer().startMyTimer();

    }

    public static void EndTime() {
        if (timer == null)
            return;
        timer.stopMyTimer();
    }

    public static void Add(FileModel model) {
        synchronized (list) {
            list.add(model);
        }
    }

    public static int GetCount() {
        int s = list.size();
        return s;
    }

    private static void Clean() {
        synchronized (list) {
            if (list.size() <= 0)
                return;
            list.remove(0);
        }
    }

    private static void Remove() {
        if (list.size() <= 0)
            return;
        list.get(0).fileDatas.clear();
        list.remove(0);
    }

    public static void SendFirst() {
        synchronized (list) {
            if (list.size() <= 0)
                return;

            Log.e(TAG, "SendFirst: 发送第一包");
            //获取队列中第一张图片
            FileModel info = list.get(0);
            if (info == null || info.fileDatas.size() <= 0)
                return;

            //发送第一张图片的第一包内容

            //TODO 可能在此处一次性发送全部数据  有待改进
            StringBuffer buff = new StringBuffer();
            buff.append("@");
            buff.append("pw001,");
            buff.append(info.getFileName());
            buff.append(",");
            buff.append(info.getTotal());
            buff.append(",");
            buff.append(info.getCurIndex());
            buff.append(",");
            buff.append(info.getData());
            buff.append("$");
            byte[] str = buff.toString().getBytes();

            isSendSucess = Common.ServicesAllSend(str);
            if (isSendSucess)
                info.setOutTime(System.currentTimeMillis());
            buff.delete(0, buff.length() - 1);

        }
    }

    public static void sendNext() {
        if (list.size() <= 0) {
            Log.e(TAG, "sendNext: 没有内容了");
            return;
        }
        Log.e(TAG, "sendNext: 发送下一个");

        //获取队列中第一张图片
        FileModel info = list.get(0);
        if (info == null || info.fileDatas.size() <= 0)
            return;

        //发送第一张图片的第一包内容

        //TODO 可能在此处一次性发送全部数据  有待改进
        StringBuffer buff = new StringBuffer();
        buff.append("@");
        buff.append("pw001,");
        buff.append(info.getFileName());
        buff.append(",");
        buff.append(info.getTotal());
        buff.append(",");
        buff.append(info.getCurIndex());
        buff.append(",");
        buff.append(info.getData());
        buff.append("$");
        byte[] str = buff.toString().getBytes();

        isSendSucess = Common.ServicesAllSend(str);
        if (isSendSucess)
            info.setOutTime(System.currentTimeMillis());

        buff.delete(0, buff.length() - 1);
    }


    private static final String TAG = "ListPictureQueue";

    public static void Send(int index) {
        synchronized (list) {
            ResultSend(index);
        }
    }

    private static void ResultSend(int index) {
        if (list.size() <= 0 || index < 0)
            return;
        try {
            FileModel info = list.get(0);
            if (info.fileDatas.size() <= 0 && isSendSucess) {
                Remove();
                return;
            }

            if (info.fileDatas.size() > index + 1) {
                info.setCurIndex(index + 1);
                isSendSucess = false;
                sendNext();
            }


            if (info.fileDatas.size() == index + 1) {
                Log.e(TAG, "ResultSend: 移调没有数据的model");
                String fileName = list.get(0).getFileName();
                String oneDown = "@pw002," + fileName + "$";
                Common.ServicesAllSend(oneDown.getBytes());
                Remove();
                if (list.size() <= 0) {
                    return;
                }
                sendNext();
                return;
            }
        } catch (Exception e) {
            Remove();
            sendNext();
        }
    }


}
