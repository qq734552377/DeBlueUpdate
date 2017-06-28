package ucast.com.deblueupdate.androidTools;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by pj on 2016/11/22.
 */
public class AppInfo {
    public static String getVersionName(Context context,String packageName) {
        if (getPackageInfo(context,packageName)==null){
            return "0.0";
        }
        return getPackageInfo(context,packageName).versionName;
    }

    //版本号
    public static int getVersionCode(Context context,String packageName) {
        return getPackageInfo(context,packageName).versionCode;
    }

    private static PackageInfo getPackageInfo(Context context,String packageName) {
        PackageInfo pi = null;

        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(packageName,
                    PackageManager.GET_CONFIGURATIONS);

            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pi;
    }
}
