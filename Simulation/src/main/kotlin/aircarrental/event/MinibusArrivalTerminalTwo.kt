package aircarrental.event

import aircarrental.entities.*

class MinibusArrivalTerminalTwo(private val minibus: Minibus, time: Double) : AcrEvent(time) {

    override fun execute() = with(core) {
        if (terminalTwo.queue.isNotEmpty() && minibus.isNotFull())
            plan(EnterToMinibus(
                minibus = minibus,
                terminal = terminalTwo,
                time = currentTime + rndTimeToEnterBus.next()
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