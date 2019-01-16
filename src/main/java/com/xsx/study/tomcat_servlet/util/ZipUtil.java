package com.xsx.study.tomcat_servlet.util;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;

/**
 * @Description:
 * @Auther: xsx
 * @Date: 2019/1/15 20:19
 */
public class ZipUtil {

    public static void main(String[] args) throws IOException {
        String webapps = "F:\\study_pro\\servlet-demo\\out\\artifacts\\servlet_demo\\";
        unzip(webapps+"servlet-demo.war",webapps);
    }

    /**
     * 解压压缩文件
     * @param sourcePath  目标文件
     * @param unzipSavePath   解压后保存文件目录
     * @throws IOException
     */
    public static void unzip(String sourcePath,String unzipSavePath) throws IOException {
        File file = new File(sourcePath);
        ZipFile zipFile = null;

        try {
            zipFile = new ZipFile(file,"UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("解压文件不存在！");
        }
        Enumeration e = zipFile.getEntries();
        while (e.hasMoreElements()){
            ZipEntry zipEntry = (ZipEntry) e.nextElement();
            if(zipEntry.isDirectory()){
                String name = zipEntry.getName();
                name = name.substring(0,name.length()-1);
                File f = new File(unzipSavePath + name);
                f.mkdirs();
            }else{
                File f = new File(unzipSavePath + zipEntry.getName());
                f.getParentFile().mkdirs();
                f.createNewFile();
                InputStream is = zipFile.getInputStream(zipEntry);
                FileOutputStream fos = new FileOutputStream(f);
                int len = 0;
                byte[] b = new byte[1024];
                while ((len = is.read(b,0,1024)) != -1){
                    fos.write(b,0,len);
                }
                is.close();
                fos.close();
            }
        }
        if(zipFile != null)
            zipFile.close();
//        file.deleteOnExit();
    }
}
