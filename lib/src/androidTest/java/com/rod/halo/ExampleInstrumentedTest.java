package com.rod.halo;

import android.content.Context;
import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.rod.halo.mulittask.MultiTask;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        assertEquals("com.rod.halo.test", appContext.getPackageName());
    }

    @Test
    public void testMultiTask() throws InterruptedException {
        long startTime = System.currentTimeMillis();
        println("current thread=" + Thread.currentThread());
        new MultiTask<>(new MultiTaskTest.MyResp())
                .addTask(MultiTaskTest.Companion.getNameTask())
                .addTask(MultiTaskTest.Companion.getAgeTask())
                .start(new MultiTask.OnTaskFinishCallback<MultiTaskTest.MyResp>() {
                    @Override
                    public void onTaskFinish(MultiTaskTest.MyResp o) {
                        println(o.toString());
                        println("callback thread=" + Thread.currentThread());
                    }
                });
        println("cost " + (System.currentTimeMillis() - startTime) + " ms");
        Thread.sleep(10000);
    }

    private void println(String msg) {
        Log.d("ExampleInstrumentedTest", msg);
    }
}
