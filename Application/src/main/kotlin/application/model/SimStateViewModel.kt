package application.model

import aircarrental.AirCarRentalState
import tornadofx.*

class AirCarRentalStateModel(state: AirCarRentalState) : tornadofx.ItemViewModel<AirCarRentalState>(state) {
    val replicationNumber = bind(AirCarRentalState::replicationNumber)
    //    val minibuses = bind(AirCarRentalState::minibuses)
    val avgQueueSizeTerminalOne = bind(AirCarRentalState::avgQueueSizeTerminalOne)
    val avgQueueSizeTerminalTwo = bind(AirCarRentalState::avgQueueSizeTerminalTwo)
    val avgQueueSizeCarRental = bind(AirCarRentalState::avgQueueSizeCarRental)
    val avgQueueWaitTimeTerminalOne = bind(AirCarRentalState::avgQueueWaitTimeTerminalOne)
    val avgQueueWaitTimeTerminalTwo = bind(AirCarRentalState::avgQueueWaitTimeTerminalTwo)
    val avgQueueWaitTimeCarRental = bind(AirCarRentalState::avgQueueWaitTimeCarRental)
    val customersTimeInSystem = bind(AirCarRentalState::averageTimeOfCustomerInSystem)
    val totalTerminal1 = bind(AirCarRentalState::totalTerminal1)
    val totalTerminal2 = bind(AirCarRentalState::totalTerminal2)
    val running = bind(AirCarRentalState::running)
    val stopped = bind(AirCarRentalState::stopped)
    val currentTime = bind(AirCarRentalState::currentTime)
    val run = bind(AirCarRentalState::run)
    val numberOfServedCustomers = bind(AirCarRentalState::numberOfServedCustomers)
    val totalCustomersTime = bind(AirCarRentalState::totalCustomersTime)
    val configu = bind(AirCarRentalState::config)

    val terminal1Queue = state.terminal1Queue.map(::CustomerModel).observable()
    val terminal2Queue = state.terminal2Queue.map(::CustomerModel).observable()
    val carRentalQueue = state.carRentalQueue.map(::CustomerModel).observable()


}
