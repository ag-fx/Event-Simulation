package aircarrental.event

import aircarrental.entities.*

class MinibusArrivalTerminalTwo(private val minibus: Minibus, time: Double) : AcrEvent(time) {

    override fun execute() = with(core) {
        plan(EnterToMinibus(
                minibus = minibus,
                terminal = terminalTwo,
                time = currentTime
        ))
    }

    override fun toString() = "Minibus ${minibus.id} ${super.toString()}"
}