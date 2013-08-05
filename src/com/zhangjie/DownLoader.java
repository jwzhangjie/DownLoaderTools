/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zhangjie;

import com.zhangjie.thread.DownLoaderThread;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;

/**
 *
 * @author jie
 */
public class DownLoader extends javax.swing.JFrame {
    public static DownLoader instance;
    /**
     * Creates new form DownLoader
     */
    public DownLoader() {
        initComponents();
        try {
            DownLoader.class.getResource("/");
            initConfigure();
            instance = this;
            download_link.setText(download_path);
        } catch (Exception ex) {
            Logger.getLogger(DownLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        download_link = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        downloader_theadCount = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        download_info = new javax.swing.JTextArea();
        jLabel4 = new javax.swing.JLabel();
        download_start = new javax.swing.JButton();
        download_local = new javax.swing.JButton();
        info_empty = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(400, 300));
        setMinimumSize(new java.awt.Dimension(400, 300));
        setPreferredSize(new java.awt.Dimension(400, 340));
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setText("快速下载助手1.0");
        jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 400, 20));
        getContentPane().add(download_link, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 30, 320, -1));

        jLabel2.setText("文件地址:");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, -1, -1));

        downloader_theadCount.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" }));
        downloader_theadCount.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                downloader_theadCountItemStateChanged(evt);
            }
        });
        getContentPane().add(downloader_theadCount, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 70, 320, -1));

        jLabel3.setText("线程个数:");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, -1, -1));

        download_info.setColumns(20);
        download_info.setRows(5);
        jScrollPane1.setViewportView(download_info);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 100, 320, 140));

        jLabel4.setText("下载进度:");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 150, 60, 40));

        download_start.setText("开始下载");
        download_start.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                download_startMouseClicked(evt);
            }
        });
        getContentPane().add(download_start, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 260, -1, -1));

        download_local.setText("下载位置");
        download_local.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                download_localMouseClicked(evt);
            }
        });
        getContentPane().add(download_local, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 260, -1, -1));

        info_empty.setText("清空缓冲");
        info_empty.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                info_emptyMouseClicked(evt);
            }
        });
        getContentPane().add(info_empty, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 260, 90, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * 初始化或者获取配置信息
     */
    public void initConfigure() throws Exception{
      //属性集合对象
     config = new Properties();
     File file = new File(Property_Path);
      //属性文件流
      FileInputStream fis = new FileInputStream(file.getAbsolutePath());
      //将属性文件流装载到Properties对象中
      config.load(fis);
      thread_Count = Integer.valueOf(config.getProperty("thread_Count"));
      savePath = config.getProperty("savePath");
      fis.close();
      infoBufer = new StringBuffer();
      infoBufer.append(info_item).append("\n");
      CreateDir(savePath);
      downloader_theadCount.setSelectedIndex(thread_Count-1);
      addInfo("默认的线程个数："+thread_Count+"\n保存路径："+savePath);
      
    }
    /**
     * 添加信息
     * @param info 
     */
    public void addInfo(String info){
        infoBufer.append(info).append("\n");
        download_info.setText(infoBufer.toString());
    }
    /**
     * 创建文件夹
     * @param dir 
     */
    public void CreateDir(String dir){
         File file = new File(dir);
        if(!file.exists()){
            file.mkdir();
            addInfo("创建目录："+dir);
            System.out.println();
        }else{
            addInfo("文件夹已经存在");
        }
    }
    /**
     * 选择下载位置
     * @param evt 
     */
    private void download_localMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_download_localMouseClicked
        JFileChooser fileChoose = new JFileChooser();
        fileChoose.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChoose.setDialogTitle(savePath_info);
        int result = fileChoose.showOpenDialog(DownLoader.this);
        if(result == JFileChooser.APPROVE_OPTION){
            savePath = fileChoose.getSelectedFile().getAbsolutePath();
            addInfo("选择的路径："+savePath);
        }else{
            addInfo("使用默认路径："+savePath);
        }
        CreateDir(savePath);
        //存储配置信息
        config.setProperty("savePath", savePath);
        FileOutputStream  out;
        try {
            out = new FileOutputStream(Property_Path);
            config.store(out, Property_Path);
            out.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DownLoader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DownLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_download_localMouseClicked

    /**
     * 开始下载
     * @param evt 
     */
    private void download_startMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_download_startMouseClicked
        try {
            String temp_download_path = download_link.getText().toString().trim();
            if(!"".equals(temp_download_path) || temp_download_path != null){
                download_path = temp_download_path;
            }
            int index = download_path.lastIndexOf("/")+1;
            if(index == 0){
                addInfo("解析下载标题错误");
                return;
            }else{
                 addInfo("解析下载标题:"+index);
            }
            download_name = download_path.substring(index);
            //1.连接服务器，获取一个文件，获取文件的长度，在本地创建一个跟服务器一样大小的临时文件
                     URL url = new URL(download_path);
                     HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                     conn.setConnectTimeout(5000);
                     conn.setRequestMethod("GET");
                     int code = conn.getResponseCode();
                     if (code == 200) {
                             //服务器端返回的数据的长度，实际上就是文件的长度
                             int length = conn.getContentLength();
                             addInfo("文件总长度："+length);
                             //在客户端本地创建出来一个大小跟服务器端一样大小的临时文件
                             String temp_download_locaPath = savePath+"/"+download_name;
                             File file = new File(temp_download_locaPath);
                             if(file.exists()){
                                file.delete();
                             }
                             RandomAccessFile raf = new RandomAccessFile(temp_download_locaPath, "rwd");
                             //指定创建的这个文件的长度
                             raf.setLength(length);
                             raf.close();
                             //假设是3个线程去下载资源。
                             //平均每一个线程下载的文件大小.
                             int blockSize = length / thread_Count;
                             for (int threadId = 1; threadId <= thread_Count; threadId++) {
                                     //第一个线程下载的开始位置
                                     int startIndex = (threadId - 1) * blockSize;
                                     int endIndex = threadId * blockSize - 1;
                                     if (threadId == thread_Count) {//最后一个线程下载的长度要稍微长一点
                                             endIndex = length;
                                     }
                                    addInfo(temp_download_locaPath+"线程："+threadId+"下载:---"+startIndex+"--->"+endIndex);
                                    new DownLoaderThread(download_path, threadId, startIndex, endIndex, temp_download_locaPath).start();
                             }
                     
                     }else {
                            addInfo("服务器错误！");
                     }
        } catch (Exception ex) {
            Logger.getLogger(DownLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_download_startMouseClicked

    private void downloader_theadCountItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_downloader_theadCountItemStateChanged
        // TODO add your handling code here:
      JComboBox com = (JComboBox)evt.getSource();
      thread_Count = Integer.valueOf(com.getSelectedItem().toString());
      //存储配置信息
        config.setProperty("thread_Count", String.valueOf(thread_Count));
        FileOutputStream  out;
        try {
            out = new FileOutputStream(Property_Path);
            config.store(out, Property_Path);
            out.close();
        } catch (Exception ex) {
            Logger.getLogger(DownLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_downloader_theadCountItemStateChanged

    private void info_emptyMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_info_emptyMouseClicked
        if(infoBufer != null){
            int len = infoBufer.length();
            infoBufer.delete(0, len);
            addInfo(info_item);
        }
    }//GEN-LAST:event_info_emptyMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(DownLoader.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DownLoader.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DownLoader.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DownLoader.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new DownLoader().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JTextArea download_info;
    private javax.swing.JTextField download_link;
    private javax.swing.JButton download_local;
    private javax.swing.JButton download_start;
    private javax.swing.JComboBox downloader_theadCount;
    private javax.swing.JButton info_empty;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables

private String Property_Path = "Config/Config.properties";
private String savePath_info = "请选择保存路径";
private String savePath = "D:/ZJ_DownLoader";
private String download_path = "http://softdownload.hao123.com/hao123-soft-online-bcs/soft/Y/2013-07-18_YoudaoDict_baidu.alading.exe";
private String download_name = "downloader.exe";
private String info_item = "欢迎使用快速下载助手-->并不是线程多就下载的快!";
private int thread_Count = 3;
private Properties config;
private StringBuffer infoBufer;
}
