package com.rod.halo

import com.rod.halo.mulittask.MulitTask
import com.rod.halo.mulittask.TaskItem
import org.junit.Test
import java.util.concurrent.Executors

/**
 *
 * @author Rod
 * @date 2019/3/5
 */
class MulitTaskTest {

    companion object {
        val EXECUTOR = Executors.newCachedThreadPool()
        fun getNameTask(): TaskItem<NameInfo, MyResp> {
            return object : TaskItem<NameInfo, MyResp>() {
                override fun doTask() {
                    getNameFunction(this).execute()
                }

                override fun transform(input: NameInfo?, out: MyResp) {
                    input?.name = "RodZhang"
                    out.mName = input
                }

            }
        }

        fun getAgeTask(): TaskItem<AgeInfo, MyResp> {
            return object : TaskItem<AgeInfo, MyResp>() {
                override fun doTask() {
                    getAgeFunction(this).execute()
                }

                override fun transform(input: AgeInfo?, out: MyResp) {
                    input?.age = 28
                    out.mAge = input
                }

            }
        }

        private fun getNameFunction(task: TaskItem<NameInfo, MyResp>): FakeHttpFunction<NameInfo> {
            return object : FakeHttpFunction<NameInfo>(object : FunctionCallback<NameInfo> {
                override fun onResponse(rsp: NameInfo) {
                    task.taskEnd(rsp)
                }

                override fun onError() {
                    task.taskEnd(null)
                }

            }) {
                override fun productRsp(): NameInfo {
                    return NameInfo()
                }

            }
        }

        private fun getAgeFunction(task: TaskItem<AgeInfo, MyResp>): FakeHttpFunction<AgeInfo> {
            return object : FakeHttpFunction<AgeInfo>(object : FunctionCallback<AgeInfo> {
                override fun onResponse(rsp: AgeInfo) {
                    task.taskEnd(rsp)
                }

                override fun onError() {
                    task.taskEnd(null)
                }

            }) {
                override fun productRsp(): AgeInfo {
                    return AgeInfo()
                }

            }
        }
    }

    @Test
    fun testMulitTask() = MulitTask(MyResp())
            .addTask(getNameTask())
            .addTask(getAgeTask())
            .start {
                println(it.toString())
            }

    abstract class FakeHttpFunction<R>(private val callback: FunctionCallback<R>) {

        fun execute() {
            val r = productRsp()
            EXECUTOR.execute {
                Thread.sleep(8000)
                callback.onResponse(r)
            }
        }

        abstract fun productRsp(): R
    }

    interface FunctionCallback<Rsp> {
        fun onResponse(rsp: Rsp)

        fun onError()
    }

    class MyResp {
        var mName: NameInfo? = null
        var mAge: AgeInfo? = null

        override fun toString(): String {
            return "MyResp(mName=$mName, mAge=$mAge)"
        }


    }

    abstract class CommonRsp {
        val mRetCode: Int = 0
        var msg: String = ""
    }

    data class NameInfo(var name: String? = null)
    data class AgeInfo(var age: Int? = null)
}