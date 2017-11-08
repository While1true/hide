package com.kxjsj.doctorassistant.Architecture.ViewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.kxjsj.doctorassistant.App;
import com.kxjsj.doctorassistant.Architecture.Dao.DaoFactory;
import com.kxjsj.doctorassistant.Architecture.Dao.PushDao;
import com.kxjsj.doctorassistant.JavaBean.KotlinBean;
import com.kxjsj.doctorassistant.Rx.DataObserver;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by vange on 2017/11/8.
 */

public class PushViewModel extends ViewModel {
    private MutableLiveData<List<KotlinBean.PushBean>> datas;

    public LiveData<List<KotlinBean.PushBean>> getUsers() {
        if (datas == null) {
            datas = new MutableLiveData<>();
            loadDatas();
        }
        return datas;
    }

    private void loadDatas() {
        PushDao pushDao = DaoFactory.getFactory(App.app).getPushDao();
        datas= (MutableLiveData<List<KotlinBean.PushBean>>) pushDao.getAll();
    }

}
