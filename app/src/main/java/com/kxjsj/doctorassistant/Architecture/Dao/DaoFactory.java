package com.kxjsj.doctorassistant.Architecture.Dao;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;

import com.kxjsj.doctorassistant.App;
import com.kxjsj.doctorassistant.JavaBean.KotlinBean;

/**
 * Created by vange on 2017/11/8.
 */

@Database(entities = {KotlinBean.PushBean.class}, version = 1)
public abstract class DaoFactory extends RoomDatabase {
    static DaoFactory factory;
    public abstract PushDao getPushDao();


    public static DaoFactory getFactory(App app){
        if(factory==null){
            synchronized (DaoFactory.class){
                if(factory==null){
                    factory= Room.databaseBuilder(app,DaoFactory.class,"dataBase").build();
                }
            }
        }
        return factory;
    }
}


