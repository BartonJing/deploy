package com.barton.utils;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ProcessUtil {
    /**
     * 执行
     * @param cmd
     * @return
     */
    public static boolean convert(StringBuilder cmd){
        boolean result = true;
        try{
            //执行构造好命令
            Process proc = Runtime.getRuntime().exec(cmd.toString());
            //读取错误日志
            //ProcessInterceptor error = new ProcessInterceptor(proc.getErrorStream());
            //读取输出日志
            //ProcessInterceptor output = new ProcessInterceptor(proc.getInputStream());
            try{
                InputStreamReader isr = new InputStreamReader(proc.getInputStream(), "gbk");
                BufferedReader br = new BufferedReader(isr);
                String line = null;
                while ((line = br.readLine()) != null) {
                    //logger.info(line.toString()); //输出内容
                    System.out.print(line.toString());
                }
            }catch (IOException e){
                e.printStackTrace();
            }
            //error.start();
           // output.start();
            proc.waitFor();
        }catch(Exception e){
            result = false;
            e.printStackTrace();
        }
        return result;
    }
}
