package ucast.com.deblueupdate.androidTools;

import android.content.Context;
import android.content.SharedPreferences;

import ucast.com.deblueupdate.app.ExceptionApplication;

/**
 * Created by pj on 2016/11/22.
 */
public class ApkVersionSave {

    private static ApkVersionSave apkVersionSave;

    private ApkVersionSave() {
    }

    public static ApkVersionSave getInstance(){
        if (apkVersionSave == null){
            synchronized (ApkVersionSave.class){
                if (apkVersionSave == null){
                    apkVersionSave=new ApkVersionSave();
                }
            }
        }
        return apkVersionSave;
    }

    public void save (String key ,String value){
        SharedPreferences sp= ExceptionApplication
                .getInstance().getSharedPreferences("apkVersion", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString(key,value);
        editor.commit();
    }
    public void save (String key ,long value){
        SharedPreferences sp= ExceptionApplication
                .getInstance().getSharedPreferences("apkVersion", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public String get(String key){
        SharedPreferences sp=ExceptionApplication
                .getInstance().getSharedPreferences("apkVersion", Context.MODE_PRIVATE);

        return sp.getString(key,"1.0");
    }
    public long getLong(String key){
        SharedPreferences sp=ExceptionApplication
                .getInstance().getSharedPreferences("apkVersion", Context.MODE_PRIVATE);

        return sp.getLong(key, 1L);
    }
}
