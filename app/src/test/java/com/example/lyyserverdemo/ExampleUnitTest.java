package com.example.lyyserverdemo;

import com.example.lyyserverdemo.utils.Des3Util;

import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        //assertEquals(4, 2 + 2);
        String s = Des3Util.encrypt(Des3Util.key, "小肥咪");
        System.out.println(s);
        System.out.println(Des3Util.decrypt(Des3Util.key,s));
    }
}