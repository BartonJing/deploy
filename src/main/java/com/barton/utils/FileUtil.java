package com.barton.utils;

import java.io.*;

public class FileUtil {
	/**
     * 读取xml文件的内容
     * @param file 想要读取的文件对象
     * @return 返回文件内容
     */
    public static String xml2String(File file){
        StringBuilder result = new StringBuilder();
        try{
            BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
            String s = null;
            while((s = br.readLine())!=null){//使用readLine方法，一次读一行
                result.append(s);
            }
            br.close();    
        }catch(Exception e){
            e.printStackTrace();
        }
        return result.toString();
    }

    /**
     * 去除文件中的BOM
     * @param file
     * @return
     */
    public static InputStream checkForUtf8BOMAndDiscardIfAny(File file){
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        PushbackInputStream pushbackInputStream = new PushbackInputStream(new BufferedInputStream(inputStream), 3);
        byte[] bom = new byte[3];
        try {
            if (pushbackInputStream.read(bom) != -1) {
                if (!(bom[0] == (byte) 0xEF && bom[1] == (byte) 0xBB && bom[2] == (byte) 0xBF)) {
                    pushbackInputStream.unread(bom);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pushbackInputStream;
    }
}
