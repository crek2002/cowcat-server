package com.example.lyyserverdemo.domain;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.util.Date;

/**
 * @author Yingyong Lao
 * 创建时间 2022/5/18 21:32
 * @version 1.0
 */
public class LyyFile extends LitePalSupport {
    @Column(unique = true,nullable = false)
    private Integer id;

    private String fileName;//文件名

    private String fileSize;//文件的大小，单位字节

    @Column(nullable = false)
    private Date uploadDate;//上传时间

    private String modifiedDate;//修改时间

    private Boolean isDir;//是否是目录

    @Column(unique = true,nullable = false)
    private String filePath;

    private LyyUser lyyUser;


    public LyyFile() {
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public Boolean getDir() {
        return isDir;
    }

    public void setDir(Boolean dir) {
        isDir = dir;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public LyyUser getLyyUser() {
        return lyyUser;
    }

    public void setLyyUser(LyyUser lyyUser) {
        this.lyyUser = lyyUser;
    }

    @Override
    public String toString() {
        return "LyyFile{" +
                "id=" + id +
                ", fileName='" + fileName + '\'' +
                ", fileSize='" + fileSize + '\'' +
                ", uploadDate=" + uploadDate +
                ", modifiedDate='" + modifiedDate + '\'' +
                ", isDir=" + isDir +
                ", filePath='" + filePath + '\'' +
                ", lyyUser=" + lyyUser +
                '}';
    }
}
