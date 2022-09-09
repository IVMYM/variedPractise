package com.example.variesdiffmayfuse.multi;

/**
 * 多线程分段下载
 * @createdTime: 2022/7/19 16:16
 * @description: 1.0    yuanliu	2022/7/19	创建初始版本
 */
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * 多线程下载模型
 *
 * @author bridge
 */
public class MultiThreadDownLoad {
    /**
     * 同时下载的线程数
     */
    private int threadCount;
    /**
     * 服务器请求路径
     */
    private String serverPath;
    /**
     * 本地路径
     */
    private String localPath;
    /**
     * 线程计数同步辅助
     */
    private CountDownLatch latch;

    public MultiThreadDownLoad(int threadCount, String serverPath, String localPath, CountDownLatch latch) {
        this.threadCount = threadCount;
        this.serverPath = serverPath;
        this.localPath = localPath;
        this.latch = latch;
    }

    public void executeDownLoad() {

        try {
            URL url = new URL(serverPath);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            int code = conn.getResponseCode();
            if (code == 200) {
                //服务器返回的数据的长度，实际上就是文件的长度,单位是字节
                int length = conn.getContentLength()-1;
                System.out.println("文件总长度:" + length + "字节(B)");
                RandomAccessFile raf = new RandomAccessFile(localPath, "rwd");
                //指定创建的文件的长度
                raf.setLength(length);
                raf.close();
                //分割文件
                int blockSize = length / threadCount;
                for (int threadId = 1; threadId <= threadCount; threadId++) {
                    //第一个线程下载的开始位置
                    int startIndex = (threadId - 1) * blockSize;
                    int endIndex = startIndex + blockSize - 1;
                    if (threadId == threadCount) {
                        //最后一个线程下载的长度稍微长一点
                        endIndex = length;
                    }
                    System.out.println("线程" + threadId + "下载:" + startIndex + "字节~" + endIndex + "字节");
                    new DownLoadThread(threadId, startIndex, endIndex).start();
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    /**
     * 内部类用于实现下载
     */
    public class DownLoadThread extends Thread {
        /**
         * 线程ID
         */
        private int threadId;
        /**
         * 下载起始位置
         */
        private int startIndex;
        /**
         * 下载结束位置
         */
        private int endIndex;

        public DownLoadThread(int threadId, int startIndex, int endIndex) {
            this.threadId = threadId;
            this.startIndex = startIndex;
            this.endIndex = endIndex;
        }


        @Override
        public void run() {
            try {
                System.out.println("线程" + threadId + "正在下载...");
                URL url = new URL(serverPath);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                //请求服务器下载部分的文件的指定位置
                conn.setRequestProperty("range", "bytes=" + startIndex + "-" + endIndex);
                conn.setConnectTimeout(5000);
                int code = conn.getResponseCode();
                System.out.println("线程" + threadId + "请求返回code=" + code);
                InputStream is = conn.getInputStream();//返回资源
                RandomAccessFile raf = new RandomAccessFile(localPath, "rwd");
                //随机写文件的时候从哪个位置开始写
                raf.seek(startIndex);//定位文件
                int len = 0;
                byte[] buffer = new byte[1024];
                while ((len = is.read(buffer)) != -1) {
                    raf.write(buffer, 0, len);
                }
                is.close();
                raf.close();
                System.out.println("线程" + threadId + "下载完毕");
                //计数值减一
                latch.countDown();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public static void main(String[] args) {
        //msfile/filecenter/downloadmulti?appfileid=
        String appfileids = "AF997237083104866304,AF987848359167918080,AF987851779559915520,AF987848680397078528,AF987849051471347712,AF987848647572455424,AF987849034362781696,AF997237115329703936,AF997237118362185728,AF997237120874573824";
        String dommain = "http://finedofilecent.ningbo-central-cluster.4a.cmit.cloud:20030/";
        String appsec = "8d343bdf2d16cf9ccd5c5bb669211754";
        String appid = "APP00000001";
        String urlString = dommain + "msfile/filecenter/downloadmulti?appfileid=" + appfileids + "&appid=" + appid + "&appsecret=" + appsec;
        String ideaurl = "http://localhost:8888/group1/M00/00/32/wKjIIGLM5pKAZ4dLAAB8oSwOziA71.pptx";

                int threadSize = 2;
                String serverPath = ideaurl;
                String localPath = "d:/download/1.pptx";
                CountDownLatch latch = new CountDownLatch(threadSize);
                MultiThreadDownLoad m = new MultiThreadDownLoad(threadSize, serverPath, localPath, latch);
                long startTime = System.currentTimeMillis();
                try {
                    m.executeDownLoad();
                    latch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                long endTime = System.currentTimeMillis();
                System.out.println("全部下载结束,共耗时" + (endTime - startTime) / 1000 + "s");
            }


}

