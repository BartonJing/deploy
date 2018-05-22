package com.barton.enums;

public enum CommonEnum {



    BACKUP("backup.sh","备份Shell脚本"),
    STARTPROJECT("startProject.sh","启动项目Shell脚本"),
    WINPACKAGE("package.bat","打包项目脚本Bat脚本"),
    LINUPACKAGE("package.sh","打包项目Shell脚本");

    private String shellName;
    private String shellDesc;

    CommonEnum(String shellName, String shellDesc) {
        this.shellName = shellName;
        this.shellDesc = shellDesc;
    }

    public String getShellName() {
        return shellName;
    }

    public void setShellName(String shellName) {
        this.shellName = shellName;
    }

    public String getShellDesc() {
        return shellDesc;
    }

    public void setShellDesc(String shellDesc) {
        this.shellDesc = shellDesc;
    }
}
