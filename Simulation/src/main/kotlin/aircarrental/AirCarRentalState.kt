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
    val avgQueueWaitTimeTerminalOne: Double,
    val avgQueueWaitTimeTerminalTwo: Double,
    val customersTimeInSystem: Double,
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
    val employees: List<Employee>
) : State




data class AirCarConfig(
        val numberOfMinibuses: Int,
        val numberOfEmployees: Int
)
