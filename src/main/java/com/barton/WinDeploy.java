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

        PropertiesUtil propertiesUtil = PropertiesUtil.getInstance();
        List<Project> projects = propertiesUtil.getProperties().getProject();
        if(projects != null && projects.size()>0){
            for(Project project : projects){
                Boolean isPackage = project.getPackage();
                if(isPackage){
                    //mvn 打包java项目
                    operateService.packageProject();
                }
                //上传,并启动项目
                operateService.execute(project);
            }
        }

    }
}
