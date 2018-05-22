package com.barton.utils;


import com.barton.domain.config.Project;
import com.barton.domain.config.Properties;
import com.barton.domain.config.Src;
import com.barton.domain.config.Target;

import java.io.File;

/**
 * 读取properties工具类
 */
public class PropertiesUtil {

    private Properties properties;
    public static PropertiesUtil propertiesUtil;

    public static PropertiesUtil getInstance(){
        if(propertiesUtil == null){
            propertiesUtil = new PropertiesUtil();
        }
        return propertiesUtil;
    }
    public PropertiesUtil(){
        initProperties();
    }
    /**
     * 读取properties中的信息
     * @param key
     * @return
     */
    public static String getPropertiesBykey(String key){
        /*Properties pro = new Properties();
        String path = PropertiesUtil.class.getClass().getResource("/config.properties").getPath();
        try {
            InputStream inStream = new FileInputStream(path);
            pro.load(inStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String value = pro.getProperty(key);*/
        return "";
    }

    /**
     * 初始化配置信息
     */
    public void initProperties(){
        String path = PropertiesUtil.class.getClass().getResource("/config.xml").getPath();
        String xmlStr = FileUtil.xml2String(new File(path));

        Properties properties = JaxbMapper.fromXml(xmlStr, com.barton.domain.config.Properties.class);
        if(properties == null){
            throw new RuntimeException("未读取到配置文件，请检查配置文件是否正确");
        }

        this.properties = properties;
        if(this.properties.getProject() == null || this.properties.getProject().size() <= 0){
            throw new RuntimeException("配置文件中未配置任何项目");
        }
    }
    /**
     * 获取一个配置信息
     */
    public Project generateOneProject(Integer code){
        if(code == null){
            code = 0;
        }
        if(this.properties.getProject().get(code) == null){
            throw new RuntimeException("未检测到任何项目信息，请检查配置文件是否有误");
        }
        if(this.properties.getProject() == null || this.properties.getProject().size() <= 0){
            return new Project();
        }
        return this.properties.getProject().get(code);
    }
    /**
     *
     * 获取一个项目源配置信息
     */
    public Src generateOneSrc(Integer code){
        return generateOneProject(code).getSrc();
    }
    /**
     * 获取一个项目目标配置信息
     */
    public Target generateOneTarget(Integer code){
        return generateOneProject(code).getTarget();
    }

    /**
     * 获取配置的项目数量
     * @return
     */
    public Integer generateProjectsSize(){
        return this.properties.getProject().size();
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

public static void main(String [] args){
    PropertiesUtil propertiesUtil = PropertiesUtil.getInstance();
    Target target = propertiesUtil.generateOneTarget(0);
    System.out.print(target);

}
}
