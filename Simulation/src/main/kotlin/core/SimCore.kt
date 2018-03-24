package core

import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import java.util.*
import java.util.concurrent.PriorityBlockingQueue

abstract class SimCore<S : State>(val maxSimTime: Double, val replications: Int) {

    val currentReplicationChannel = Channel<S>()
    val afterReplicationChannel = Channel<List<S>>()

    private val timeline = PriorityBlockingQueue<Event>()
    private val replicationStates = mutableListOf<S>()
    private val oneSecond = 1.0
    private var runs = 0
    private var isWatched = true
    var log = true

    var sleepTime = 1000L

    var currentTime = 0.0
        private set(value) {
            field = if (value < field && value != 0.0) throw IllegalArgumentException("Time travel") else value
        }

    open var speed = oneSecond //* 60

    protected var isRunning = true

    var stop = false
        private set(value) {
            field = value
        }

    fun start() = launch {
        beforeSimulation()
        repeat(replications) {
            beforeReplication()
            simulate()
            afterReplication()
            replicationStates += toState(runs, currentTime)
            if (!stop)
                afterReplicationChannel.send(replicationStates)
        }
        afterSimulation()
        isRunning = false
    }

    private suspend fun simulate() {
        if (isWatched())
            planTick()

        while (shouldSimulate()) {

            if (isSimulationRunning()) {

                val currentEvent = timeline.poll()
                currentTime = currentEvent.occurrenceTime
                log(currentEvent)
                currentEvent.execute()

                if (isWatched()) {
                    val state = toState(runs++, currentTime)
                    currentReplicationChannel.send(state)
                }


            } else {
                Thread.sleep(250)
            }

            if (stop) {
                val state = toState(runs++, currentTime)
                if (isWatched())
                    currentReplicationChannel.send(state)
            }

        }
    }

    private val tick = Tick<S>(currentTime + speed)

    fun planTick() {
        tick.core = this
        tick.occurrenceTime = currentTime + speed
        timeline.add(tick)
    }

    open fun plan(event: Event) {
        if (event.occurrenceTime >= currentTime)
            timeline.add(event)
        else
            throw IllegalArgumentException("Event is occurring in the past")
    }

    private fun isSimulationRunning() = isRunning && !stop

    private fun shouldSimulate() = currentTime < maxSimTime && timeline.isNotEmpty()

    protected open fun afterReplication() {
        //TODO osterit posledne eventy v kalendari
    }

    protected abstract fun beforeReplication()

      protected open fun afterSimulation() {
        afterReplicationChannel.close()
        currentReplicationChannel.close()
    }

    protected abstract fun beforeSimulation()
    protected abstract fun toState(run: Int, simTime: Double): S
    protected val rndSeed = Random()

    fun log(s: Any) {
        if (log)
            println(s)
    }

    open fun clear() {
        currentTime = 0.0
        runs = 0
        isRunning = true
        stop = false
        timeline.clear()
    }

    fun pause() {
        isRunning = false
    }

    fun resume() {
        isRunning = true
    }

    fun stop() {
        stop = true
    }

    fun isWatched() = isWatched

    fun startWatching() {
        isWatched = true
        planTick()
    }

    fun stopWatching() {
        isWatched = false
    }

}