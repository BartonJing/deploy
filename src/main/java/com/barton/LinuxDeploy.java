package com.barton;


import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.barton.domain.config.Project;
import com.barton.service.LinuxOperateService;
import com.barton.service.OperateService;
import com.barton.service.WinOperateService;
import com.barton.utils.PropertiesUtil;

import java.util.List;


/**
 * Linux下打包发布项目
 */
public class LinuxDeploy {
    private static final Log logger = LogFactory.get();
    public static void main(String [] args){
        String configFile = null;
        if(args != null && args.length > 0){
            configFile = args[0];
        }
        logger.info("配置信息："+configFile);
        OperateService operateService = new LinuxOperateService();
        //获取配置信息
        PropertiesUtil propertiesUtil = PropertiesUtil.getInstance(configFile);
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
                //上传,并启动项目
                operateService.execute(project);
            }
        }
    }
}
