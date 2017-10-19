package com.kxjsj.doctorassistant;

import com.kxjsj.doctorassistant.Utils.EncryptUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by vange on 2017/10/16.
 */
public class TestCaseTest {
    @Before
    public void setUp() throws Exception {

    }
    @Test
    public void getData(){
        System.out.println(EncryptUtils.md5("13959012996"));
    }
    @After
    public void tearDown() throws Exception {

    }

}