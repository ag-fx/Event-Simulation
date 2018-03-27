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

    fun clearStat(){
        lastChange = 0.0
        totalTime = 0.0
        weightTime = 0.0
        totalWaitTime = 0.0
        served = 0
    }

    fun toList() = queue.toList()

}

abstract class Stat {

    private var sumXpow2 = 0.0
    private var sumX = 0.0
    private var served = 0

    private val alfa = 1.645



    fun meanConfidence90(): Double {
        val standDeviation = Math.sqrt(1 / (served*1.0)  * sumXpow2 - Math.pow(1 / (served*1.0) * sumX, 2.0))
        return 1.645 * (standDeviation / Math.sqrt(served*1.0))
    }

    fun average() = sumX / served

    fun interval() = average() - meanConfidence90() to average() + meanConfidence90()

    fun add(t: Double) {
        sumX += t
        sumXpow2 += t.pow(2)
        served++
    }

    fun clear() { //TODO zavolat v after replication
        sumX = 0.0
        sumXpow2 = 0.0
        served = 0
    }
}
