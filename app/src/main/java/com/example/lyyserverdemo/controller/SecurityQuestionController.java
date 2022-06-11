package com.example.lyyserverdemo.controller;

import com.example.lyyserverdemo.domain.LyyUser;
import com.example.lyyserverdemo.domain.ReturnData;
import com.example.lyyserverdemo.domain.SecurityQuestion;
import com.example.lyyserverdemo.utils.Des3Util;
import com.yanzhenjie.andserver.annotation.GetMapping;
import com.yanzhenjie.andserver.annotation.PostMapping;
import com.yanzhenjie.andserver.annotation.RequestParam;
import com.yanzhenjie.andserver.annotation.RestController;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Yingyong Lao
 * 创建时间 2022/5/30 23:42
 * @version 1.0
 */
@RestController
public class SecurityQuestionController extends BaseController {
    @PostMapping(path = "/addSecurityQuestions")
    public ReturnData addSecurityQuestions(@RequestParam(name = "userName") String userName,
                                           @RequestParam(name = "question1") String question1,
                                           @RequestParam(name = "question2") String question2,
                                           @RequestParam(name = "question3") String question3,
                                           @RequestParam(name = "answer1") String answer1,
                                           @RequestParam(name = "answer2") String answer2,
                                           @RequestParam(name = "answer3") String answer3) {
        int count = LitePal.where("lyyuser_id=?", "" + getCurrUser(userName).getId()).count(SecurityQuestion.class);
        if (count == 3) {
            return getFailData(522);
        }
        Date date = new Date();
        LyyUser currUser = getCurrUser(userName);
        SecurityQuestion securityQuestion1 = new SecurityQuestion(date, question1, answer1, currUser);
        SecurityQuestion securityQuestion2 = new SecurityQuestion(date, question2, answer2, currUser);
        SecurityQuestion securityQuestion3 = new SecurityQuestion(date, question3, answer3, currUser);

        LitePal.beginTransaction();//开启事务
        boolean save1 = securityQuestion1.save();
        boolean save2 = securityQuestion2.save();
        boolean save3 = securityQuestion3.save();
        if (save1 && save2 && save3) {
            LitePal.setTransactionSuccessful();
        }
        LitePal.endTransaction();//结束事务
        if (save1 && save2 && save3) {
            return getSucessData();
        } else {
            return getFailData(521);
        }
    }

    @GetMapping(path = "/getSecurityQuestion")
    public ReturnData getSecurityQuestion(@RequestParam(name = "userName") String userName) {
        List<SecurityQuestion> securityQuestions = LitePal.where("lyyuser_id=?", getCurrUser(userName).getId() + "").find(SecurityQuestion.class);
        ReturnData returnData = new ReturnData();
        returnData.setData(securityQuestions);
        returnData.setSuccess(true);
        return returnData;
    }

    @PostMapping(path = "/updateSecurityQuestion")
    public ReturnData updateSecurityQuestion(
            @RequestParam(name = "userName") String userName,
            @RequestParam(name = "question1") String question1,
            @RequestParam(name = "question2") String question2,
            @RequestParam(name = "question3") String question3,
            @RequestParam(name = "answer1") String answer1,
            @RequestParam(name = "answer2") String answer2,
            @RequestParam(name = "answer3") String answer3
    ) {
        List<SecurityQuestion> securityQuestions = LitePal.where("lyyuser_id=?", getCurrUser(userName).getId() + "").find(SecurityQuestion.class);
        if (securityQuestions.size()!=3){
            return getFailData(523);
        }
        Date date = new Date();
        SecurityQuestion securityQuestion1 = securityQuestions.get(0);
        SecurityQuestion securityQuestion2 = securityQuestions.get(1);
        SecurityQuestion securityQuestion3 = securityQuestions.get(2);
        securityQuestion1.setQuestion(question1);
        securityQuestion1.setAnswer(answer1);
        securityQuestion1.setUpdatedTime(date);

        securityQuestion2.setQuestion(question2);
        securityQuestion2.setAnswer(answer2);
        securityQuestion2.setUpdatedTime(date);

        securityQuestion3.setQuestion(question3);
        securityQuestion3.setAnswer(answer3);
        securityQuestion3.setUpdatedTime(date);

        LitePal.beginTransaction();

        int update1 = securityQuestion1.update(securityQuestion1.getId());
        int update2 = securityQuestion2.update(securityQuestion2.getId());
        int update3 = securityQuestion3.update(securityQuestion3.getId());

        if (update1==1&&update2==1&&update3==1){
            LitePal.setTransactionSuccessful();
        }

        LitePal.endTransaction();
        if (update1==1&&update2==1&&update3==1){
            List<SecurityQuestion> questionList = LitePal.where("lyyuser_id=?", getCurrUser(userName).getId() + "").find(SecurityQuestion.class);
            return getSucessData(questionList);
        }else {
            return getFailData(524);
        }
    }

