package aircarrental.entities

import core.Statistical

data class Customer(
    val id: Int,
    val terminal: Buildings,
    var startWaitingInCarRental: Double = 0.0,
    var getOnBusTime: Double = 0.0,
    var getFromBusTime: Double = 0.0,
    override var arrivedToSystem: Double
) : Statistical, Comparable<Customer> {

    override fun compareTo(other: Customer) = arrivedToSystem.compareTo(other.arrivedToSystem)

    override fun toString() = "Customer $id from $terminal"

}


