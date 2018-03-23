package aircarrental.event

import aircarrental.entities.*

class EnterToMinibus(
    val minibus: Minibus,
    val terminal: Terminal,
    var time: Double
) : AcrEvent(time) {

    override fun execute() = with(core) {

        if (minibus.isNotFull() && terminal.queue.isNotEmpty()) {
            val customer = terminal.queue.pop()
            minibus.enter(customer)
            if (log)
                println("Customer ${customer.id} entering at $currentTime")
        }

        if (minibus.isNotFull() && terminal.queue.isNotEmpty()) {
            with(this@EnterToMinibus) {
                occurrenceTime = currentTime + rndTimeToEnterBus.next()
                plan(this)
            }

        } else {
            when (terminal.description) {
                Buildings.TerminalOne -> plan(MinibusGoTo(minibus, Buildings.TerminalTwo, Buildings.TerminalOne, currentTime))
                Buildings.TerminalTwo -> plan(MinibusGoTo(minibus, Buildings.AirCarRental, Buildings.TerminalTwo, currentTime))
                Buildings.AirCarRental -> throw IllegalStateException("Poeple don't get on bus in AirCarRental")
            }

        }

    }

    override fun toString() = "Entering minibus ${minibus.id} at ${terminal.description} ${super.toString()}"

}