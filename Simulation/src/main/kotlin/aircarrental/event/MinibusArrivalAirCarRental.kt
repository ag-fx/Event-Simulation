package aircarrental.event

import aircarrental.entities.Buildings
import aircarrental.entities.Minibus
import aircarrental.entities.Terminal

class MinibusArrivalAirCarRental(private val minibus: Minibus, time: Double) : AcrEvent(time) {

    override fun execute() = with(core) {
        var totalTimeToExit = 0.0

        while (minibus.seats.isNotEmpty()) {
            totalTimeToExit += rndTimeToExitBus.next()
            carRental.queue.push(minibus.seats.pop())
        }

        plan(MinibusGoTo(
                minibus = minibus,
                destination = Buildings.TerminalOne,
                source = Buildings.AirCarRental,
                time = currentTime + totalTimeToExit
        ))

        println("Zatial som skoncil")
        TODO()
    }

    override fun toString() = "Minibus ${minibus.id} ${super.toString()}"

}
