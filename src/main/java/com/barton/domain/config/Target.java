package com.barton.domain.config;

import javax.xml.bind.annotation.*;

/**
 * 目标项目信息
 */
@XmlRootElement(name = "target")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "target", propOrder = { "ip", "username",
        "password", "port","keyFilePath","dir","path","backup","backupDir"})
public class Target {
    /**
     * ip地址
     */
    @XmlElement(name = "ip")
    private String ip;
    /**
     * 用户
     */
    @XmlElement(name = "username")
    private String username;
    /**
     * 密码
     */
    @XmlElement(name = "password")
    private String password;
    /**
     * 端口
     */
    @XmlElement(name = "port")
    private Integer port;
    /**
     * 私钥地址
     */
    @XmlElement(name = "keyFilePath")
    private String keyFilePath;
    /**
     * 存放项目目录
     */
    @XmlElement(name = "dir")
    private String dir;
    /**
     * 项目路径
     */
    @XmlElement(name = "path")
    private String path;
    /**
     * 是否备份
     */
    @XmlElement(name = "backup")
    private Boolean backup;
    /**
     * 备份目录
     */
    @XmlElement(name = "backupDir")
    private String backupDir;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getKeyFilePath() {
        return keyFilePath;
    }

    public void setKeyFilePath(String keyFilePath) {
        this.keyFilePath = keyFilePath;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Boolean getBackup() {
        return backup;
    }

    public void setBackup(Boolean backup) {
        this.backup = backup;
    }

    public String getBackupDir() {
        return backupDir;
    }

    public void setBackupDir(String backupDir) {
        this.backupDir = backupDir;
    }
}
