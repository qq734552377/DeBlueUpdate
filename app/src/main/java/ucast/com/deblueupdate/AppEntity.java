package ucast.com.deblueupdate;

/**
 * Created by pj on 2016/11/24.
 */
public class AppEntity {
    String packageName;
    String apkName;
    String installName;
    int state;



    public AppEntity() {
    }

    public AppEntity(String packageName, String apkName, String installName, int state) {
        this.packageName = packageName;
        this.apkName = apkName;
        this.installName = installName;
        this.state = state;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getApkName() {
        return apkName;
    }

    public void setApkName(String apkName) {
        this.apkName = apkName;
    }

    public String getInstallName() {
        return installName;
    }

    public void setInstallName(String installName) {
        this.installName = installName;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
