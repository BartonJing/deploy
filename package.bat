@echo off
rem create by barton
rem 此脚本进行项目的maven打包，运行此脚本的前提是安装maven,配置maven环境变量
echo "----start package----"
rem 进入对应的目录
rem 清除并打包
echo %1
cd /d %1
echo %cd%
call mvn clean package
pause
exit
