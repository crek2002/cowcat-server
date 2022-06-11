package com.example.lyyserverdemo.domain;

import org.litepal.crud.LitePalSupport;

import java.util.Date;

/**
 * @author Yingyong Lao
 * 创建时间 2022/5/23 23:16
 * @version 1.0
 */
public class Note extends LitePalSupport {
    private Integer id;
    private String title;//标题
    private String content;//正文
    private Date createdTime;//创建时间
    private Date updatedTime;//修改时间
    private LyyUser user;

    public Note() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public LyyUser getUser() {
        return user;
    }

    public void setUser(LyyUser user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", createdTime=" + createdTime +
                ", updatedTime=" + updatedTime +
                ", user=" + user +
                '}';
    }
}
