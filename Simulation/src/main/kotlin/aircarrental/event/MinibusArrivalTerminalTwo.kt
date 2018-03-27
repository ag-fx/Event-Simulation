package aircarrental.event

import aircarrental.entities.*

class MinibusArrivalTerminalTwo(private val minibus: Minibus, time: Double) : AcrEvent(time) {

    override fun execute() = with(core) {
        minibus.isInDestination = true

        if (terminalTwo.queue.isNotEmpty() && minibus.isNotFull())
            plan(StartLoading(
                minibus = minibus,
                terminal = terminalTwo,
                time = currentTime
            ))
        else
            plan(MinibusGoTo(
                minibus = minibus,
                source = Buildings.TerminalTwo,
                destination = Buildings.AirCarRental,
                time = currentTime
            ))
    }

    override fun toString() = "Minibus ${minibus.id} ${super.toString()}"

}