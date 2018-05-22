package com.barton;


import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.barton.service.LinuxOperateService;
import com.barton.service.OperateService;


/**
 * Linux下打包发布项目
 */
public class LinuxDeploy {
    private static final Log logger = LogFactory.get();
    public static void main(String [] args){
        OperateService operateService = new LinuxOperateService();
        //打包项目
        operateService.packageProject();
        //上传项目到服务器
        //operateService.uploadProject();
        //启动项目
        //operateService.startProject();
    }
}
