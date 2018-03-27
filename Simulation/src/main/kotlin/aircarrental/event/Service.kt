package aircarrental.event

import aircarrental.entities.*

class Service(
    private val employee: Employee,
    private val toServe: Customer,
    time: Double
) : AcrEvent(time) {

    override fun execute() = with(core) {
        carRental.serviceTotalWaitTime += currentTime - toServe.startWaitingInCarRental
        carRental.served++
        plan(ServiceEnd(toServe, employee, currentTime + rndTimeToOneCustomerService.next()))
        employee.isBusy = true
        employee.serving = toServe
    }

}

class ServiceEnd(
    private val customer: Customer,
    private val employee: Employee,
    time: Double
) : AcrEvent(time) {

    override fun execute() = with(core) {
        ppl.remove(customer)

        employee.isBusy = false
        employee.serving = null
        totalCustomersTime += (currentTime - customer.arrivedToSystem)
        numberOfServedCustomers++
        if(carRental.queue.isNotEmpty())
            plan(Service(employee,carRental.queue.pop(),currentTime))
    }

}