package aircarrental.event

import aircarrental.entities.*

class MinibusArrivalTerminalOne(private val minibus: Minibus, time: Double) : AcrEvent(time) {

    override fun execute() = with(core) {

        if (minibus.isNotEmpty()) throw IllegalStateException("Minibus has to arrive empty to Terminal 1")

        if (terminalOne.queue.isNotEmpty())
            plan(StartLoading(
                minibus = minibus,
                terminal = terminalOne,
                time = currentTime
            ))
        else
            plan(MinibusGoTo(
                minibus = minibus,
                source = Buildings.TerminalOne,
                destination = Buildings.TerminalTwo,
                time = currentTime
            ))
    }

    override fun toString() = "Minibus ${minibus.id} ${super.toString()}"

}