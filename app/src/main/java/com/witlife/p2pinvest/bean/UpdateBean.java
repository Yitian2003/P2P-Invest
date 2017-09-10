package com.witlife.p2pinvest.bean;

/**
 * Created by bruce on 7/09/2017.
 */

public class UpdateBean {
    /**
     * version : 1.2
     * apkUrl : http://192.168.1.104:8080/P2PInvest/app_new.apk
     * desc : fix bugs, optimize request to server!
     */

    private String version;
    private String apkUrl;
    private String desc;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getApkUrl() {
        return apkUrl;
    }

    public void setApkUrl(String apkUrl) {
        this.apkUrl = apkUrl;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
