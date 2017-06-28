package ucast.com.deblueupdate.socket.Message;

/**
 * Created by Administrator on 2016/3/31.
 */
public class GeMAcMessage extends MessageBase {
    public void Load(String[] str) {
        super.Load(str);
        Cmd = str[0];
    }
}
