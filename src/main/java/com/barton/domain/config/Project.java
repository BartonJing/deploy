package com.barton.domain.config;

import javax.xml.bind.annotation.*;

/**
 * 项目信息
 */
@XmlRootElement(name = "project")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "project", propOrder = { "src", "target","code","isPackage"})
public class Project {
    /**
     * 源项目配置信息
     */
    @XmlElement(name = "src")
    private Src src;
    /**
     * 目标项目配置信息
     */
    @XmlElement(name = "target")
    private Target target;

    /**
     * 项目编号
     */
    @XmlElement(name = "code")
    private Integer code;

    /**
     * 是否打包
     */
    @XmlElement(name = "isPackage")
    private Boolean isPackage;

    public Src getSrc() {
        return src;
    }

    public void setSrc(Src src) {
        this.src = src;
    }

    public Target getTarget() {
        return target;
    }

    public void setTarget(Target target) {
        this.target = target;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Boolean getPackage() {
        return isPackage;
    }

    public void setPackage(Boolean aPackage) {
        isPackage = aPackage;
    }
}
