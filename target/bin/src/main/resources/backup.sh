
#backup 备份文件
src=$1
target=$2
echo $src
echo $target
cp -u -v $src $target
#chmod 777 $target