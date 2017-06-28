package ucast.com.deblueupdate.socket.Message;

/**
 * Created by pj on 2016/11/23.
 */
public class ApkVersion extends MessageBase {

    public String version;
    public String url;
    public long size;
    @Override
    public void Load(String[] Str) {
        super.Load(Str);
        this.Cmd=Str[0];
        this.version=Str[1];
        this.size=Long.parseLong(Str[2]);
        this.url=Str[3];
    }
}
