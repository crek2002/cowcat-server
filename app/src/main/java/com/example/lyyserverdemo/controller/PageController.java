package com.example.lyyserverdemo.controller;

import com.yanzhenjie.andserver.annotation.Controller;
import com.yanzhenjie.andserver.annotation.GetMapping;

/**
 * @author Yingyong Lao
 * 创建时间 2022/5/14 12:57
 * @version 1.0
 */
@Controller
public class PageController{
    @GetMapping(path = "/")
    public String index(){
        return "forward:/index.html";
    }
}
