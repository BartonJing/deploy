@ECHO OFF
rem start
set CLASSPATH=%JAVA_HOME%/lib
set JAVA=%JAVA_HOME%/bin/java

set CLASSPATH=%CLASSPATH%;../conf;../bin
set JAVA_OPTIONS=-Djava.ext.dirs="../lib"

"%JAVA%" -Xms512m -Xmx1024m -classpath "%CLASSPATH%" %JAVA_OPTIONS% com.barton.WinDeploy
@pause
