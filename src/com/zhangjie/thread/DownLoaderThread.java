/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zhangjie.thread;

import com.zhangjie.DownLoader;
import static com.zhangjie.DownLoader.instance;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *
 * @author jie
 */
public class DownLoaderThread extends Thread{
        private int threadId;
        private int startIndex;
        private int endIndex;
        private String path;
        private String rename;
        /**
         * @param path 下载文件在服务器上的路径
         * @param threadId 线程Id
         * @param startIndex 线程下载的开始位置
         * @param endIndex	线程下载的结束位置
         * @param rename    下载后的文件位置
         */
        public DownLoaderThread(String path, int threadId, int startIndex, int endIndex, String rename) {
                super();
                this.path = path;
                this.threadId = threadId;
                this.startIndex = startIndex;
                this.endIndex = endIndex;
                this.rename = rename;
        }

    @Override
    public void run() {
        try {
                URL url = new URL(path);
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                conn.setConnectTimeout(5000);
                conn.setRequestMethod("GET");
                //重要:请求服务器下载部分文件 指定文件的位置
                conn.setRequestProperty("Range", "bytes="+startIndex+"-"+endIndex);
                //从服务器请求全部资源返回200 ok如果从服务器请求部分资源 返回 206 ok
                int code = conn.getResponseCode();
                DownLoader.instance.addInfo("线程："+threadId+"开始下载");
                InputStream is = conn.getInputStream();//已经设置了请求的位置，返回的是当前位置对应的文件的输入流
                RandomAccessFile raf = new RandomAccessFile(rename, "rwd");
                //随机写文件的时候从哪个位置开始写
                raf.seek(startIndex);//定位文件

                int len = 0;
                byte[] buffer = new byte[1024];
                while ((len = is.read(buffer)) != -1) {
                        raf.write(buffer, 0, len);
                }
                is.close();
                raf.close();
                DownLoader.instance.addInfo("线程："+threadId+"下载完毕");
        } catch (Exception e) {
                e.printStackTrace();
        }
    }
        
}
