# deploy
deploy(springboot项目打包发布
目的：解决springboot项目从开发机（windows,linux）打包发布部署到生成环境(linux)
项目自动化部署有开源的jenkins,试了下它的使用，功能很强大，使用界面也很友好，但是自己的项目不需要那么多的功能，
尤其是jenkins要在安装单独的服务，在服务器上还得安装maven,git等觉得比较麻烦，于是自己根据打包发布部署的操作使用
java+shell 编写了此项目：
此项目比较简单，暂时只实现了从window,linux到Linux的打包发布，其余平台的接口以提供，未实现。

可直接下载deploy-1.0.rar解压运行

项目使用maven 构建 pom文件如下

    <?xml version="1.0" encoding="UTF-8"?>
    <project xmlns="http://maven.apache.org/POM/4.0.0"
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
        <modelVersion>4.0.0</modelVersion>
    
        <groupId>com.barton</groupId>
        <artifactId>deploy</artifactId>
        <version>1.0-SNAPSHOT</version>
        <packaging>jar</packaging>
        <dependencies>
            <dependency>
                <groupId>commons-net</groupId>
                <artifactId>commons-net</artifactId>
                <version>3.6</version>
            </dependency>
            <dependency>
                <groupId>cn.hutool</groupId>
                <artifactId>hutool-all</artifactId>
                <version>4.0.9</version>
            </dependency>
            <!-- https://mvnrepository.com/artifact/ch.ethz.ganymed/ganymed-ssh2 -->
            <dependency>
                <groupId>ch.ethz.ganymed</groupId>
                <artifactId>ganymed-ssh2</artifactId>
                <version>262</version>
            </dependency>
            <dependency>
                <groupId>commons-io</groupId>
                <artifactId>commons-io</artifactId>
                <version>2.4</version>
                <type>jar</type>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>commons-lang</groupId>
                <artifactId>commons-lang</artifactId>
                <version>2.6</version>
                <type>jar</type>
                <scope>compile</scope>
            </dependency>
            <dependency>
                <groupId>com.jcraft</groupId>
                <artifactId>jsch</artifactId>
                <version>0.1.54</version>
            </dependency>
    
        </dependencies>
        <build>
            <sourceDirectory>src/main/java</sourceDirectory>
            <resources>
                <!-- 把src/main/resources目录下所有的文件拷贝到conf目录中 -->
                <resource>
                    <directory>src/main/resources</directory>
                    <includes>
                        <include>**/*.properties</include>
                        <include>**/*.xml</include>
                    </includes>
                    <targetPath>${project.build.directory}/conf</targetPath>
                </resource>
                <!-- 把lib目录下所有的文件拷贝到lib目录中
                （可能有些jar包没有办法在maven中找到，需要放在lib目录中） -->
                <resource>
                    <directory>lib</directory>
                    <targetPath>${project.build.directory}/lib</targetPath>
                </resource>
                <!-- 把放在根目录下的脚本文件.sh,.bat拷贝到bin目录中 -->
                <resource>
                    <directory>.</directory>
                    <includes>
                        <include>**/*.sh</include>
                        <include>**/*.bat</include>
                    </includes>
                    <targetPath>${project.build.directory}/bin</targetPath>
                </resource>
                <!-- 把src/main/resources目录下所有的文件拷贝到conf目录中 -->
                <resource>
                    <directory>src/main/resources</directory>
                    <includes>
                        <include>**/*.sh</include>
                        <include>**/*.bat</include>
                    </includes>
                    <targetPath>${project.build.directory}/bin</targetPath>
                </resource>
            </resources>
    
            <plugins>
                <!-- 用于编译的plugin -->
                <plugin>
                    <groupId>org.apache.maven.plugins</groupId>
                    <artifactId>maven-compiler-plugin</artifactId>
                    <version>3.1</version>
                    <configuration>
                        <fork>true</fork>
                        <!--<defaultLibBundleDir>lib</defaultLibBundleDir>-->
                        <source>1.8</source>
                        <target>1.8</target>
                        <encoding>UTF-8</encoding>
                        <!-- 如果配置了JAVA_HOME,下面应该可以不用配 -->
                        <!--<executable>C:\Program Files (x86)\Java\jdk1.8.0_91\bin\javac.exe</executable>-->
                    </configuration>
                </plugin>
                <!-- 用于生成jar包的plugin -->
                <plugin>
                    <groupId>org.apache.maven.plugins</groupId>
                    <artifactId>maven-jar-plugin</artifactId>
                    <version>2.6</version>
                    <configuration>
                        <!-- 把生成的jar包放在lib目录下（和其他所有jar包一起） -->
                        <outputDirectory>${project.build.directory}/lib</outputDirectory>
                        <archive>
                            <manifest>
                                <addClasspath>true</addClasspath>
                                <classpathPrefix>lib/</classpathPrefix>
                            </manifest>
                        </archive>
                        <excludes>
                            <!-- 排除掉一些文件,不要放到jar包中，
                            这里是为了排除掉src/main/resources中的文件（它们应该放到conf目录）
                            这里只能指定要排除的目标文件，而不能指定源文件，虽然不够完美，但是基本能达到目的。 -->
                            <exclude>*.xml</exclude>
                            <exclude>*.properties</exclude>
                        </excludes>
                    </configuration>
                </plugin>
    
                <!-- 用于拷贝maven依赖的plugin -->
                <plugin>
                    <groupId>org.apache.maven.plugins</groupId>
                    <artifactId>maven-dependency-plugin</artifactId>
                    <version>2.10</version>
                    <executions>
                        <execution>
                            <id>copy-dependencies</id>
                            <phase>package</phase>
                            <goals>
                                <goal>copy-dependencies</goal>
                            </goals>
                            <configuration>
                                <!-- 把依赖的所有maven jar包拷贝到lib目录中（这样所有的jar包都在lib目录中） -->
                                <outputDirectory>${project.build.directory}/lib</outputDirectory>
                            </configuration>
                        </execution>
                    </executions>
                </plugin>
    
                <!-- 用于拷贝resource的plugin -->
                <plugin>
                    <groupId>org.apache.maven.plugins</groupId>
                    <artifactId>maven-resources-plugin</artifactId>
                    <version>2.7</version>
                    <configuration>
                        <encoding>UTF-8</encoding>
                    </configuration>
                </plugin>
    
                <!-- 配置生成源代码jar的plugin -->
                <plugin>
                    <artifactId>maven-source-plugin</artifactId>
                    <version>2.4</version>
                    <configuration>
                        <attach>true</attach>
                        <!--<encoding>UTF-8</encoding>-->
                        <!-- 配置源代码jar文件的存放路径，和其他jar文件一起放在lib目录 -->
                        <outputDirectory>${project.build.directory}/lib</outputDirectory>
                    </configuration>
                    <executions>
                        <execution>
                            <phase>compile</phase>
                            <goals>
                                <goal>jar</goal>
                            </goals>
                        </execution>
                    </executions>
                </plugin>
    
            </plugins>
        </build>
    
    </project>

（注:代码调试时请注释掉 pom 文件中的 build 标签内容)


