package com.example.lyyserverdemo.controller;

import android.content.ContentValues;
import android.text.TextUtils;

import com.example.lyyserverdemo.component.LoginIterceptor;
import com.example.lyyserverdemo.domain.LyyUser;
import com.example.lyyserverdemo.domain.ReturnData;
import com.example.lyyserverdemo.utils.Des3Util;
import com.example.lyyserverdemo.utils.LyyLogUtil;
import com.yanzhenjie.andserver.annotation.PostMapping;
import com.yanzhenjie.andserver.annotation.RequestParam;
import com.yanzhenjie.andserver.annotation.RestController;
import com.yanzhenjie.andserver.http.HttpRequest;
import com.yanzhenjie.andserver.http.HttpResponse;
import com.yanzhenjie.andserver.http.cookie.Cookie;
import com.yanzhenjie.andserver.http.session.Session;

import org.litepal.LitePal;

import java.util.List;

/**
 * @author Yingyong Lao
 * 创建时间 2022/5/14 15:18
 * @version 1.0
 */
@RestController
public class UserController extends BaseController{

    /**
     * 登录
     * @param request
     * @param response
     * @param userName
     * @param password
     * @return
     */
    @PostMapping("/login")
    ReturnData login(HttpRequest request, HttpResponse response,@RequestParam(name = "userName") String userName, @RequestParam(name = "password")String password) {
        ReturnData returnData = new ReturnData();
        List<LyyUser> lyyUsers = LitePal.where("userName=? and password =?", userName, Des3Util.encrypt(Des3Util.key,password)).find(LyyUser.class);
        if (lyyUsers.size()==1){
            LyyUser user = lyyUsers.get(0);
            Session session = request.getValidSession();
            session.setAttribute(LoginIterceptor.LOGIN_ATTRIBUTE,true);
            Cookie cookie=new Cookie("userName",userName+"="+password);
            response.addCookie(cookie);
            returnData.setSuccess(true);
            returnData.setData(user);
        }else {
            return getFailData(510);
        }
        return returnData;

    }

    /**
     * 注册
     * @param request
     * @param response
     * @param userName
     * @param password
     * @return
     */
    @PostMapping("/register")
    ReturnData register(HttpRequest request, HttpResponse response, @RequestParam(name = "userName") String userName, @RequestParam(name = "password")String password){
        LyyUser lyyUser=new LyyUser();
        lyyUser.setUserName(userName);
        lyyUser.setPassword(Des3Util.encrypt(Des3Util.key,password));
        boolean save = lyyUser.save();
        ReturnData returnData = new ReturnData();

        LyyLogUtil.logD("是否保存成功："+save);
        if (save){
           returnData.setSuccess(true);
           returnData.setData(lyyUser);
        }else {
            return getFailData(511);
        }
        return returnData;
    }

    /**
     * 获取用户信息
     * @param userName
     * @param password
     * @return
     */
    @PostMapping(path = "/getUserInfo")
    public ReturnData getUserInfo(@RequestParam(name = "userName") String userName,@RequestParam(name = "password")String password){
        List<LyyUser> lyyUsers = LitePal.where("userName=? and password=?", userName, password).find(LyyUser.class);
        if (lyyUsers.size()<1){
            return getFailData(517);
        }else {
            ReturnData returnData = new ReturnData();
            returnData.setSuccess(true);
            returnData.setData(lyyUsers.get(0));
            return returnData;
        }
    }

    /**
     * 修改用户信息
     * @param userId
     * @param userName
     * @param trueName
     * @param gender
     * @param age
     * @param email
     * @param address
     * @return
     */
    @PostMapping(path = "/updateUserInfo")
    public ReturnData updateUserInfo(@RequestParam(name = "userId") Integer userId,
                                     @RequestParam(name = "userName",required = false,defaultValue = "") String userName,
                                     @RequestParam(name = "trueName",required = false,defaultValue = "") String trueName,
                                     @RequestParam(name = "gender",required = false,defaultValue = "") String gender,
                                     @RequestParam(name = "age",required = false,defaultValue = "") String age,
                                     @RequestParam(name = "email",required = false,defaultValue = "") String email,
                                     @RequestParam(name = "address",required = false,defaultValue = "") String address){

        ContentValues contentValues=new ContentValues();
        if (!TextUtils.isEmpty(trueName)){
            contentValues.put("trueName",trueName);
        }
        if (!TextUtils.isEmpty(gender)){
            contentValues.put("gender",gender);
        }
        if (!TextUtils.isEmpty(age)){
            contentValues.put("age",age);
        }
        if (!TextUtils.isEmpty(email)){
            contentValues.put("email",email);
        }
        if (!TextUtils.isEmpty(address)){
            contentValues.put("address",address);
        }

        int update = LitePal.update(LyyUser.class,contentValues , userId);
        if (update==1){
            return getSucessData();
        }else {
            return getFailData(518);
        }
    }

    /**
     * 修改密码
     * @param userId
     * @param newPassword
     * @return
     */
    @PostMapping(path = "/updatePassword")
    public ReturnData updatePassword(HttpRequest request,@RequestParam(name = "userId") Integer userId,
                                     @RequestParam(name = "newPassword") String newPassword,
                                     @RequestParam(name = "oldPassword")String oldPassword){
        List<LyyUser> lyyUsers = LitePal.where("id=? and password=?", userId + "", Des3Util.encrypt(Des3Util.key,oldPassword)).find(LyyUser.class);
        if (lyyUsers.size()<1){
            return getFailData(520);
        }
        LyyUser lyyUser = new LyyUser();
        lyyUser.setPassword(Des3Util.encrypt(Des3Util.key,newPassword));
        int update = lyyUser.update(userId);
        if (1==update){
            Session session = request.getValidSession();
            session.removeAttribute(LoginIterceptor.LOGIN_ATTRIBUTE);
            return getSucessData();
        }else {
            return getFailData(519);
        }
    }
}
