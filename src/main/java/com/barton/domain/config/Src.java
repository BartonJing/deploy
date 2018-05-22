package com.barton.domain.config;

import javax.xml.bind.annotation.*;

/**
 * 源项目信息
 */
@XmlRootElement(name = "src")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "src", propOrder = { "name",
        "dir", "path"})
public class Src {
    /**
     * 项目名称
     */
    @XmlElement(name = "name")
    private String name;
    /**
     * 项目目录
     */
    @XmlElement(name = "dir")
    private String dir;
    /**
     * 打包后的jar包存放位置
     */
    @XmlElement(name = "path")
    private String path;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
