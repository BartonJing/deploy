package com.barton.utils;


import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.jcraft.jsch.*;
import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.util.Properties;
import java.util.Vector;

public class SFTPUtil {
    private static final Log logger = LogFactory.get();
    private ChannelSftp sftp;
    private String userName; // FTP 登录用户名
    private String password = ""; // FTP 登录密码
    private String keyFilePath;// 私钥文件的路径
    private String host; // FTP 服务器地址IP地址
    private int port; // FTP 端口
    private Session sshSession;


    public SFTPUtil(){
    }



    /**
     * 构造基于密码认证的sftp对象
     *
     * @param userName 用户名
     * @param password 登陆密码
     * @param host     服务器ip
     * @param port     fwq端口
     */
    public SFTPUtil(String userName, String password, String host, int port) {
        super();
        this.userName = userName;
        this.password = password;
        this.host = host;
        this.port = port;
    }

    /**
     * 构造基于秘钥认证的sftp对象
     *
     * @param userName    用户名
     * @param host        服务器ip
     * @param port        fwq端口
     * @param keyFilePath 私钥文件路径
     */
    public SFTPUtil(String userName, String host, int port, String keyFilePath) {
        super();
        this.userName = userName;
        this.host = host;
        this.port = port;
        this.keyFilePath = keyFilePath;
    }

    /**
     * 连接sftp服务器
     *
     * @throws Exception
     */
    public void connect() throws Exception {
        try {
            JSch jsch = new JSch();
            if (StringUtils.isNotEmpty(keyFilePath)) {
                jsch.addIdentity(keyFilePath);// 设置私钥
                logger.info("连接sftp，私钥文件路径：" + keyFilePath);
            }
            logger.info("sftp host: " + host + "; userName:" + userName);
            sshSession = jsch.getSession(userName, host, port);
            logger.debug("Session 已建立.");
            if (password != null) {
                sshSession.setPassword(password);
            }
            Properties sshConfig = new Properties();
            sshConfig.put("StrictHostKeyChecking", "no");
            sshSession.setConfig(sshConfig);
            sshSession.connect();
            logger.debug("Session 已连接.");
            Channel channel = sshSession.openChannel("sftp");
            channel.connect();

            sftp = (ChannelSftp) channel;
            logger.info("连接到SFTP成功。host: " + host);
        } catch (Exception e) {
            logger.error("连接sftp失败！", e);
            throw e;
        }
    }

    /**
     * 关闭连接 server
     */
    public void disconnect() {
        if (sftp != null) {
            if (sftp.isConnected()) {
                sftp.disconnect();
                sshSession.disconnect();
                logger.info("sftp连接关闭成功！" + sftp);
            } else if (sftp.isClosed()) {
                logger.warn("sftp 已经关闭,不需要重复关闭！" + sftp);
            }
        }
    }

    /**
     * 将输入流的数据上传到sftp作为文件
     *
     * @param directory    上传到该目录
     * @param sftpFileName sftp端文件名
     * @param input           输入流
     * @throws Exception
     */
    public void upload(String directory, String sftpFileName, InputStream input) throws Exception {
        try {
            try {// 如果cd报异常，说明目录不存在，就创建目录
                sftp.cd(directory);
            } catch (Exception e) {
                sftp.mkdir(directory);
                sftp.cd(directory);
            }
            logger.info("正在上传中...   文件名：" + sftpFileName);
            sftp.put(input, sftpFileName);
            logger.info("sftp上传成功！文件名：" + sftpFileName);
        } catch (Exception e) {
            logger.error("sftp上传失败！文件名" + sftpFileName, e);
            throw e;
        }
    }

    /**
     * 上传单个文件
     *
     * @param directory  上传到sftp目录
     * @param uploadFile 要上传的文件,包括路径
     * @throws Exception
     */
    public void upload(String directory, String uploadFile) throws Exception {
        File file = new File(uploadFile);
        upload(directory, file.getName(), new FileInputStream(file));
    }

    /**
     * 将byte[]上传到sftp，作为文件。注意:从String生成byte[]是，要指定字符集。
     *
     * @param directory    上传到sftp目录
     * @param sftpFileName 文件在sftp端的命名
     * @param byteArr      要上传的字节数组
     * @throws Exception
     */
    public void upload(String directory, String sftpFileName, byte[] byteArr) throws Exception {
        upload(directory, sftpFileName, new ByteArrayInputStream(byteArr));
    }

    /**
     * 将字符串按照指定的字符编码上传到sftp
     *
     * @param directory    上传到sftp目录
     * @param sftpFileName 文件在sftp端的命名
     * @param dataStr      待上传的数据
     * @param charsetName  sftp上的文件，按该字符编码保存
     * @throws Exception
     */
    public void upload(String directory, String sftpFileName, String dataStr, String charsetName) throws Exception {
        upload(directory, sftpFileName, new ByteArrayInputStream(dataStr.getBytes(charsetName)));
    }

    /**
     * 下载文件
     *
     * @param directory    下载目录
     * @param downloadFile 下载的文件
     * @param saveFile     存在本地的路径
     * @throws Exception
     */
    public void download(String directory, String downloadFile, String saveFile) throws Exception {
        try {
            if (directory != null && !"".equals(directory)) {
                sftp.cd(directory);
            }
            File file = new File(saveFile);
            sftp.get(downloadFile, new FileOutputStream(file));
            logger.info("sftp下载文件成功！文件名" + downloadFile);
        } catch (Exception e) {
            logger.error("sftp下载文件失败！文件名：" + downloadFile, e);
            throw e;
        }
    }

    /**
     * 下载文件
     *
     * @param directory    下载目录
     * @param downloadFile 下载的文件名
     * @return 字节数组
     * @throws Exception
     */

    public byte[] download(String directory, String downloadFile) throws Exception {
        byte[] fileData = null;
        try {
            if (directory != null && !"".equals(directory)) {
                sftp.cd(directory);
            }
            InputStream is = sftp.get(downloadFile);
            fileData = new byte[is.available()];
            is.read(fileData);
            logger.info("sftp下载文件成功！文件名" + downloadFile);
        } catch (SftpException e) {
            logger.error("sftp下载文件失败！文件名：" + downloadFile, e);
            throw e;
        } catch (IOException e) {
            logger.error("sftp下载文件读取失败！文件名：" + downloadFile, e);
            throw e;
        }
        return fileData;
    }

    /**
     * 删除文件
     *
     * @param directory  要删除文件所在目录
     * @param deleteFile 要删除的文件
     * @throws Exception
     */

    public void delete(String directory, String deleteFile) throws Exception {
        try {
            sftp.cd(directory);
            sftp.rm(deleteFile);
        } catch (Exception e) {
            logger.error("sftp删除文件失败" + deleteFile, e);
            throw e;
        }
    }

    /**
     * 列出目录下的文件
     *
     * @param directory 要列出的目录
     * @return
     * @throws SftpException
     */
    public Vector listFiles(String directory) throws SftpException {
        return sftp.ls(directory);
    }
}
