package aircarrental.event

import aircarrental.entities.*
import java.util.*

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

fun main(args: Array<String>) {
    val r = Random(6L)
    for (i in 0..5) {
        println(r.nextDouble())
        Thread.sleep(100)
    }

}