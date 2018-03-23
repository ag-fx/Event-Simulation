package aircarrental.event

import aircarrental.entities.*

class ExitMinibus(
    private val minibus: Minibus,
    var time: Double
) : AcrEvent(time) {

    override fun execute() = with(core) {
        if (minibus.isNotEmpty()) {
            val exitingCustomer = minibus.seats.pop()
            carRental.queue.push(exitingCustomer)

            log("$exitingCustomer exiting bus ${minibus.id}  at $occurrenceTime")

            carRental.employees
                .firstOrNull(Employee::isNotBusy)
                ?.let {
                    plan(Service(it,currentTime))
                    it.isBusy = true
                }
        }

        if (minibus.isNotEmpty()) {
            occurrenceTime = currentTime + rndTimeToExitBus.next()
            plan(this@ExitMinibus)

        } else {
            plan(MinibusGoTo(
                minibus = minibus,
                destination = Buildings.TerminalOne,
                source = Buildings.AirCarRental,
                time = currentTime
            ))
        }
    }

    override fun toString() = "Exiting minibus ${minibus.id} ${super.toString()}"

}