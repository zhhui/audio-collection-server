package com.ushow.tv.socket;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by zhonghui on 2018/6/29.
 */
public class SocketServer {

  private static final Logger log = LoggerFactory.getLogger(SocketServer.class);
  private static boolean status = true;
  private static FileOutputStream fileOutputStream = null;

  /**
   * 启动socket服务，开启监听
   */
  public void startSocketServer(int port) throws Exception {

    String mainAudioFilePath = System.getProperty("user.dir") + "/mainSourceFileDir/";
    String mainAudioFile = createORclearFileIfExists(mainAudioFilePath, "main.raw");

    // 创建UDP套接字
    DatagramSocket serverSocket = null;
    try {
      serverSocket = new DatagramSocket(port);
    } catch (SocketException e) {
      e.printStackTrace();
      log.error("new DatagramSocket error. error message : {}", e.getMessage());
      throw e;
    }

    byte[] receiveData = new byte[3584];
    DatagramPacket receivePacket = new DatagramPacket(receiveData,
      receiveData.length);

    try {
      fileOutputStream = new FileOutputStream(mainAudioFile, true);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      log.error("create main audio file error. error message : {}", e.getMessage());
      throw e;
    }
    try {
      while (status == true) {
        serverSocket.receive(receivePacket);
        writeToFile(receivePacket, fileOutputStream);
      }
    } catch (Exception e) {
      e.printStackTrace();
      log.error("receive data packet error. error message : {}", e.getMessage());
    } finally {
      try {
        fileOutputStream.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public static void writeToFile(DatagramPacket receivePacket, FileOutputStream out) {

    try {
      byte[] soundbytes = receivePacket.getData();
      out.write(soundbytes, 0, soundbytes.length);

      // 44.1K 16位精度 音频转 8k 8位进度音频 FIXME
      // ffmpeg -y -f s16le -ac 1 -ar 44100 -i main.raw  -acodec pcm_s8 -f s8 -b:a 8k -ar 8000 -ac 1 mains8.raw

    } catch (Exception e) {
      System.out.println("Not working in speakers...");
      e.printStackTrace();
    }

  }

  public static String createORclearFileIfExists(String filePath, String fileName) throws Exception {

    String filePathName = filePath + fileName;
    if (createDir(filePath)) {
      File file = new File(filePathName);
      if (!file.exists()) {
        file.createNewFile();
      }
      FileWriter fileWriter = new FileWriter(file);
      fileWriter.write("");
      fileWriter.flush();
      fileWriter.close();

    } else {
      throw new RuntimeException("创建接收主音频的文件失败");
    }
    return filePathName;
  }

  public static boolean createDir(String destDirName) {
    File dir = new File(destDirName);
    if (!dir.exists()) {
      if (!destDirName.endsWith(File.separator)) {
        destDirName = destDirName + File.separator;
      }
      //创建目录
      if (dir.mkdirs()) {
        log.info("创建目录[" + destDirName + "]成功！");
        return true;
      } else {
        log.info("创建目录[{" + destDirName + "}]失败！");
        return false;
      }
    }
    log.info("目录[{" + destDirName + "}]已经存在");
    return true;
  }


}
