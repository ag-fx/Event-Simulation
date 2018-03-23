package aircarrental.event

import aircarrental.entities.*

class Service(
    time: Double
) : AcrEvent(time) {

    override fun execute() = with(core) {
        carRental.employees
            .firstOrNull(Employee::isNotBusy)
            ?.let { employee ->
            if (carRental.queue.isNotEmpty()) {
                plan(ServiceEnd(carRental.queue.pop(), employee, currentTime + rndTimeToOneCustomerService.next()))
                employee.isBusy = true
            }
        }
        Unit
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
        plan(Service(currentTime))
    }

}