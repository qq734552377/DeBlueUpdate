package ucast.com.deblueupdate.socket;


import java.util.concurrent.TimeUnit;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.timeout.IdleStateHandler;
import ucast.com.deblueupdate.socket.MessageCallback.CallbackHandle;
import ucast.com.deblueupdate.socket.MessageCallback.IMsgCallback;
import ucast.com.deblueupdate.socket.MessageProtocol.StationPackage;

/**
 * Created by Administrator on 2016/2/3.
 */
public class DataServerInitializer extends ChannelInitializer {

    public IMsgCallback callback;

    public DataServerInitializer() {
        callback = new CallbackHandle();
    }

    public void initChannel(Channel channel) {
        StationPackage stationPackage = new StationPackage(channel);
        stationPackage.callback = callback;
        TcpServerHandle handle = new TcpServerHandle(stationPackage);
        channel.pipeline().addLast("idleStateHandler", new IdleStateHandler(0, 0, 300000, TimeUnit.MILLISECONDS));
        channel.pipeline().addLast("handler", handle);

    }
}
