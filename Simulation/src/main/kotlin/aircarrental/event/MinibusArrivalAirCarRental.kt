package aircarrental.event

import aircarrental.entities.*

class MinibusArrivalAirCarRental(private val minibus: Minibus, time: Double) : AcrEvent(time) {

    override fun execute() = core.plan(ExitMinibus(minibus, core.currentTime + core.rndTimeToExitBus.next()))

    override fun toString() = "Minibus ${minibus.id} ${super.toString()}"

}