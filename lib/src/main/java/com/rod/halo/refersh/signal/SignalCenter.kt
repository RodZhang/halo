package com.rod.halo.refersh.signal

/**
 *
 * @author Rod
 * @date 2019/4/29
 */
class SignalCenter {

    private val mConditionMap = HashMap<Class<*>, Condition<Signal>>()

    fun <S : Signal> put(cls: Class<S>, condition: Condition<S>) {
        mConditionMap[cls] = condition as Condition<Signal>
    }

    fun <S : Signal> post(signal: S) {
        mConditionMap[signal::class.java]?.execute(this, signal)
    }
}