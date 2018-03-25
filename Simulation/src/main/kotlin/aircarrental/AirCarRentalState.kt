package aircarrental

import aircarrental.entities.Minibus
import core.State

data class AirCarRentalState(
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
    val config : AirCarConfig
) : State




data class AirCarConfig(
        val numberOfMinibuses: Int,
        val numberOfEmployees: Int
)
