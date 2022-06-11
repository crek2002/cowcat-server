package com.example.lyyserverdemo.component;

import android.content.Context;

import com.yanzhenjie.andserver.annotation.Config;
import com.yanzhenjie.andserver.framework.config.Multipart;
import com.yanzhenjie.andserver.framework.config.WebConfig;
import com.yanzhenjie.andserver.framework.website.AssetsWebsite;

import java.io.File;

/**
 * @author Yingyong Lao
 * 创建时间 2022/5/14 13:25
 * @version 1.0
 */
@Config
public class AppConfig implements WebConfig {
    @Override
    public void onConfig(Context context, Delegate delegate) {
        delegate.addWebsite(new AssetsWebsite(context, "/web"));

        delegate.setMultipart(Multipart.newBuilder()
                .allFileMaxSize(1024*1024*2000)//单个请求上传文件总大小
                .fileMaxSize(1024*1024*2000)//单个文件的最大大小
                .maxInMemorySize(1024*1024*10)//保存上传文件时buffer大小
                .uploadTempDir(new File(context.getCacheDir(), "_server_upload_cache_")) // 文件保存目录
                .build());
    }
}
