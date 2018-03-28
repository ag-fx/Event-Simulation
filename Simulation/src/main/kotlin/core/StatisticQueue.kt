package core

import java.util.*
import kotlin.math.pow
import kotlin.math.sqrt

interface Statistical {
    var arrivedToSystem: Double
}

class StatisticPriorityQueue<T : Statistical, S : State>(private val simCore: SimCore<S>) {

    private val queue = PriorityQueue<T>()
    private var lastChange = 0.0
    private var totalTime = 0.0
    private var weightTime = 0.0
    private var totalWaitTime = 0.0
    private var served = 0.0

    private fun beforeChange() {
        weightTime += (simCore.currentTime - lastChange) * queue.size
        totalTime += (simCore.currentTime - lastChange)
    }

    fun push(t: T) {
        beforeChange()
        queue.add(t)
        lastChange = simCore.currentTime
    }

    fun pop(): T {
        beforeChange()
        val popped = queue.remove()
        lastChange = simCore.currentTime
        totalWaitTime += simCore.currentTime - popped.arrivedToSystem
        served++
        return popped
    }

    fun averageWaitTime() = totalWaitTime / served

    fun averageSize() = weightTime / totalTime

    fun isEmpty() = queue.isEmpty()

    fun size() = queue.size

    fun isNotEmpty() = queue.isNotEmpty()

    fun clear() {
        queue.clear()
        clearStat()
    }

    fun fixTimeAtEndOfSimulation(lastTimeInSimulation: Double) {
//        weightTime += (lastChange - lastTimeInSimulation) * queue.size
//        totalTime  += (simCore.currentTime - lastTimeInSimulation)
    }

    fun clearStat() {
        lastChange = 0.0
        totalTime = 0.0
        weightTime = 0.0
        totalWaitTime = 0.0
        served = 0.0
    }

    fun toList() = queue.toList()


}

class StatisticQueue<T : Statistical, S : State>(private val simCore: SimCore<S>) {

    private val queue = LinkedList<T>() as Queue<T>
    private var lastChange = 0.0
    private var totalTime = 0.0

    private var weightTime = 0.0

    var totalWaitTime = 0.0
        private set(value) {
            field = value
        }
    private var served = 0

    private fun beforeChange() {
        weightTime += (simCore.currentTime - lastChange) * queue.size
        totalTime += (simCore.currentTime - lastChange)
    }

    fun push(t: T) {
        beforeChange()
        queue.add(t)
        lastChange = simCore.currentTime
    }

    fun remove(t: T) {
        val popped = (queue as LinkedList<T>).remove(t).let { t }
        lastChange = simCore.currentTime
        totalWaitTime += simCore.currentTime - popped.arrivedToSystem
        served++
    }

    fun pop(): T {
        beforeChange()
        val popped = queue.remove()
        lastChange = simCore.currentTime
        totalWaitTime += simCore.currentTime - popped.arrivedToSystem
        served++
        return popped
    }

    fun averageWaitTime() = totalWaitTime / served

    fun averageSize() = weightTime / totalTime


    fun isEmpty() = queue.isEmpty()

    fun size() = queue.size

    fun isNotEmpty() = queue.isNotEmpty()

    fun clear() {
        queue.clear()
        clearStat()
    }

    fun clearStat() {
        lastChange = 0.0
        totalTime = 0.0
        weightTime = 0.0
        totalWaitTime = 0.0
        served = 0
    }

    fun toList() = queue.toList()

    fun fixTimeAtEndOfSimulation(lastTimeInSimulation: Double) {
        lastTimeInSimulation.let { it }
//        weightTime += (lastChange - lastTimeInSimulation) * queue.size
//        totalTime  += (simCore.currentTime - lastTimeInSimulation)
    }
}

abstract class Stat {

    private var sumXpow2 = 0.0
    private var sumX = 0.0
    private var served = 0.0
    val alfa = 1.645

    private fun alfaS() = alfa * sqrt((sumXpow2 / served) - ((sumX.pow(2)) / served))

    fun meanConfidence90(): Double {
        val standDeviation = Math.sqrt(1 / served as Double * sumXpow2 - Math.pow(1 / served as Double * sumX, 2.0))
        return 1.645 * (standDeviation / Math.sqrt(served))
    }

    fun average()       = sumX / served
    fun averageSquare() = sumXpow2 / served

    fun interval(avg: Double, n: Int) = avg - (alfaS() / sqrt(n - 1.0)) to avg + (alfaS() / sqrt(n - 1.0))

    fun add(t: Double) {
        sumX += t
        sumXpow2 += t.pow(2)
        served += 1
    }

    fun clear() {
        sumX = 0.0
        sumXpow2 = 0.0
        served = 0.0
    }
}
