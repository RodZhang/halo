package com.rod.halo.refersh.signal

/**
 *
 * @author Rod
 * @date 2019/4/29
 */
abstract class Condition<S : Signal>(private val signalCenter: SignalCenter) {

    abstract fun execute(signalCenter: SignalCenter, signal: S)
}