package ucast.com.deblueupdate.socket.Message;

/**
 * Created by pj on 2016/11/23.
 */
public class GetLogsMessage extends MessageBase {


    @Override
    public void Load(String[] Str) {
        super.Load(Str);
        this.Cmd=Str[0];
    }
}
