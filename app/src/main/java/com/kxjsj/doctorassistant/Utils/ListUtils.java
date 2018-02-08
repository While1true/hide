package com.kxjsj.doctorassistant.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vange on 2018/1/10.
 */

public class ListUtils {

    public static List<String> provideList(int size){
        List<String> strings=new ArrayList();
        for (int i = 0; i < size; i++) {
            strings.add("这是 item "+i);
        }

        return strings;
    }
}
