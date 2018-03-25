package aircarrental.event

import aircarrental.entities.Customer
import aircarrental.entities.Minibus
import aircarrental.entities.Terminal
import aircarrental.entities.nextStop

class EnterToMinibus(
    val minibus: Minibus,
    val terminal: Terminal,
    var time: Double,
    var customer: Customer
) : AcrEvent(time) {

    override fun execute() = with(core) {

        minibus.enter(customer)
        log("Customer ${customer.id} entering at $currentTime to minibus ${minibus.id}")

        if (minibus.isNotFull() && terminal.queue.isNotEmpty())
            plan(StartLoading(
                minibus = minibus,
                terminal = terminal,
                time = currentTime
            ))
        else
            plan(MinibusGoTo(
                minibus = minibus,
                destination = terminal.description.nextStop(),
                source = terminal.description,
                time = currentTime
            ))

    }

    override fun toString() = "Entering minibus ${minibus.id} at ${terminal.description} ${super.toString()}"

}
