package aircarrental.event

import aircarrental.entities.Buildings
import aircarrental.entities.Customer
import aircarrental.entities.Minibus

class MinibusArrivalTerminalTwo(private val minibus: Minibus, time: Double) : AcrEvent(time) {

    override fun execute() = with(core) {
        var totalTimeToEnterBus = 0.0
        lateinit var currentCustomer: Customer

        while (terminalTwo.queue.isNotEmpty() && minibus.isNotFull()) {
            currentCustomer = terminalTwo.queue.pop()
            totalTimeToEnterBus += rndTimeToEnterBus.next()
            minibus.seats.push(currentCustomer)
        }

        plan(MinibusGoTo(
                minibus = minibus,
                destination = Buildings.AirCarRental,
                source = Buildings.TerminalTwo,
                time = currentTime + totalTimeToEnterBus
        ))

        //zatial pre istotu
        if (minibus.seats.size() > minibus.capacity)
            throw IllegalStateException("Bus has capacity ${minibus.capacity} but there's  ${minibus.seats.size()}")
    }

    override fun toString()  = "Minibus ${minibus.id} ${super.toString()}"
}