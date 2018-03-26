package aircarrental.event

import aircarrental.entities.*

class Service(
    private val employee: Employee,
    time: Double
) : AcrEvent(time) {

    override fun execute() = with(core) {
        val served = carRental.queue.pop()
        plan(ServiceEnd(served, employee, currentTime + rndTimeToOneCustomerService.next()))
        employee.isBusy = true
        employee.serving = served
    }

}

class ServiceEnd(
    private val customer: Customer,
    private val employee: Employee,
    time: Double
) : AcrEvent(time) {

    override fun execute() = with(core) {
        employee.isBusy = false
        employee.serving = null
        totalCustomersTime += (currentTime - customer.arrivedToSystem)
        numberOfServedCustomers++
        if(carRental.queue.isNotEmpty())
            plan(Service(employee,currentTime))
    }

}