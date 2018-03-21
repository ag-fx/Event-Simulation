package aircarrental.event

import aircarrental.entities.Buildings
import aircarrental.entities.Customer
import aircarrental.entities.Minibus

class MinibusArrivalTerminalOne(private val minibus: Minibus, time: Double) : AcrEvent(time) {

    override fun execute() = with(core) {

        var totalTimeToEnterBus = 0.0
        lateinit var currentCustomer: Customer
        //TODO bojim sa tohto nastupovania, ze to budu zle vysledky v statistickej triede
        while (terminalOne.queue.isNotEmpty() && minibus.isNotFull()) {
            currentCustomer = terminalOne.queue.pop()
            val timeToEnter = rndTimeToEnterBus.next()
            totalTimeToEnterBus += timeToEnter
            minibus.seats.push(currentCustomer)
        }

        plan(MinibusGoTo(
                minibus = minibus,
                destination = Buildings.TerminalTwo,
                source = Buildings.TerminalOne,
                time = currentTime + totalTimeToEnterBus
        ))

        //zatial pre istotu
        if (minibus.seats.size() > minibus.capacity)
            throw IllegalStateException("Bus has capacity ${minibus.capacity} but there's  ${minibus.seats.size()}")
    }

    override fun toString() = "Minibus ${minibus.id} ${super.toString()}"

}