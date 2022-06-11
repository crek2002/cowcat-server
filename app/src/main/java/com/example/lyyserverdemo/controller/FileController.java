package com.example.lyyserverdemo.controller;


import androidx.annotation.Nullable;

import com.example.lyyserverdemo.domain.LyyFile;
import com.example.lyyserverdemo.domain.ReturnData;
import com.example.lyyserverdemo.utils.ErrCodeUtil;
import com.example.lyyserverdemo.utils.FileUtils;
import com.example.lyyserverdemo.utils.LyyLogUtil;
import com.yanzhenjie.andserver.annotation.GetMapping;
import com.yanzhenjie.andserver.annotation.PostMapping;
import com.yanzhenjie.andserver.annotation.RequestParam;
import com.yanzhenjie.andserver.annotation.RestController;
import com.yanzhenjie.andserver.framework.body.FileBody;
import com.yanzhenjie.andserver.http.HttpRequest;
import com.yanzhenjie.andserver.http.HttpResponse;
import com.yanzhenjie.andserver.http.ResponseBody;
import com.yanzhenjie.andserver.http.multipart.MultipartFile;
import com.yanzhenjie.andserver.util.MediaType;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * @author Yingyong Lao
 * 创建时间 2022/5/15 19:18
 * @version 1.0
 */
@RestController
public class FileController {
    /**
     * 上传文件
     * @param request
     * @param response
     * @param file
     * @return
     */
    @Deprecated
    @PostMapping(path = "/uploadFile")
    String uploadFile(HttpRequest request, HttpResponse response, @RequestParam(name = "fileName") MultipartFile file,@RequestParam(name = "userName") String userName) {
        try {
            File uploadFile = FileUtils.createUploadFile(file);
            LyyLogUtil.logD("用户上传的文件的保存路径："+uploadFile.getAbsolutePath());
            file.transferTo(uploadFile);
        } catch (Exception e) {
            e.printStackTrace();
            LyyLogUtil.logE("服务器接收文件出现异常！"+e.getMessage());
        }
        return "文件上传成功！";
    }


    /**
     * 获取文件列表
     * @param request
     * @param response
     * @return
     */
    @GetMapping(path = "/getFileList", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ReturnData getFileList(HttpRequest request, HttpResponse response){
        ReturnData returnData=new ReturnData();
        List<LyyFile> list=new ArrayList<>();
        File file=new File(FileUtils.filePath);
        File[] files = file.listFiles();
        if (files!=null&&files.length>0){
            Arrays.sort(files, new Comparator<File>() {//按文件的修改日期递减排序
                @Override
                public int compare(File f1, File f2) {
                    long diff = f1.lastModified() - f2.lastModified();
                    if (diff > 0)
                        return -1;
                    else if (diff == 0)
                        return 0;
                    else
                        return 1;
                }

                @Override
                public boolean equals(@Nullable Object obj) {
                    return true;
                }
            });
              for (File f : files) {
                LyyFile lyyFile = new LyyFile();
                lyyFile.setFileName(f.getName());
                lyyFile.setFileSize(""+f.length());
                lyyFile.setDir(f.isDirectory());
                lyyFile.setModifiedDate(""+f.lastModified());
                lyyFile.setFilePath(f.getAbsolutePath());
                list.add(lyyFile);
            }
            returnData.setSuccess(true);
        }else {
            returnData.setSuccess(false);
            returnData.setErrorCode(512);
            returnData.setErrorMsg(ErrCodeUtil.map.get(512));
        }

        returnData.setData(list);
        return returnData;
    }

    /**
     * 下载文件
     * @param response
     * @param fileName
     * @return
     */
    @GetMapping(path = "/downloadFile")
    public ResponseBody downloadFile(HttpResponse response, @RequestParam(name = "fileName") String fileName){
        File file=new File(FileUtils.filePath+File.separator+fileName);
        FileBody body=new FileBody(file);
        response.setHeader("Content-Disposition", "attachment;filename="+fileName);
        return body;
    }

    /**
     * 根据文件名删除文件
     * @param fileName 文件名
     * @return
     */
    @PostMapping(path = "/deleteFile")
    public ReturnData deleteFile(@RequestParam(name = "fileName") String fileName){
        ReturnData returnData=new ReturnData();
        File file=new File(FileUtils.filePath+File.separator+fileName);
        boolean flag = file.delete();
        returnData.setSuccess(flag);
        return returnData;
    }
}
