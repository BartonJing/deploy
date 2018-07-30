package com.barton;


import cn.hutool.core.date.DateUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.barton.domain.config.Project;
import com.barton.domain.config.Src;
import com.barton.domain.config.Target;
import com.barton.enums.CommonEnum;
import com.barton.service.OperateService;
import com.barton.service.WinOperateService;
import com.barton.utils.PropertiesUtil;
import com.barton.utils.RemoteExecuteCommand;
import com.barton.utils.SFTPUtil;
import com.jcraft.jsch.ChannelSftp;

import java.util.Date;
import java.util.List;


/**
 * window下打包发布项目
 */
public class WinDeploy {
    private static final Log logger = LogFactory.get();
    public static void main(String [] args){
        OperateService operateService = new WinOperateService();
        //获取配置信息
        PropertiesUtil propertiesUtil = PropertiesUtil.getInstance();
        //配置信息 Project
        List<Project> projects = propertiesUtil.getProperties().getProject();
        //遍历配置的项目信息，依次处理
        if(projects != null && projects.size()>0){
            for(Project project : projects){
                Boolean isPackage = project.getPackage();
                //是否要编译打包
                if(isPackage){
                    //运行编译打包脚本
                    operateService.packageProject(project);
                }
                System.out.println("打包完成");
                //上传,并启动项目
                operateService.execute(project);
            }
        }

    }
}
