#!/bin/sh
# 此脚本进行项目的maven打包，运行此脚本的前提是安装maven,配置maven环境变量
echo "----start package----"
# 进入对应的目录
# 清除并打包
echo $1
cd $1
pwd
mvn clean package
#echo "press any key to continue"
#read anykey
exit
