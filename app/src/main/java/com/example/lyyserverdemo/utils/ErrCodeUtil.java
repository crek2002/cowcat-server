package com.example.lyyserverdemo.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Yingyong Lao
 * 创建时间 2022/5/15 11:03
 * @version 1.0
 */
public class ErrCodeUtil {
    public static final Map<Integer,String> map=new HashMap<>();
    static {
        map.put(510,"登录失败，用户名或密码错误！");
        map.put(511,"注册失败，用户名已经存在！");
        map.put(512,"服务器没有保存有任何文件！");
        map.put(513,"添加记事失败！");
        map.put(514,"非法请求，如有疑问，请联系管理员。");
        map.put(515,"删除记事失败。");
        map.put(516,"修改记事失败。");
        map.put(517,"查询不到相关用户信息。");
        map.put(518,"更新用户信息失败。");
        map.put(519,"修改密码失败。");
        map.put(520,"旧密码输入有误。");
        map.put(521,"密保问题设置失败。");
        map.put(522,"您已经存在密保问题，无法再新增。");
        map.put(523,"暂时无法处理您的请求，如有疑问，请联系管理员。");
        map.put(524,"修改密保问题失败。");
        map.put(525,"根据您输入的用户名或邮箱未查询到相关用户。");
        map.put(526,"您尚未设置有密保问题，无法找回密码。");
        map.put(527,"系统错误，如有疑问，请联系管理员。");
        map.put(528,"密保问题没有完全答对，无法找回密码。");
        map.put(529,"重置密码失败。");
    }
}
