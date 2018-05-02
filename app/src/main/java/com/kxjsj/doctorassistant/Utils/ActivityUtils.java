package com.kxjsj.doctorassistant.Utils;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vange on 2017/10/19.
 */

public class ActivityUtils {
    List<Activity> activities;

    private ActivityUtils() {
        activities = new ArrayList<>(5);
    }

    public void put(Activity activity) {
        if (!activities.contains(activity)) {
            activities.add(activity);
        }
    }

    public void remove(Activity activity) {
        if (activities.contains(activity)) {
            activities.remove(activity);
        }

    }
    public Activity getTopActivity(){
        return activities.get(activities.size()-1);
    }

    public void removeTop() {
        if (activities.size() > 0) {
            Activity activity = activities.get(activities.size() - 1);
            if (activity != null)
                activity.finish();
            activities.remove(activities.size() - 1);
        }
    }

    public void removeAll() {
        for (Activity activity : activities) {
            if (activity != null) {
                activity.finish();
            }
        }
        activities.clear();
    }

    public static ActivityUtils getInstance(){
        return Instance.activityUtils;
    }


    private static class Instance {
        private static ActivityUtils activityUtils = new ActivityUtils();
    }
}
