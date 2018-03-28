package aircarrental

import aircarrental.entities.Customer
import aircarrental.entities.Employee
import aircarrental.entities.Minibus
import core.State

data class AirCarRentalState(
    val replicationNumber: Int,
    val minibuses: List<Minibus>,
    val avgQueueSizeTerminalOne: Double,
    val avgQueueSizeTerminalTwo: Double,
    val avgQueueSizeCarRental: Double,

    val avgQueueWaitTimeTerminalOne: Double,
    val avgQueueWaitTimeTerminalTwo: Double,
    val avgQueueWaitTimeCarRental: Double,

    val averageTimeOfCustomerInSystem: Double,

    val interval: Pair<Double, Double>,

    val totalTerminal1: Int,
    val totalTerminal2: Int,

    override val running: Boolean,
    override val stopped: Boolean,
    override val currentTime: Double,
    override val run: Int,

    val numberOfServedCustomers: Double,
    val totalCustomersTime: Double,
    val config: AirCarConfig,
    val terminal1Queue: List<Customer>,
    val terminal2Queue: List<Customer>,
    val carRentalQueue: List<Customer>,
    val employees: List<Employee>,
    val statistics: Statistics? = null
    ) : State




data class AirCarConfig(
        val numberOfMinibuses: Int,
        val numberOfEmployees: Int
)
