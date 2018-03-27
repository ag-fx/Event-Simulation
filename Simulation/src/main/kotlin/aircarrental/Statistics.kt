package aircarrental

import java.util.*

data class Statistics(

    val avgTimeInSystem: LinkedList<Double>,
    val avgWaitTimeTerminal1: LinkedList<Double>,
    val avgWaitTimeTerminal2: LinkedList<Double>,
    val avgWaitTimeTerminalAirCarRental: LinkedList<Double>,
    val avgQueueSizeTerminal1: LinkedList<Double>,
    val avgQueueSizeTerminal2: LinkedList<Double>,
    val avgQueueSizeTerminalAirCarRental: LinkedList<Double>

)