package com.rod.api;

import android.app.Activity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Rod
 * @date 2019-06-24
 */
public class Halo {

    private Halo() {
    }

    public static void inject(Activity activity) {
        try {
            Class<?> injector = Class.forName(activity.getClass().getName() + "$ViewInjector");
            Method inject = injector.getDeclaredMethod("inject", activity.getClass());
            inject.invoke(null, activity);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
