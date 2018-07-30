package com.barton.service;

import com.barton.domain.config.Project;
import com.barton.domain.config.Src;
import com.barton.domain.config.Target;
import com.barton.enums.CommonEnum;
import com.barton.utils.ProcessUtil;

public class WinOperateService extends OperateService {
    /**
     * 执行打包项目脚本
     */
    public void packageProject(Project project) {
        //项目源信息
        Src src = project.getSrc();
        String srcDir = src.getDir();

        StringBuilder cmd = new StringBuilder();
        //打包本地项目
        cmd.append("cmd /c start "+ CommonEnum.WINPACKAGE.getShellName() + " " + srcDir);
        ProcessUtil.convert(cmd);
    }

}
