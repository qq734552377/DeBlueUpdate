package ucast.com.deblueupdate.socket.Model;

import java.util.ArrayList;
import java.util.List;

import io.netty.channel.Channel;

public class PictureModel {

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public long getOutTime() {
		return outTime;
	}

	public void setOutTime(long outTime) {
		this.outTime = outTime;
	}

	public synchronized void setCurtNum(int curtNum) {
		this.curtNum = curtNum;
	}

	public synchronized int getCurtNum() {
		return curtNum;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public  List<byte[]> BufferPicture=new ArrayList();
	
	private  long outTime;//接收该包时的时间
	
	private String number; //当前包名称
	
	private int total;//总包数

	private int curtNum;//表示当前打印包号

	private Channel channel;//表示通道信息
}
