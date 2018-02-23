package com.example.dell.mymediaplay.tool;

import java.util.ArrayList;

/**
 * 实现AbstructProvider接口，通过cursor来搜索视频的相关信息
 */
public interface AbstructProvider {
    public ArrayList<Video> getList();//返回一个封装了所有视频的信息的list
}
