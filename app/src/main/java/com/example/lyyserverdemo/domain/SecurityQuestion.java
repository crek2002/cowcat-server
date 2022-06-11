package com.example.lyyserverdemo.domain;

import org.litepal.crud.LitePalSupport;

import java.util.Date;

/**
 * @author Yingyong Lao
 * 创建时间 2022/5/30 23:37
 * @version 1.0
 */
public class SecurityQuestion extends LitePalSupport {
    private Long id;
    private Date createdTime;//新增时间
    private Date updatedTime;//修改时间
    private String question;//问题内容
    private String answer;//密保问题的答案
    private LyyUser user;

    public SecurityQuestion() {
    }


    public SecurityQuestion(Date createdTime, String question, String answer, LyyUser user) {
        this.createdTime = createdTime;
        this.question = question;
        this.answer = answer;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public LyyUser getUser() {
        return user;
    }

    public void setUser(LyyUser user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "SecurityQuestion{" +
                "id=" + id +
                ", createdTime=" + createdTime +
                ", updatedTime=" + updatedTime +
                ", question='" + question + '\'' +
                ", answer='" + answer + '\'' +
                ", user=" + user +
                '}';
    }
}