    @GetMapping(path = "/getQuestions")
    public ReturnData getQuestions(@RequestParam(name = "emailOrUsername") String emailOrUsername){
        LyyUser user = getUserByEmailOrUsername(emailOrUsername);
        if (user==null){
            return getFailData(525);
        }

        List<SecurityQuestion> questionList = LitePal.where("lyyuser_id=?", user.getId() + "").find(SecurityQuestion.class);
        if (questionList.size()==0){
            return getFailData(526);
        }
        if (questionList.size()==3){
            List<String> stringList=new ArrayList<>();
            for (SecurityQuestion securityQuestion : questionList) {
                stringList.add(securityQuestion.getQuestion());
            }
            return getSucessData(stringList);
        }else {
            return getFailData(527);
        }
    }

    @GetMapping(path = "/verifyQuestions")
    public ReturnData verifyQuestions(@RequestParam(name = "emailOrUsername") String emailOrUsername,
                                      @RequestParam(name = "answer1")String answer1,
                                      @RequestParam(name = "answer2")String answer2,
                                      @RequestParam(name = "answer3")String answer3){
        LyyUser user = getUserByEmailOrUsername(emailOrUsername);
        if (user==null){
            return getFailData(525);
        }

        List<SecurityQuestion> questionList = LitePal.where("lyyuser_id=?", user.getId() + "").find(SecurityQuestion.class);
        if (questionList.size()==0){
            return getFailData(526);
        }
        if (questionList.size()==3){
            //如果三个密保问题都回答正确
            if (answer1.equals(questionList.get(0).getAnswer())
                    &&answer2.equals(questionList.get(1).getAnswer())
                    &&answer3.equals(questionList.get(2).getAnswer())){
                return getSucessData();
            }else {
                return getFailData(528);
            }
        }else {
            return getFailData(527);
        }
    }

    @PostMapping(path = "/resetPassword")
    public ReturnData resetPassword(@RequestParam(name = "emailOrUsername") String emailOrUsername,
                                    @RequestParam(name = "answer1")String answer1,
                                    @RequestParam(name = "answer2")String answer2,
                                    @RequestParam(name = "answer3")String answer3,
                                    @RequestParam(name = "password")String password){
        LyyUser user = getUserByEmailOrUsername(emailOrUsername);
        if (user==null){
            return getFailData(525);
        }

        List<SecurityQuestion> questionList = LitePal.where("lyyuser_id=?", user.getId() + "").find(SecurityQuestion.class);
        if (questionList.size()==0){
            return getFailData(526);
        }
        if (questionList.size()==3){
            //如果三个密保问题都回答正确
            if (answer1.equals(questionList.get(0).getAnswer())
                    &&answer2.equals(questionList.get(1).getAnswer())
                    &&answer3.equals(questionList.get(2).getAnswer())){
                List<LyyUser> list = LitePal.where("id=?", user.getId() + "").find(LyyUser.class);
                if (list.size()!=1){
                    return getFailData(527);
                }else {
                    LyyUser lyyUser = list.get(0);
                    lyyUser.setPassword(Des3Util.encrypt(Des3Util.key,password));
                    int update = lyyUser.update(user.getId());
                    if (update==1){
                        return getSucessData();
                    }else {
                        return getFailData(529);
                    }
                }
            }else {
                return getFailData(528);
            }
        }else {
            return getFailData(527);
        }

    }
}
