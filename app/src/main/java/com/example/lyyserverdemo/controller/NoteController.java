package com.example.lyyserverdemo.controller;

import android.text.TextUtils;

import com.example.lyyserverdemo.domain.LyyUser;
import com.example.lyyserverdemo.domain.Note;
import com.example.lyyserverdemo.domain.ReturnData;
import com.yanzhenjie.andserver.annotation.GetMapping;
import com.yanzhenjie.andserver.annotation.PostMapping;
import com.yanzhenjie.andserver.annotation.RequestParam;
import com.yanzhenjie.andserver.annotation.RestController;

import org.litepal.LitePal;

import java.util.Date;
import java.util.List;

/**
 * @author Yingyong Lao
 * 创建时间 2022/5/23 23:21
 * @version 1.0
 */
@RestController
public class NoteController extends BaseController{

    @PostMapping(path = "/addNote")
    public ReturnData addNote(@RequestParam(name = "userName",required = false,defaultValue = "") String userName,
                              @RequestParam(name = "title",required = false,defaultValue = "")String title,
                              @RequestParam(name = "content",required = false,defaultValue = "")String content){
        Note note=new Note();
        note.setTitle(title);
        note.setContent(content);
        note.setCreatedTime(new Date());
        note.setUser(getCurrUser(userName));
        boolean flag = note.save();
        if (flag){
            return getSucessData();
        }else {
            return getFailData(513);
        }
    }

    @GetMapping(path = "/getNoteList")
    public ReturnData getNoteList(@RequestParam(name = "userName") String userName,@RequestParam(name = "password") String password){
        List<LyyUser> lyyUsers = LitePal.where("userName=? and password=?", userName, password).find(LyyUser.class);
        ReturnData returnData=new ReturnData();
        if (lyyUsers.size()<1){
            return getFailData(514);
        }
        Integer id = lyyUsers.get(0).getId();
        List<Note> notes = LitePal.where("lyyuser_id=?", "" + id).order("createdTime desc").find(Note.class);//倒序排序
        returnData.setData(notes);
        return returnData;
    }

    @PostMapping(path = "/deleteNote")
    public ReturnData deleteNote(@RequestParam(name = "id")int id){
        int delete = LitePal.delete(Note.class, id);
        if (delete==1){
            return getSucessData();
        }else {
            return getFailData(515);
        }
    }

    @PostMapping(path = "/updateNote")
    public ReturnData updateNote(@RequestParam(name = "id") int id,@RequestParam(name = "title") String title,@RequestParam(name = "content") String content){
        Note note = new Note();
        note.setTitle(title);
        note.setContent(content);
        note.setUpdatedTime(new Date());
        int update = note.update(id);
        if (1==update){
            return getSucessData();
        }else {
            return getFailData(516);
        }
    }

    @GetMapping(path = "/searchNote")
    public ReturnData searchNote(@RequestParam(name = "keyword",required = false,defaultValue = "") String keyword){
        ReturnData returnData=new ReturnData();
        returnData.setSuccess(true);
        if (TextUtils.isEmpty(keyword)){
            List<Note> all = LitePal.order("createdTime desc").find(Note.class);
            returnData.setData(all);
        }else {
            List<Note> notes = LitePal.where("title like ? or content like ?", "%"+keyword+"%", "%"+keyword+"%").find(Note.class);
            returnData.setData(notes);
        }
        return returnData;
    }

}
