package application.model

import aircarrental.AirCarRentalState

class AirCarRentalStateModel(state:AirCarRentalState) : tornadofx.ItemViewModel<AirCarRentalState>(state) {
//    val minibuses = bind(AirCarRentalState::minibuses)
//    val avgQueueSizeTerminalOne = bind(AirCarRentalState::avgQueueSizeTerminalOne)
//    val avgQueueSizeTerminalTwo = bind(AirCarRentalState::avgQueueSizeTerminalTwo)
//    val avgQueueWaitTimeTerminalOne = bind(AirCarRentalState::avgQueueWaitTimeTerminalOne)
//    val avgQueueWaitTimeTerminalTwo = bind(AirCarRentalState::avgQueueWaitTimeTerminalTwo)
//    val customersTimeInSystem = bind(AirCarRentalState::customersTimeInSystem)
//    val totalTerminal1 = bind(AirCarRentalState::totalTerminal1)
//    val totalTerminal2 = bind(AirCarRentalState::totalTerminal2)
//    val running = bind(AirCarRentalState::running)
//    val stopped = bind(AirCarRentalState::stopped)
    val currentTime = bind(AirCarRentalState::currentTime)
//    val run = bind(AirCarRentalState::run)
//    val numberOfServedCustomers = bind(AirCarRentalState::numberOfServedCustomers)
    val customersTimeInSystem = bind(AirCarRentalState::customersTimeInSystem)


}