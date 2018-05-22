package com.barton.utils;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ProcessInterceptor extends Thread {
    private static final Log logger = LogFactory.get();
    private InputStream is;

    public ProcessInterceptor(InputStream is){
        this.is = is;
    }

    public void run(){
        try{
            InputStreamReader isr = new InputStreamReader(is, "gbk");
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null) {
                logger.info(line.toString()); //输出内容
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
