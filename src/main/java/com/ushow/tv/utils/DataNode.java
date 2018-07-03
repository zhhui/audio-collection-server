package com.ushow.tv.utils;

/**
 * Created by zhonghui on 2018/6/20.
 */
import java.util.Arrays;

public class DataNode {
  double[] datas;

  public double[] getDatas() {
    return datas;
  }

  public void setDatas(double[] datas) {
    this.datas = datas;
  }

  @Override
  public String toString() {
    return "DataNode [datas=" + Arrays.toString(datas) + "]";
  }

  public DataNode() {

  }
  public DataNode(double[] datas) {
    super();
    this.datas = datas;
  }
}