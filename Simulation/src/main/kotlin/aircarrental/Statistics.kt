package aircarrental

import java.util.*

data class Statistics(
    val interval: LinkedList<Pair<Double, Double>> = LinkedList(),
    val avgTimeInSystem: LinkedList<Double> = LinkedList(),
    val avgWaitTimeTerminal1: LinkedList<Double> = LinkedList(),
    val avgWaitTimeTerminal2: LinkedList<Double> = LinkedList(),
    val avgWaitTimeAirCarRental: LinkedList<Double> = LinkedList(),
    val avgQueueSizeTerminal1: LinkedList<Double> = LinkedList(),
    val avgQueueSizeTerminal2: LinkedList<Double> = LinkedList(),
    val avgQueueSizeAirCarRental: LinkedList<Double> = LinkedList()

)