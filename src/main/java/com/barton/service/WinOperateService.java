package com.barton.service;

import com.barton.utils.ProcessUtil;

public class WinOperateService extends OperateService {
    public void packageProject() {
        StringBuilder cmd = new StringBuilder();
        //打包本地项目
        cmd.append("cmd /c start package.bat");
        ProcessUtil.convert(cmd);
    }

}
