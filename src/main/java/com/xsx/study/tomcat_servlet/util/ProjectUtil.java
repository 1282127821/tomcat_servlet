package com.xsx.study.tomcat_servlet.util;

import com.xsx.study.tomcat_servlet.constant.Constants;

import javax.servlet.Servlet;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description:加载项目
 * @Auther: xsx
 * @Date: 2019/1/15 20:13
 */
public class ProjectUtil {

    public static Map<String,WebXml> result = new HashMap<>();

    //项目存放目录，等于tomcat的webapps目录
    private final static String webapps = Constants.WEBAPPS;

    public static void load() {
        try{
            //获取webapps下的所有文件
            File[] files = new File(webapps).listFiles(File::isFile);
            //解压war
            for(File file : files){
                if(file.getName().endsWith(".war")){
                    ZipUtil.unzip(webapps + file.getName(),webapps+file.getName().substring(0,file.getName().lastIndexOf("."))+"\\");
                }
            }
            //获取webapps下的所有文件夹（也就是获取所有项目，一个文件夹可视为一个项目）
            File[] projects = new File(webapps).listFiles(File::isDirectory);
            for(File project : projects){
                //读取web.xml文件，解析里边的servlet
                WebXml webXml = new WebXmlConfigUtil().loadXml(project.getPath() + "\\WEB-INF\\web.xml");
                webXml.projectPath = project.getPath();
                //加载创建对象
                webXml.loadServlet();
                result.put(project.getName(),webXml);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 存放配置
     */
    public class WebXml{
        /**
         * 项目文件夹地址
         */
        public String projectPath = null;
        /**
         * 存储servlet class集合
         */
        public Map<String,Object> servlets = new HashMap<>();
        /**
         * servlet映射
         */
        public Map<String,Object> servletMapper = new HashMap<>();
        /**
         * 实例对象
         */
        public  Map<String,Object> servletInstances = new HashMap<>();

        public void loadServlet() throws Exception {
            //执行、创建一个类的实例
            URLClassLoader loader = new URLClassLoader(new URL[]{
                new URL("file:"+projectPath + Constants.WEBINF_CLASS_PATH)
            });
            //遍历web.xml中servler class的定义
            for(Map.Entry<String,Object> entry : servlets.entrySet()){
                String servletName = entry.getKey();
                String servletClass = entry.getValue().toString();
                //加载class到jvm
               Class<?> clazz = loader.loadClass(servletClass);
                //创建对象 -- 反射
                Servlet servlet = (Servlet) clazz.newInstance();
                //存储实例对象
                servletInstances.put(servletName,servlet);
            }
        }
    }
}
