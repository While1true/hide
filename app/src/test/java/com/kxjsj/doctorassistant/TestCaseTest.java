package com.kxjsj.doctorassistant;

import android.util.Log;

import com.kxjsj.doctorassistant.Constant.Constance;
import com.kxjsj.doctorassistant.JavaBean.KotlinBean;
import com.kxjsj.doctorassistant.Net.Api;
import com.kxjsj.doctorassistant.Net.ApiController;
import com.kxjsj.doctorassistant.Net.RetrofitHttpManger;
import com.kxjsj.doctorassistant.Rx.MyObserver;
import com.kxjsj.doctorassistant.Rx.RxSchedulers;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by vange on 2017/10/16.
 */
public class TestCaseTest {
    @Before
    public void setUp() throws Exception {

    }
    @Test
    public void getData(){
        String token = App.getUserInfo().getToken();
        System.out.println(token);
    }
    @After
    public void tearDown() throws Exception {

    }

}