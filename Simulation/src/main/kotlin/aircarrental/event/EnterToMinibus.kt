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
            log("Customer ${customer.id} entering at $currentTime")
        }

        if (minibus.isNotFull() && terminal.queue.isNotEmpty()) {
            with(this@EnterToMinibus) {
                occurrenceTime = currentTime + rndTimeToEnterBus.next()
                plan(this)
            }

        } else {
            plan(MinibusGoTo(
                minibus = minibus,
                destination = terminal.description.nextStop(),
                source = terminal.description,
                time = currentTime
            ))
        }

    }

    override fun toString() = "Entering minibus ${minibus.id} at ${terminal.description} ${super.toString()}"

}