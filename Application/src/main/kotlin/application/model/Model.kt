package application.model

import aircarrental.entities.Customer

class CustomerModel(customer:Customer) : tornadofx.ItemViewModel<Customer>(customer) {
    val id = bind(Customer::id)
    val terminal = bind(Customer::terminal)
    val rentCarWaitingTime = bind(Customer::startWaitingInCarRental)
    val getOnBusTime = bind(Customer::getOnBusTime)
    val getFromBusTime = bind(Customer::getFromBusTime)
    val arrivedToSystem = bind(Customer::arrivedToSystem)
}