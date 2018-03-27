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
    var interval: Pair<Double, Double> = 0.0 to 0.0,
    var interval2: Pair<Double, Double> = 0.0 to 0.0,
    val avgTimeInSystem: LinkedList<Double>,
    val avgWaitTimeTerminal1: LinkedList<Double>,
    val avgWaitTimeTerminal2: LinkedList<Double>,
    val avgWaitTimeAirCarRental: LinkedList<Double>,
    val avgQueueSizeTerminal1: LinkedList<Double>,
    val avgQueueSizeTerminal2: LinkedList<Double>,
    val avgQueueSizeAirCarRental: LinkedList<Double>

)