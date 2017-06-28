package ucast.com.deblueupdate.socket.Message;

/**
 * Created by Allen on 2017/1/5.
 */

public class FileSendMessage extends MessageBase {
    public String fileName;


    @Override
    public void Load(String[] Str) {
        this.Cmd=Str[0];
        this.fileName=Str[1];
    }
}
