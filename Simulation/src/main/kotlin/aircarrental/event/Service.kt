package aircarrental.event

import aircarrental.entities.*

class Service(
    private val employee: Employee,
    time: Double
) : AcrEvent(time) {

    override fun execute() = with(core) {
        plan(ServiceEnd(carRental.queue.pop(), employee, currentTime + rndTimeToOneCustomerService.next()))
        employee.isBusy = true
    }

}

class ServiceEnd(
    private val customer: Customer,
    private val employee: Employee,
    time: Double
) : AcrEvent(time) {

    override fun execute() = with(core) {
        employee.isBusy = false
        totalCustomersTime += (currentTime - customer.arrivedToSystem)
        numberOfServedCustomers++
        if(carRental.queue.isNotEmpty())
            plan(Service(employee,currentTime))
    }

}