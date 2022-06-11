package com.example.lyyserverdemo.domain;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Yingyong Lao
 * 创建时间 2022/5/14 15:20
 * @version 1.0
 */
public class LyyUser extends LitePalSupport {
    @Column(unique = true)
    private Integer id;

    @Column(unique = true)
    private String userName;

    private String password;

    private String trueName;

    private String gender;

    private Integer age;

    @Column(unique = true)
    private String email;

    private String address;

    private Date registerTime;//注册时间

    private List<LyyFile> fileList=new ArrayList<>();
    private List<Note> noteList=new ArrayList<>();
    private List<SecurityQuestion> questionList=new ArrayList<>();

    public LyyUser() {
    }

    public LyyUser(Integer id, String userName, String password) {
        this.id = id;
        this.userName = userName;
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTrueName() {
        return trueName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<LyyFile> getFileList() {
        return fileList;
    }

    public void setFileList(List<LyyFile> fileList) {
        this.fileList = fileList;
    }

    public List<Note> getNoteList() {
        return noteList;
    }

    public void setNoteList(List<Note> noteList) {
        this.noteList = noteList;
    }

    public Date getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }

    public List<SecurityQuestion> getQuestionList() {
        return questionList;
    }

    public void setQuestionList(List<SecurityQuestion> questionList) {
        this.questionList = questionList;
    }

    @Override
    public String toString() {
        return "LyyUser{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", trueName='" + trueName + '\'' +
                ", gender='" + gender + '\'' +
                ", age=" + age +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
