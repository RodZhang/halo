package com.rod.halo.simple.refresh.condition

import com.rod.halo.refersh.signal.Condition
import com.rod.halo.refersh.signal.SignalCenter
import com.rod.halo.simple.refresh.signal.EnterPage

/**
 *
 * @author Rod
 * @date 2019/4/29
 */
class EnterPageCondition(signalCenter: SignalCenter) : Condition<EnterPage>(signalCenter) {

    override fun execute(signalCenter: SignalCenter, signal: EnterPage) {
    }
}