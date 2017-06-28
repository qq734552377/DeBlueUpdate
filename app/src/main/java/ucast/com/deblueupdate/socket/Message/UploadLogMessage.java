package ucast.com.deblueupdate.socket.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pj on 2016/11/23.
 */
public class UploadLogMessage extends MessageBase {

    public List<String> logs=new ArrayList<>();
    public String url;

    @Override
    public void Load(String[] Str) {
        super.Load(Str);
        this.Cmd=Str[0];
        this.url=Str[1];
        for (int i = 2; i < Str.length; i++) {
            this.logs.add(Str[i]);
        }
    }
}
