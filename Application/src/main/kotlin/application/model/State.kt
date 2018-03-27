package application.model

import aircarrental.AirCarConfig
import aircarrental.AirCarRentalState

private val init = AirCarRentalState(
    replicationNumber = 0,
    minibuses = mutableListOf(),
    avgQueueSizeTerminalOne = 0.0,
    avgQueueSizeTerminalTwo = 0.0,
    avgQueueSizeCarRental = 0.0,
    avgQueueWaitTimeTerminalOne = 0.0,
    avgQueueWaitTimeTerminalTwo = 0.0,
    avgQueueWaitTimeCarRental = 0.0,
    averageTimeOfCustomerInSystem = 0.0,
    interval = 0.0 to 0.0,
    totalTerminal1 = 0,
    totalTerminal2 = 0,
    running = false,
    stopped = false,
    currentTime = -1.0,
    run = 0,
    numberOfServedCustomers = 0.0,
    totalCustomersTime = 0.0,
    config = AirCarConfig(1, 1),
    terminal1Queue = mutableListOf(),
    terminal2Queue = mutableListOf(),
    carRentalQueue = mutableListOf(),
    employees = mutableListOf()
)

val initModel = AirCarRentalStateModel(init)
