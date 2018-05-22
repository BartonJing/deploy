
#start project 查找要启动的项目进程号杀死后启动
NAME=$1
echo $NAME
ID=`ps -ef | grep "$NAME" | grep -v "grep" | awk '{print $2}'`
echo $ID
echo "---------------"
for id in $ID
do
kill -9 $id
echo "killed $id"
echo "start #NAME"
#nohup java -jar  -Dspring.profiles.active=prod -Xms8g -Xmx32g "$NAME"
echo "java -jar  -Dspring.profiles.active=dev -Xms8g #NAME"
nohup java -jar  -Dspring.profiles.active=dev -Xms8g "$NAME"

done
echo "---------------"