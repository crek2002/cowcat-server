package com.example.lyyserverdemo.lyyframework;

import android.app.Application;
import android.content.Context;
import android.os.Environment;

import androidx.annotation.NonNull;

import com.example.lyyserverdemo.utils.FileUtils;
import com.yanzhenjie.andserver.util.IOUtils;

import org.litepal.LitePal;

import java.io.File;

/**
 * @author Yingyong Lao
 * 创建时间 2022/5/15 9:44
 * @version 1.0
 */
public class LyyApplication extends Application {
    private static LyyApplication lyyApplication;
    private File mRootDir;
    @Override
    public void onCreate() {
        super.onCreate();
        if (lyyApplication==null){
            lyyApplication=this;
            LitePal.initialize(this);
            initRootPath(this);
        }

    }
    public static LyyApplication getInstance(){
        return lyyApplication;
    }

    @NonNull
    public File getRootDir() {
        return mRootDir;
    }

    private void initRootPath(Context context) {
        if (mRootDir != null) {
            return;
        }

        if (FileUtils.storageAvailable()) {
            mRootDir = Environment.getExternalStorageDirectory();
        } else {
            mRootDir = context.getFilesDir();
        }
        mRootDir = new File(mRootDir, "AndServer");
        IOUtils.createFolder(mRootDir);
    }
}
