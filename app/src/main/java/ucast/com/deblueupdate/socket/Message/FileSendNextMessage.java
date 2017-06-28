package ucast.com.deblueupdate.socket.Message;

/**
 * Created by Allen on 2017/1/5.
 */

public class FileSendNextMessage extends MessageBase {
    public String fileName;
    public int number_bao;

    @Override
    public void Load(String[] Str) {
        this.Cmd = Str[0];
        this.fileName = Str[1];
        this.number_bao = Integer.parseInt(Str[2]);
    }
}
