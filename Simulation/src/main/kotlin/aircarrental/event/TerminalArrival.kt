package aircarrental.event

import aircarrental.entities.Buildings
import aircarrental.entities.Customer

class TerminalOneCustomerArrival(time: Double) : AcrEvent(time) {

    override fun execute() = with(core) {
        val customer = Customer(
            id = 1+ terminalOne.arrivals++,
            terminal = Buildings.TerminalOne,
            arrivedToSystem = currentTime
        )
        terminalOne.queue.push(customer)
        ppl.push(customer)
        occurrenceTime = currentTime + rndArrivalTerminalOne.next()
        plan(this@TerminalOneCustomerArrival)
    }

}

class TerminalTwoCustomerArrival(time: Double) : AcrEvent(time) {

    override fun execute() = with(core) {
        val customer = Customer(
            id =  1+ terminalTwo.arrivals++,
            terminal = Buildings.TerminalTwo,
            arrivedToSystem = currentTime
        )
        terminalTwo.queue.push(customer)
        ppl.push(customer)

        occurrenceTime = currentTime + rndArrivalTerminalTwo.next()
        plan(this@TerminalTwoCustomerArrival)

    }

}