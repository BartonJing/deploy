package com.barton.service;

import com.barton.domain.config.Project;
import com.barton.domain.config.Src;
import com.barton.enums.CommonEnum;
import com.barton.utils.ProcessUtil;

public class LinuxOperateService extends OperateService {
    /**
     * 执行打包项目脚本
     */
    public void packageProject(Project project) {
        StringBuilder cmd = new StringBuilder();
        //项目源信息
        Src src = project.getSrc();
        String srcDir = src.getDir();
        //打包本地项目
        cmd.append("./"+ CommonEnum.LINUPACKAGE.getShellName() + " " + srcDir);
        ProcessUtil.convert(cmd);
    }

}
