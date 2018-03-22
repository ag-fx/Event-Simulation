package aircarrental.event

import aircarrental.entities.Buildings
import aircarrental.entities.Minibus
import aircarrental.entities.Terminal


class EnterToMinibus(
        val minibus: Minibus,
        val terminal: Terminal,
        var numberOfCustomersInQueue: Int,
        var time: Double
) : AcrEvent(time) {

    override fun execute() = with(core) {

        if (numberOfCustomersInQueue > 0 && minibus.isNotFull() && terminal.queue.isNotEmpty()) {
            val customer = terminal.queue.pop()
            minibus.seats.push(customer)
            val timeToEnter = core.rndTimeToEnterBus.next()
            if(log){
                println("$customer entering minibus ${minibus.id} and it takes him $timeToEnter")
            }

            with(this@EnterToMinibus){
                numberOfCustomersInQueue--
                occurrenceTime = currentTime + timeToEnter
            }
            plan(this@EnterToMinibus)

        }
        else{
            when(terminal.description){
                Buildings.TerminalOne -> plan(MinibusGoTo(minibus, Buildings.TerminalTwo, Buildings.TerminalOne,currentTime))
                Buildings.TerminalTwo -> plan(MinibusGoTo(minibus, Buildings.AirCarRental, Buildings.TerminalTwo,currentTime))
                Buildings.AirCarRental -> throw IllegalStateException("Poeple don't get on bus in AirCarRental")
            }
                }
//        while (terminal.queue.isNotEmpty() && minibus.isNotFull()) {
//            currentCustomer = terminal.queue.pop()
//            val timeToEnter = core.rndTimeToEnterBus.next()
//            minibus.seats.push(currentCustomer)
//        }


    }

    override fun toString() = "Entering minibus ${minibus.id} at ${terminal.description}"
}