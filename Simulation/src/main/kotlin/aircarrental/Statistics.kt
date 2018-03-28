package aircarrental

import java.util.*

//abstract class Stat {
//    var count: Int = 0
//    var sumXpow2 = 0.0
//    var sumX = 0.0
//
//    val meanConfidence90
//        get() = {
//            val standDeviation = Math.sqrt(1 / count * sumXpow2 - Math.pow(1 / count * sumX, 2.0))
//            1.645 * (standDeviation / Math.sqrt(count * 1.0))
//        }
//
//    fun add() {
//        count++
//    }
//}

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