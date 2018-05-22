package com.barton.domain.config;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * 配置信息
 */
@XmlRootElement(name = "properties")
@XmlAccessorType(XmlAccessType.FIELD)
public class Properties {
    /**
     * 项目配置信息
     */
    @XmlElement(name = "project")
    private List<Project> project;


    public List<Project> getProject() {
        return project;
    }

    public void setProject(List<Project> project) {
        this.project = project;
    }
}
