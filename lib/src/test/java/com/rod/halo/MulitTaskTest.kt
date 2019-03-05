package com.rod.halo

import com.rod.halo.mulittask.MulitTask
import com.rod.halo.mulittask.TaskItem
import org.junit.Test

/**
 *
 * @author Rod
 * @date 2019/3/5
 */
class MulitTaskTest {

    @Test
    fun testMulitTask() {
        MulitTask<MyResp>(MyResp())
                .addTask(object : TaskItem<String, MyResp>() {
                    override fun doTask() {

                    }

                    override fun transform(out: MyResp) {
                    }

                })
                .addTask(object : TaskItem<Int, MyResp>() {
                    override fun doTask() {

                    }

                    override fun transform(out: MyResp) {
                    }

                })
                .subscribe {

                }
    }

    class MyResp {

    }
}