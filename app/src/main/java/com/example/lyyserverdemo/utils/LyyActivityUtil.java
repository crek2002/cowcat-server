package com.example.lyyserverdemo.utils;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * 活动管理工具
 * @author Yingyong Lao
 * 创建时间 2022/5/20 23:20
 * @version 1.0
 */
public class LyyActivityUtil {
    private static volatile LyyActivityUtil util;
    private static List<Activity> list=new ArrayList<>();
    private LyyActivityUtil(){
    }
    public static LyyActivityUtil getInstance(){
        if (util==null){
            synchronized (LyyActivityUtil.class){
                if (util==null){
                    util=new LyyActivityUtil();
                }
            }
        }
        return util;
    }

    public void addActToList(Activity activity){
        list.add(activity);
    }

    public void removeActFromList(Activity activity){
        list.remove(activity);
    }

    public void exitApp(){
        for (Activity activity : list) {
            if (!activity.isFinishing()){
                activity.finish();
            }
        }
        //Process.killProcess(Process.myPid());//杀掉当前进程
    }
}
