package aircarrental.event

import aircarrental.entities.Customer

class TerminalOneCustomerArrival(time: Double) : AcrEvent(time) {

    override fun execute() = with(core) {
        occurrenceTime = currentTime + rndArrivalTerminalOne.next()
        plan(this@TerminalOneCustomerArrival)
        terminalOne.queue.push(Customer(arrivedToSystem = currentTime))
    }

}


class TerminalTwoCustomerArrival(time: Double) : AcrEvent(time) {

    override fun execute() = with(core) {
        occurrenceTime = currentTime + rndArrivalTerminalTwo.next()
        plan(this@TerminalTwoCustomerArrival)
        terminalTwo.queue.push(Customer(arrivedToSystem = currentTime))
    }

}