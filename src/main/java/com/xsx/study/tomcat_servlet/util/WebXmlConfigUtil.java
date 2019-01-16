package com.xsx.study.tomcat_servlet.util;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

/**
 * @Description:解析xml
 * @Auther: xsx
 * @Date: 2019/1/15 20:33
 */
public class WebXmlConfigUtil{

    /**
     * 存储servlet class集合
     */
    public  Map<String,Object> servlets = new HashMap<>();

    /**
     * servlet映射
     */
    public  Map<String,Object> servletMapper = new HashMap<>();

    /**
     * web.xml 路径
     */
    private String xmlPath = null;

    /**
     * 解析xml
     * @param xmlPath
     * @return
     */
    public ProjectUtil.WebXml loadXml(String xmlPath) throws Exception {
        this.xmlPath = xmlPath;
        //创建解析器
        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(this.xmlPath);
        //获取根节点
        Element rootElement = document.getRootElement();
        //获取根节点下的所有节点
        List<Element> elementList = rootElement.elements();
        for(Element element : elementList){
            String eleName = element.getName();
            if("servlet".equals(eleName)){
                String servletName = element.elementText("servlet-name");
                String servletClass = element.elementText("servlet-class");
                servlets.put(servletName,servletClass);
            }
            if("servlet-mapping".equals(eleName)){
                String servletName = element.elementText("servlet-name");
                String url = element.elementText("url-pattern");
                servletMapper.put(url,servletName);
            }
        }

        ProjectUtil.WebXml webXml = new ProjectUtil().new WebXml();
        webXml.servlets = this.servlets;
        webXml.servletMapper = this.servletMapper;
        return webXml;
    }

}
