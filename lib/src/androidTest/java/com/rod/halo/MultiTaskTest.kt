package com.rod.halo

import android.support.test.runner.AndroidJUnit4
import com.rod.halo.mulittask.MultiTask
import com.rod.halo.mulittask.AbsTaskUnit
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.Executors

/**
 *
 * @author Rod
 * @date 2019/3/5
 */
@RunWith(AndroidJUnit4::class)
class MultiTaskTest {

    companion object {
        val EXECUTOR = Executors.newCachedThreadPool()
        fun getNameTask(): AbsTaskUnit<NameInfo, MyResp> {
            return object : AbsTaskUnit<NameInfo, MyResp>() {
                override fun doTask() {
                    getNameFunction(this).execute()
                }

                override fun transform(input: NameInfo?, out: MyResp) {
                    input?.name = "RodZhang"
                    out.mName = input
                }

            }
        }

        fun getAgeTask(): AbsTaskUnit<AgeInfo, MyResp> {
            return object : AbsTaskUnit<AgeInfo, MyResp>() {
                override fun doTask() {
                    getAgeFunction(this).execute()
                }

                override fun transform(input: AgeInfo?, out: MyResp) {
                    input?.age = 28
                    out.mAge = input
                }

            }
        }

        private fun getNameFunction(absTask: AbsTaskUnit<NameInfo, MyResp>): FakeHttpFunction<NameInfo> {
            return object : FakeHttpFunction<NameInfo>(object : FunctionCallback<NameInfo> {
                override fun onResponse(rsp: NameInfo) {
                    absTask.taskEnd(rsp)
                }

                override fun onError() {
                    absTask.taskEnd(null)
                }

            }) {
                override fun productRsp(): NameInfo {
                    return NameInfo()
                }

            }
        }

        private fun getAgeFunction(absTask: AbsTaskUnit<AgeInfo, MyResp>): FakeHttpFunction<AgeInfo> {
            return object : FakeHttpFunction<AgeInfo>(object : FunctionCallback<AgeInfo> {
                override fun onResponse(rsp: AgeInfo) {
                    absTask.taskEnd(rsp)
                }

                override fun onError() {
                    absTask.taskEnd(null)
                }

            }) {
                override fun productRsp(): AgeInfo {
                    return AgeInfo()
                }

            }
        }
    }

    @Test
    fun testMultiTask() {
        val startTime = System.currentTimeMillis()
        println("current thread=${Thread.currentThread()}")
        MultiTask(MyResp())
                .addTask(getNameTask())
                .addTask(getAgeTask())
                .start {
                    println(it.toString())
                    println("callback thread=${Thread.currentThread()}")
                }
        println("cost ${System.currentTimeMillis() - startTime} ms")
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