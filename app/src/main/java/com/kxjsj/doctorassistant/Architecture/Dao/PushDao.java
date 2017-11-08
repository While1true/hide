package com.kxjsj.doctorassistant.Architecture.Dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.kxjsj.doctorassistant.JavaBean.KotlinBean;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by vange on 2017/11/8.
 */

@Dao
public interface PushDao {
    @Query("select * from pushbean")
    LiveData<List<KotlinBean.PushBean>> getAll();

    @Insert
    void inset(KotlinBean.PushBean... bean);
}
