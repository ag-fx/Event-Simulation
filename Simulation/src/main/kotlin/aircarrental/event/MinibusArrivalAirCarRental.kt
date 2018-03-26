package aircarrental.event

import aircarrental.entities.*

class MinibusArrivalAirCarRental(private val minibus: Minibus, time: Double) : AcrEvent(time) {

    override fun execute() = with(core) {
        minibus.isInSource = true
        if (minibus.isNotEmpty())
            plan(ExitMinibus(
                minibus = minibus,
                time = currentTime + rndTimeToExitBus.next()
            ))
        else
            plan(MinibusGoTo(
                minibus = minibus,
                source = Buildings.AirCarRental,
                destination = Buildings.TerminalOne,
                time = currentTime
            ))
    }

    override fun toString() = "Minibus ${minibus.id} ${super.toString()}"

}