package com.example.lyyserverdemo.lyyframework;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.lyyserverdemo.utils.LyyActivityUtil;
import com.example.lyyserverdemo.utils.LyyLogUtil;

/**
 * 活动基类
 * @author Yingyong Lao
 * 创建时间 2022/5/20 23:18
 * @version 1.0
 */
public class LyyBaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LyyLogUtil.logD(this.getClass().getName()+"\tonCreate()...");
        LyyActivityUtil.getInstance().addActToList(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LyyLogUtil.logD(this.getClass().getName()+"\tonDestroy()...");
        LyyActivityUtil.getInstance().removeActFromList(this);
    }
}
