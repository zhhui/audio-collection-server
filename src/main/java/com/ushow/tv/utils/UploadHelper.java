package com.ushow.tv.utils;


import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

/**
 * @author zhonghui
 * @create 2012-11-19 下午6:24:03
 * @description 上传帮助类
 */
public class UploadHelper {

  /**
   * @param maxLength 文件最大限制
   * @param allowExtName 不允许上传的文件扩展名
   * @return MultipartFile集合
   * @descrption 根据HttpServletRequest对象获取MultipartFile集合
   * @author xdwang
   * @create 2012-11-19下午5:11:41
   */
  public static List<MultipartFile> getFileSet(HttpServletRequest request,
    long maxLength, String[] allowExtName) {
    MultipartHttpServletRequest multipartRequest = null;
    try {
      multipartRequest = (MultipartHttpServletRequest) request;
    } catch (Exception e) {
      return new LinkedList<MultipartFile>();
    }

    List<MultipartFile> files = new LinkedList<MultipartFile>();
    files = multipartRequest.getFiles("attach");
    // 移除不符合条件的
    for (int i = 0; i < files.size(); i++) {
      if (!validateFile(files.get(i), maxLength, allowExtName)) {
        files.remove(files.get(i));
        if (files.size() == 0) {
          return files;
        }
      }
    }
    return files;
  }

  /**
   * @param file MultipartFile对象
   * @param path 保存路径，如“D:\\File\\”
   * @return 保存的全路径 如“D:\\File\\2345678.txt”
   * @throws Exception 文件保存失败
   * @descrption 保存文件
   * @author xdwang
   * @create 2012-11-19下午4:17:36
   */
  public static String uploadFile(MultipartFile file, String path, String profix)
    throws Exception {

    String filename = file.getOriginalFilename();
    String extName = filename.substring(filename.lastIndexOf("."))
      .toLowerCase();
    String fileProfixName = UUID.randomUUID().toString();
    if(StringUtils.isNotBlank(profix)){
      fileProfixName = profix;
    }
    String lastFileName = fileProfixName + extName;
    if (!path.endsWith(File.separator)) {
      path = path + File.separator;
    }
    File temp = new File(path);
    if (!temp.isDirectory()) {
      temp.mkdir();
    }
    // 文件存储的全路径
    String fileFullPath = path + lastFileName;
    FileCopyUtils.copy(file.getBytes(), new File(fileFullPath));
    return fileFullPath;
  }

  /**
   * @param file MultipartFile对象
   * @param maxLength 文件最大限制
   * @param allowExtName 不允许上传的文件扩展名
   * @return 文件格式是否合法
   * @descrption 验证文件格式，这里主要验证后缀名
   * @author xdwang
   * @create 2012-11-19下午4:08:12
   */
  private static boolean validateFile(MultipartFile file, long maxLength,
    String[] allowExtName) {
    if (file.getSize() < 0 || file.getSize() > maxLength) {
      return false;
    }
    String filename = file.getOriginalFilename();

    // 处理不选择文件点击上传时，也会有MultipartFile对象，在此进行过滤
    if (filename == "") {
      return false;
    }
    String extName = filename.substring(filename.lastIndexOf("."))
      .toLowerCase();
    if (allowExtName == null || allowExtName.length == 0
      || Arrays.binarySearch(allowExtName, extName) != -1) {
      return true;
    } else {
      return false;
    }
  }

}
