#!/bin/sh
export CLASSPATH=$JAVA_HOME/lib:../conf:../bin
export JAVA=$JAVA_HOME/bin/java
JAVA_OPTIONS=-Djava.ext.dirs="../lib"
echo $CLASSPATH
"$JAVA" -Xms512m -Xmx1024m -classpath "$CLASSPATH" $JAVA_OPTIONS com.barton.LinuxDeploy