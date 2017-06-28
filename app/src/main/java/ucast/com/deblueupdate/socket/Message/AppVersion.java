package ucast.com.deblueupdate.socket.Message;

/**
 * Created by pj on 2016/11/23.
 */
public class AppVersion extends MessageBase {

    public boolean isGetappVersion;

    @Override
    public void Load(String[] Str) {
        super.Load(Str);
        this.Cmd=Str[0];
        if (Str[1].equals("1")){
            this.isGetappVersion=true;
        }else{
            this.isGetappVersion=false;
        }
    }
}
