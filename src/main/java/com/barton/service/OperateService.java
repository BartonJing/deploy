package com.barton.service;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.barton.domain.config.Project;
import com.barton.domain.config.Src;
import com.barton.domain.config.Target;
import com.barton.enums.CommonEnum;
import com.barton.utils.FTPUtil;
import com.barton.utils.PropertiesUtil;
import com.barton.utils.RemoteExecuteCommand;
import com.barton.utils.SFTPUtil;

import java.io.File;
import java.util.Date;
import java.util.List;

public abstract class OperateService {
    private static final Log logger = LogFactory.get();
    /**
     * 打包项目
     */
    public abstract void packageProject();


    /**
     * 上传,并启动项目
     * @param project
     */
    public void execute(Project project){
        //上传项目目标信息
        Target target = project.getTarget();
        //项目源信息
        Src src = project.getSrc();
        String projectName = src.getName();
        String srcPath = src.getPath();
        String targetDir = target.getDir();
        String targetPath = target.getPath();
        Boolean isBackUp = target.getBackup();
        String backUpDir = target.getBackupDir();

        String userName = target.getUsername();
        String password = target.getPassword();
        String host = target.getIp();
        Integer port = target.getPort();


        try {
            SFTPUtil sftpUtil = new SFTPUtil(userName, password, host, port);
            sftpUtil.connect();
            //上传脚本到服务器
            String backupPath = OperateService.class.getClass().getResource("/"+CommonEnum.BACKUP.getShellName()).getPath();
            String startProjectPath = OperateService.class.getClass().getResource("/"+CommonEnum.STARTPROJECT.getShellName()).getPath();

            //拷贝得shell脚本目录
            String targetScriptDir = targetDir +"script/";
            //启动脚本
            RemoteExecuteCommand rec=new RemoteExecuteCommand(host, userName, password);
            //上传脚本文件
            sftpUtil.upload(targetScriptDir,backupPath);
            sftpUtil.upload(targetScriptDir,startProjectPath);
            //为脚本文件赋权限
            rec.execute("chmod 777 "+targetScriptDir+CommonEnum.BACKUP.getShellName());
            rec.execute("chmod 777 "+targetScriptDir+CommonEnum.STARTPROJECT.getShellName());
            //替换脚本中的\r(win下的换行为\n\r,linux下的换行为\r)
            rec.execute("sed -i 's/\\r$//' "+targetScriptDir+CommonEnum.BACKUP.getShellName());
            rec.execute("sed -i 's/\\r$//' "+targetScriptDir+CommonEnum.STARTPROJECT.getShellName());
            logger.info(targetScriptDir);
            //备份项目
            if(isBackUp){
                //执行命令
                String cmd = targetScriptDir+ CommonEnum.BACKUP.getShellName() + " " + targetPath + " " +backUpDir+ DateUtil.format(new Date(),"yyyy-MM-dd")+projectName;
                logger.info(cmd);
                String pid = rec.execute(cmd);
                logger.info(pid);
            }

            //上传文件
            sftpUtil.upload(targetDir,srcPath);
            sftpUtil.disconnect();
            //启动项目
            logger.info("开始启动项目->    {}",projectName);
            String shell = targetScriptDir+CommonEnum.STARTPROJECT.getShellName()+" "+targetDir+projectName;
            logger.info(shell);
            String str = rec.execute(shell);
            logger.info(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
