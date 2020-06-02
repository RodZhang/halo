package com.rod.api;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import java.lang.reflect.Constructor;

/**
 * @author Rod
 * @date 2019-06-24
 */
public class Halo {

    private Halo() {
    }

    public static <A extends Activity> void inject(A activity) {
        Log.d("Halo", "inject");
        try {
            Class<?> injector = Class.forName(activity.getClass().getName() + "_ViewBinding");
            Constructor<?> inject = injector.getConstructor(activity.getClass());
            inject.newInstance(activity);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Halo", "", e);
        }
    }

    public static void inject(Object target, View view) {
        try {
            Class<?> injector = Class.forName(target.getClass().getName() + "_ViewBinding");
            Constructor<?> inject = injector.getConstructor(target.getClass(), View.class);
            inject.newInstance(target, view);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Halo", "", e);
        }
    }
}
