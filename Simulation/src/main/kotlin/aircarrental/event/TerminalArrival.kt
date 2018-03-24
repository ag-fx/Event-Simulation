package aircarrental.event

import aircarrental.entities.Buildings
import aircarrental.entities.Customer

class TerminalOneCustomerArrival(time: Double) : AcrEvent(time) {

    override fun execute() = with(core) {
        terminalOne.queue.push(Customer(
            id = terminalOne.arrivals++,
            terminal = Buildings.TerminalOne,
            arrivedToSystem = currentTime
        ))
        occurrenceTime = currentTime + rndArrivalTerminalOne.next()
        plan(this@TerminalOneCustomerArrival)
        log("Terminal 1 = ${terminalOne.queue.size()}")
    }

}

class TerminalTwoCustomerArrival(time: Double) : AcrEvent(time) {

    override fun execute() = with(core) {
        terminalTwo.queue.push(Customer(
            id =  terminalTwo.arrivals++,
            terminal = Buildings.TerminalTwo,
            arrivedToSystem = currentTime
        ))

        occurrenceTime = currentTime + rndArrivalTerminalTwo.next()
        plan(this@TerminalTwoCustomerArrival)
        log("Terminal 2 = ${terminalTwo.queue.size()}")

    }

}