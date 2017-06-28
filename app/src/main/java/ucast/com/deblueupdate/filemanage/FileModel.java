package ucast.com.deblueupdate.filemanage;

import java.util.ArrayList;
import java.util.List;

import io.netty.channel.Channel;

/**
 * Created by Allen on 2017/1/5.
 */

public class FileModel {

    private int curIndex = 0;
    private long outTime;

    private int total;

    private Channel channel;

    private String fileName;

    public List<String> fileDatas = new ArrayList();

    public int getTotal() {

        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getOutTime() {

        return outTime;
    }

    public void setOutTime(long outTime) {
        this.outTime = outTime;
    }

    public int getCurIndex() {
        return curIndex;
    }

    public void setCurIndex(int curIndex) {
        this.curIndex = curIndex;
    }

    public String getData() {
        if (curIndex > 0) {
            fileDatas.set(curIndex - 1, "");
        }
        return fileDatas.get(curIndex);
    }

}
