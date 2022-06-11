package com.example.lyyserverdemo.controller;

import com.example.lyyserverdemo.domain.LyyUser;
import com.example.lyyserverdemo.domain.ReturnData;
import com.example.lyyserverdemo.utils.ErrCodeUtil;

import org.litepal.LitePal;

import java.util.List;

/**
 * @author Yingyong Lao
 * 创建时间 2022/5/23 23:23
 * @version 1.0
 */
public class BaseController {
    protected ReturnData getSucessData(){
        ReturnData returnData=new ReturnData();
        returnData.setSuccess(true);
        return returnData;
    }

    protected ReturnData getSucessData(Object data){
        ReturnData returnData=new ReturnData();
        returnData.setData(data);
        returnData.setSuccess(true);
        return returnData;
    }

    protected ReturnData getFailData(int errCode){
        ReturnData returnData=new ReturnData();
        returnData.setSuccess(false);
        returnData.setErrorCode(errCode);
        returnData.setErrorMsg(ErrCodeUtil.map.get(errCode));
        return returnData;
    }

    protected LyyUser getCurrUser(String userName){
        List<LyyUser> list = LitePal.where("userName=?", userName).find(LyyUser.class);
        if (list.size()==0){
            return null;
        }else {
            return list.get(0);
        }
    }

    /**
     * 通过邮件或用户名获取用户
     * @param emailOrUsername
     * @return
     */
    protected LyyUser getUserByEmailOrUsername(String emailOrUsername){
        List<LyyUser> list = LitePal.where("email=? or userName=?", emailOrUsername,emailOrUsername).find(LyyUser.class);
        if (list.size()==0){
            return null;
        }else {
            return list.get(0);
        }
    }
}
