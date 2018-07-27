#start project 查找要启动的项目进程号杀死后启动
NAME=$1
SHELL=$2
echo $NAME
echo $SHELL
ID=`ps -ef | grep "$NAME" | grep -v "grep" | grep -v "$SHELL" | awk '{print $2}'`
if  [ ! -n "$ID" ] ;then
    echo "No startup process was found -- $NAME!"
else
    for id in $ID
    do
        echo "kill process id = $id"
        kill -9 $id
    done
fi
echo "start project $NAME"

nohup java -jar  -Dspring.profiles.active=dev "$NAME"
