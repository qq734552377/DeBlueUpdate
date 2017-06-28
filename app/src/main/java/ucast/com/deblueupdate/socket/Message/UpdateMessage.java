package ucast.com.deblueupdate.socket.Message;

/**
 * Created by pj on 2016/11/23.
 */
public class UpdateMessage extends MessageBase {

    public boolean isUpdate;

    @Override
    public void Load(String[] Str) {
        super.Load(Str);
        this.Cmd=Str[0];
        if (Str[1].equals("1")){
            this.isUpdate=true;
        }else{
            this.isUpdate=false;
        }
    }
}
