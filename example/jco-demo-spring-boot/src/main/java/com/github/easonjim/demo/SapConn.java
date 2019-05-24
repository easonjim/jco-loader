package com.github.easonjim.demo;

/**
 * @author jim
 * @date 2019/05/21
 */
public class SapConn {

    /**
     * SAP服务器
     **/
    private String jcoAshost;
    /**
     * SAP系统编号
     **/
    private String jcoSysnr;
    /**
     * SAP集团
     **/
    private String jcoClient;
    /**
     * SAP用户名
     **/
    private String jcoUser;
    /**
     * SAP密码
     **/
    private String jcoPasswd;
    /**
     * SAP登录语言
     **/
    private String jcoLang;
    /**
     * 最大连接数
     **/
    private String jcoPoolCapacity;
    /**
     * 最大连接线程
     **/
    private String jcoPeakLimit;
    /**
     * SAP ROUTER
     **/
    private String jcoSaprouter;

    public SapConn(String jcoAshost, String jcoSysnr, String jcoClient, String jcoUser, String jcoPasswd, String jcoLang, String jcoPoolCapacity, String jcoPeakLimit, String jcoSaprouter) {
        this.jcoAshost = jcoAshost;
        this.jcoSysnr = jcoSysnr;
        this.jcoClient = jcoClient;
        this.jcoUser = jcoUser;
        this.jcoPasswd = jcoPasswd;
        this.jcoLang = jcoLang;
        this.jcoPoolCapacity = jcoPoolCapacity;
        this.jcoPeakLimit = jcoPeakLimit;
        this.jcoSaprouter = jcoSaprouter;
    }

    public SapConn() {
    }

    public String getJcoAshost() {
        return jcoAshost;
    }

    public void setJcoAshost(String jcoAshost) {
        this.jcoAshost = jcoAshost;
    }

    public String getJcoSysnr() {
        return jcoSysnr;
    }

    public void setJcoSysnr(String jcoSysnr) {
        this.jcoSysnr = jcoSysnr;
    }

    public String getJcoClient() {
        return jcoClient;
    }

    public void setJcoClient(String jcoClient) {
        this.jcoClient = jcoClient;
    }

    public String getJcoUser() {
        return jcoUser;
    }

    public void setJcoUser(String jcoUser) {
        this.jcoUser = jcoUser;
    }

    public String getJcoPasswd() {
        return jcoPasswd;
    }

    public void setJcoPasswd(String jcoPasswd) {
        this.jcoPasswd = jcoPasswd;
    }

    public String getJcoLang() {
        return jcoLang;
    }

    public void setJcoLang(String jcoLang) {
        this.jcoLang = jcoLang;
    }

    public String getJcoPoolCapacity() {
        return jcoPoolCapacity;
    }

    public void setJcoPoolCapacity(String jcoPoolCapacity) {
        this.jcoPoolCapacity = jcoPoolCapacity;
    }

    public String getJcoPeakLimit() {
        return jcoPeakLimit;
    }

    public void setJcoPeakLimit(String jcoPeakLimit) {
        this.jcoPeakLimit = jcoPeakLimit;
    }

    public String getJcoSaprouter() {
        return jcoSaprouter;
    }

    public void setJcoSaprouter(String jcoSaprouter) {
        this.jcoSaprouter = jcoSaprouter;
    }

    @Override
    public String toString() {
        return "SapConn{" +
                "jcoAshost='" + jcoAshost + '\'' +
                ", jcoSysnr='" + jcoSysnr + '\'' +
                ", jcoClient='" + jcoClient + '\'' +
                ", jcoUser='" + jcoUser + '\'' +
                ", jcoPasswd='" + jcoPasswd + '\'' +
                ", jcoLang='" + jcoLang + '\'' +
                ", jcoPoolCapacity='" + jcoPoolCapacity + '\'' +
                ", jcoPeakLimit='" + jcoPeakLimit + '\'' +
                ", jcoSaprouter='" + jcoSaprouter + '\'' +
                '}';
    }
}