package com.ushow.tv.controller.common;

/**
 * Created by zhonghui on 2017/6/29.
 */
public class Consts {

  public static final String SUCCESS = "A00000";// 成功
  public static final String FAILURE = "A00001";// 失败
  public static final String ERR_SYS = "A00002";// 系统错误
  public static final String ERR_UPDATE_USER_INFO = "A00003";// 更新用户信息时错误

  public static final String ERR_ILLEGAL_PARAM = "A00100";// 非法参数
  public static final String ERR_ILLEGAL_DATE = "A00101";// 非法日期
  public static final String RESOURCE_ACCESS_EXCEPTION = "A00102";// 非法日期
  public static final String ERR_ILLEGAL_REQUEST = "A00103";// 非法请求

  public final static String NOT_EXIST_USER = "A00200";// 用户不存在
  public final static String EXIST_APPROVAL_RECORD = "A00201";// 已存在待审核的记录
  public final static String NOT_FENCHENG_USER = "A00202";// 非分成用户
  public final static String FENCHENG_USER_INFO_IS_COMPLETE = "A00203";// 资料齐全，无需申请

  public final static String QIYI_VERIFY_FILE_EXIST = "A00300";
  public final static String QIYI_VERIFY_FILE_ERROR_SAVE = "A00301";// IOException
  public final static String MAX_UPLOAD_SIZE_EXCEEDED_EXCEPTION = "A00302";// IOException
  public final static String VERIFY_FILE_UPLOAD_SIZE_EXCEEDED_EXCEPTION = "A00303";// IOException
  public final static String REST_CLIENT_EXCEPTION = "A00307";

  public final static String VERIFY_EXIST_VERIFY_INFO = "A00400";// 已经存在认证信息
  public final static String VERIFY_NOT_FOUND_USER_INFO = "A00401";// 用户不存在
  public final static String AS_MCN_OPERATOR = "A00402";//用户是运营人员

  public static final String API_LOGIN = "B00000";// 已登录
  public static final String API_NOT_LOGIN = "B00001";// 未登录
  public static final String ILLEGAL_SECRET_KEY = "B00002";// 非法key

  public static final String RAW_CUTTING_FILEDIR = "/tmpfile/";
  public static final int MAIN_AUDIO_DURTIME = 60;// 单位秒 最后 durTime 秒主音频
  public static final int COMPARE_AUDIO_DURTIME = 5;// 单位秒 比较音频时间
  public static final int STEP = 16; // 单位 bit 位

}
