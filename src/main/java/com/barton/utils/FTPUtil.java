package com.barton.utils;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPReply;

import java.io.*;
import java.util.TimeZone;

/**
 * ftp工具类
 */
public class FTPUtil {
	private FTPClient ftpClient;
	private String ip;
	private String port;
	private String user;
	private String password;
	private static final Log logger = LogFactory.get();
	public FTPUtil(){
		getProperties();
	}
	public void getProperties(){
		ftpClient = new FTPClient();
		//ip
		this.ip = PropertiesUtil.getPropertiesBykey("ip");
		//用户名称
		this.user = PropertiesUtil.getPropertiesBykey("userName");
		//密码
		this.password = PropertiesUtil.getPropertiesBykey("userPwd");
		this.port = PropertiesUtil.getPropertiesBykey("port");
	}

	/**
	 * 判断是否登入成功
	 *
	 * @return
	 */
	public boolean ftpLogin() {
		boolean isLogin = false;
		FTPClientConfig ftpClientConfig = new FTPClientConfig();
		ftpClientConfig.setServerTimeZoneId(TimeZone.getDefault().getID());
		this.ftpClient.setControlEncoding("UTF-8");
		this.ftpClient.configure(ftpClientConfig);
		try {
			int intPort = Integer.valueOf(this.port);
			if (intPort > 0) {
				this.ftpClient.connect(this.ip, intPort);
			} else {
				this.ftpClient.connect(this.ip);
			}
			// FTP服务器连接回答
			int reply = this.ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				this.ftpClient.disconnect();
				logger.error("登录FTP服务失败！");
				return isLogin;
			}
			this.ftpClient.login(this.user, this.password);
			// 设置传输协议
			this.ftpClient.enterLocalPassiveMode();
			this.ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			logger.info(this.user + "成功登陆FTP服务器");
			isLogin = true;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(this.user + "登录FTP服务失败！" + e.getMessage());
		}
		this.ftpClient.setBufferSize(1024 * 2);
		this.ftpClient.setDataTimeout(30 * 1000);
		return isLogin;
	}

	/**
	 * 退出关闭服务器链接
	 * @return
	 */
	public boolean ftpLogOut() {
		boolean flag = false;
		if (null != this.ftpClient && this.ftpClient.isConnected()) {
			try {
				boolean reuslt = this.ftpClient.logout();// 退出FTP服务器
				if (reuslt) {
					logger.info("成功退出服务器");
					flag = true;
				}
			} catch (IOException e) {
				e.printStackTrace();
				logger.warn("退出FTP服务器异常！" + e.getMessage());
			} finally {
				try {
					this.ftpClient.disconnect();// 关闭FTP服务器的连接
				} catch (IOException e) {
					e.printStackTrace();
					logger.warn("关闭FTP服务器的连接异常！");
				}
			}
		}

		return flag;
	}

	/**
	 * 上传Ftp文件
	 *
	 * @param localFile 当地文件
	 * @param romotUpLoadePath 上传服务器路径
	 *            - 应该以/结束
	 */
	public boolean uploadFile(File localFile, String romotUpLoadePath) {
		boolean success = false;
		BufferedInputStream inStream = null;
		try {
			boolean flag = this.ftpClient.changeWorkingDirectory(romotUpLoadePath);// 改变工作路径
			inStream = new BufferedInputStream(new FileInputStream(localFile));
			logger.info(localFile.getName() + "开始上传.....");
			success = this.ftpClient.storeFile(localFile.getName(), inStream);
			if (success == true) {
				logger.info(localFile.getName() + "上传成功");
				return success;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			logger.error(localFile + "未找到");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (inStream != null) {
				try {
					inStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return success;
	}

	/***
	 * 下载文件
	 *
	 * @param remoteFileName
	 *            待下载文件名称
	 * @param localDires
	 *            下载到当地那个路径下
	 * @param remoteDownLoadPath
	 *            remoteFileName所在的路径
	 */

	public boolean downloadFile(String remoteFileName, String localDires, String remoteDownLoadPath) {
		String strFilePath = localDires + remoteFileName;
		BufferedOutputStream outStream = null;
		boolean success = false;
		try {
			boolean flag = this.ftpClient.changeWorkingDirectory(remoteDownLoadPath);
			logger.info(flag + "$$$$$$$$$$$$$$$$$$$$$");
			outStream = new BufferedOutputStream(new FileOutputStream(strFilePath));
			logger.info(remoteFileName + "开始下载....");
			success = this.ftpClient.retrieveFile(remoteFileName, outStream);
			if (success == true) {
				logger.info(remoteFileName + "成功下载到" + strFilePath);
				return success;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(remoteFileName + "下载失败");
		} finally {
			if (null != outStream) {
				try {
					outStream.flush();
					outStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		if (success == false) {
			logger.error(remoteFileName + "下载失败!!!");
		}
		return success;
	}

}
