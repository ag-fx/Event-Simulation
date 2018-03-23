package core

import java.util.*

interface Statistical {
    var arrivedToSystem: Double
}

class  StatisticalPriorityQueue<T : Statistical, S : State>(private val simCore: SimCore<S>) {

    private val queue           = PriorityQueue<T>()
    private var lastChange      = 0.0
    private var totalTime       = 0.0
    private var weightTime      = 0.0
    private var totalWaitTime   = 0.0
    private var served          = 0.0

    private fun beforeChange() {
        weightTime += (simCore.currentTime - lastChange) * queue.size
        totalTime  += (simCore.currentTime - lastChange)
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

    fun clear(){
        queue.clear()
        lastChange      = 0.0
        totalTime       = 0.0
        weightTime      = 0.0
        totalWaitTime   = 0.0
        served          = 0.0
    }
}

 class StatisticQueue<T : Statistical, S : State>(private val simCore: SimCore<S>) {

    private val queue           = LinkedList<T>() as Queue<T>
    private var lastChange      = 0.0
    private var totalTime       = 0.0
    private var weightTime      = 0.0
    private var totalWaitTime   = 0.0
    private var served          = 0.0

    private fun beforeChange() {
        weightTime += (simCore.currentTime - lastChange) * queue.size
        totalTime  += (simCore.currentTime - lastChange)
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

    fun clear(){
        queue.clear()
        lastChange      = 0.0
        totalTime       = 0.0
        weightTime      = 0.0
        totalWaitTime   = 0.0
        served          = 0.0
    }

}