使用步骤：

        1.需要在自己的服务器上安装你的项目运行所需环境，确保手动部署可以启动并正常运行，如：jdk等
        
        2.下载deploy-1.0.rar解压
        
        3.配置conf下的config.xml文件
        
            <properties>
                <!--项目信息-->
                <project>
                    <!--项目编号-->
                    <code>0</code>
                    <!--是否打包-->
                    <isPackage>false</isPackage>
                    <!--项目源 信息-->
                    <src>
                        <!--项目名称-->
                        <name>test-0.0.1-SNAPSHOT.jar</name>
                        <!--项目目录-->
                        <dir>D:\\workspace\\test\\</dir>
                        <!--打包后的jar包存放位置-->
                        <path>D:\\workspace\\test\\target\\ssm-0.0.1-SNAPSHOT.jar</path>
                    </src>
                    <!--项目目标 信息-->
                    <target>
                        <!--ip地址-->
                        <ip>xxx.xx.x.xx75</ip>
                        <!--用户-->
                        <username>username</username>
                        <!--密码-->
                        <password>password</password>
                        <!--端口-->
                        <port>22</port>
                        <!--私钥地址-->
                        <keyFilePath></keyFilePath>
                        <!--存放项目目录-->
                        <dir>/home/test/test/</dir>
                        <!--项目路径-->
                        <path>/home/test/test/test-0.0.1-SNAPSHOT.jar</path>
                        <!--是否备份-->
                        <backup>false</backup>
                        <!--备份目录-->
                        <backupDir>/home/test/test/backup/</backupDir>
                    </target>
                </project>
            </properties>
            
        4.双击bin文件下的start.bat（linux start.sh）文件启动，即可完成部署

        其他配置：一般的springboot 项目使用上述步骤就可以了，但是如果是有其他的启动等方式，可以修改bin目录下的shell脚本
        已达到自己的需求
    
    


    
