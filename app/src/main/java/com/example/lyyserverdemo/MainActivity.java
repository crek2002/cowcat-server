package com.example.lyyserverdemo;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.lyyserverdemo.broadcastreceiver.LyyServerBroadcastReceiver;
import com.example.lyyserverdemo.lyyframework.LyyBaseActivity;
import com.example.lyyserverdemo.lyyframework.widget.LyyDialog;
import com.example.lyyserverdemo.service.LyyServerService;
import com.example.lyyserverdemo.utils.LyyActivityUtil;
import com.example.lyyserverdemo.utils.LyyLogUtil;

import java.io.File;

public class MainActivity extends LyyBaseActivity {
    public static final int REQUEST_WRITE = 2;
    private Toolbar toolbar;
    private WebView webView;
    private Menu menu;
    private ProgressDialog progressDialog;
    private ValueCallback<Uri[]> mUploadMessage5;
    private static final int FILECHOOSER_RESULTCODE_FOR_ANDROID_5 = 5174;
    private static final String TAG = "MainActivity";
    private final LyyServerBroadcastReceiver lyyServerBroadcastReceiver=new LyyServerBroadcastReceiver(new LyyServerBroadcastReceiver.ServerStateListener() {
        @Override
        public void onStart(String hostAddress) {
            LyyLogUtil.logD("服务器已经启动，地址为："+hostAddress);
            toolbar.setSubtitle("http://"+hostAddress+":8080");

            webView.loadUrl("http://"+hostAddress+":8080");
            if (menu!=null){
                menu.findItem(R.id.stop).setVisible(true);
                menu.findItem(R.id.start).setVisible(false);
            }
        }

        @Override
        public void onStop() {
            LyyLogUtil.logD("服务器已经停止");
            toolbar.setSubtitle("服务已停止");
            progressDialog.dismiss();
            if (menu!=null){
                menu.findItem(R.id.stop).setVisible(false);
                menu.findItem(R.id.start).setVisible(true);
            }
        }

        @Override
        public void onError(String error) {
            super.onError(error);
            LyyLogUtil.logE(error);
            Toast.makeText(MainActivity.this, error, Toast.LENGTH_LONG).show();
        }
    });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        registerReceiver(lyyServerBroadcastReceiver,new IntentFilter("LyyServerBroadcastReceiver"));
        startService(new Intent(this,LyyServerService.class));//启动服务
    }

    @Override
    protected void onResume() {
        super.onResume();
        webView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        webView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView.destroy();
        unregisterReceiver(lyyServerBroadcastReceiver);
        stopService(new Intent(this,LyyServerService.class));//停止服务
    }

    private void initView() {
        progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("正在停止服务...");
        toolbar=findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (webView!=null){
                    if (webView.canGoBack()){
                        webView.goBack();
                    }
                }
            }
        });
        webView=findViewById(R.id.webView);
        WebSettings settings = webView.getSettings();
        settings.setDomStorageEnabled(true);
        settings.setAppCachePath(getApplicationContext().getCacheDir().getAbsolutePath());
        settings.setAllowFileAccess(true);
        settings.setAppCacheEnabled(true);
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);//不使用缓存，永远从网络上加载最新的内容
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {//网页加载完成回调
                super.onPageFinished(view, url);
                if (webView.canGoBack()){
                    toolbar.setNavigationIcon(R.drawable.ic_chevron_left);
                }else {
                    toolbar.setNavigationIcon(null);
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }
        });
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {//让WebView支持file input上传文件
                LyyLogUtil.logD("onShowFileChooser()被回调");
                if (mUploadMessage5 != null) {
                    mUploadMessage5.onReceiveValue(null);
                    mUploadMessage5 = null;
                }
                mUploadMessage5 = filePathCallback;
                Intent intent = fileChooserParams.createIntent();
                try {
                    startActivityForResult(intent,
                            FILECHOOSER_RESULTCODE_FOR_ANDROID_5);
                } catch (ActivityNotFoundException e) {
                    mUploadMessage5 = null;
                    return false;
                }
                return true;
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {//改变标题
                super.onReceivedTitle(view, title);
                if (!TextUtils.isEmpty(title)){
                    toolbar.setTitle(title);
                }
            }
        });
        webView.setDownloadListener(new DownloadListener() {//让WebView支持下载（跳转到浏览器下载）
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });
        checkPermission();
        checkStorageManagerPermission();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.index_toolbar,menu);
        this.menu=menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId==R.id.start){//点击启动服务图标
            startService(new Intent(this,LyyServerService.class));//启动服务
        }else if (itemId==R.id.stop){//点击停止服务图标
            stopService(new Intent(this,LyyServerService.class));//停止服务
            progressDialog.show();
        }else if (itemId==R.id.exit){
            LyyDialog lyyDialog = new LyyDialog(this);
            lyyDialog.setContent("您确定要退出程序吗？");
            lyyDialog.setOnClickListener(new LyyDialog.OnClickListener() {
                @Override
                public void onCancel(Dialog lyyDialog) {
                    lyyDialog.dismiss();
                }

                @Override
                public void onConfirm(Dialog lyyDialog) {
                    lyyDialog.dismiss();
                    LyyActivityUtil.getInstance().exitApp();
                }
            });
            lyyDialog.show();
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LyyLogUtil.logD("onActivityResult()被回调");
        if (requestCode == FILECHOOSER_RESULTCODE_FOR_ANDROID_5) {
            if (null == mUploadMessage5) {
                return;
            }
            mUploadMessage5.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, data));
            mUploadMessage5 = null;
        }
    }

    private void checkPermission(){
        int i = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (i!= PackageManager.PERMISSION_GRANTED){//没有授予读写权限
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_WRITE);
        }else {
            makeDir();
        }
    }

    private void makeDir() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){//已挂载、可读写
            File mRootDir=Environment.getExternalStorageDirectory();
            File file = new File(mRootDir, File.separator+"CowcatServer");
            if (!file.exists()){
                boolean mkdirs = file.mkdirs();
                LyyLogUtil.logD("目录是否创建成功："+mkdirs);
            }else {
                LyyLogUtil.logD("目录已经存在。");
            }
        }
    }

    private void checkStorageManagerPermission(){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R || Environment.isExternalStorageManager()){
            LyyLogUtil.logD("已获得访问所有文件权限");
        }else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("本程序需要您同意允许访问所有文件权限");
            builder.setCancelable(false);
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent =new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                    startActivity(intent);
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(MainActivity.this, "取消授权将无法正常使用文件上传、下载等功能。", Toast.LENGTH_LONG).show();
                }
            });
            builder.show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==REQUEST_WRITE){
            if (grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                makeDir();
            }else {
                Toast.makeText(this, "您没有授予读写文件的权限，将无法正常使用本软件。", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()){
            webView.goBack();//让WebView支持返回上个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);//退出整个应用程序

    }
}