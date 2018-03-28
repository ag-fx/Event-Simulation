package core

import kotlinx.coroutines.experimental.channels.Channel
import java.util.*
import java.util.concurrent.PriorityBlockingQueue

abstract class SimCore<S : State>(val maxSimTime: Double, val replications: Int) {

    val currentReplicationChannel = Channel<S>()
    val afterReplicationChannel = Channel<List<S>>()

    private val timeline = PriorityBlockingQueue<Event>()
    private val replicationStates = mutableListOf<S>()
    private var runs = 0
    private var isWatched = true
    var log = true
    var sleepTime = 1000L
    protected var isRunning = true
    abstract var warmUpSeconds: Double

    var currentTime = 0.0
        private set(value) {
            field =
                if (value < field && value != 0.0)
                    throw IllegalArgumentException("Time travel")
                else value
        }

    var stop = false
        private set(value) {
            field = value
        }

    suspend fun start() {
        stop = false
        // currentReplicationChannel
        // afterReplicationChannel.o
        beforeSimulation()
        repeat(replications) { replicationNumber ->
            beforeReplication()
            simulate(replicationNumber)
            coolDown()
            afterReplication(replicationNumber + 1)
            replicationStates += toState(replicationNumber + 1, currentTime)
            if (!stop)
                afterReplicationChannel.send(replicationStates)
        }
        afterSimulation()
        isRunning = false
    }

    private suspend fun simulate(replicationNumber: Int) {
        if (isWatched())
            planTick()
        var notWarmedUp = true
        while (shouldSimulate()) {

            if (isSimulationRunning()) {
                val currentEvent = timeline.poll()
                currentTime = currentEvent.occurrenceTime
                log(currentEvent)
                currentEvent.execute()
                if (currentTime > warmUpSeconds && notWarmedUp) {
                    notWarmedUp = false
                    afterWarmUp()
                }
                if (isWatched() && currentTime > warmUpSeconds) {
                    val state = toState(replicationNumber, currentTime)
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

    abstract fun afterWarmUp()

    private suspend fun coolDown() {
        while (timeline.isNotEmpty()) {
            val currentEvent = timeline.poll()

            if (timeline.size == 0 && currentEvent == tick) break

            if (coolDownEventFilter(currentEvent)) {
                currentTime = currentEvent.occurrenceTime
                log("Cooling down : $currentEvent")
                currentEvent.execute()
                if (isWatched()) {
                    val state = toState(runs++, currentTime)
                    currentReplicationChannel.send(state)
                }
            }
        }
    }

    /**
     * Filter for events that should be executed. I recommend to filter self calling events which create
     * new entities in system. ie. customer arrivals.
     *
     * @param event event that is about to be executed after time was exceeded and timiline was not empty
     * @return True if event should be executed in cooldown
     */
    protected abstract fun coolDownEventFilter(event: Event): Boolean

    var oneTick = 1.0
    private val tick = Tick<S>(currentTime + oneTick)

    fun planTick() {
        tick.core = this
        tick.occurrenceTime = currentTime + oneTick
        timeline.add(tick)
    }

    /**
     * Always call super
     */
    open fun plan(event: Event) {
        //Always cast event to your event and assign a core to it.
        if (event.occurrenceTime >= currentTime)
            timeline.add(event)
        else
            throw IllegalArgumentException("Event is occurring in the past")
    }

    protected open fun afterReplication(replicationNumber: Int) {

    }

    protected abstract fun beforeReplication()


    /**
     * Always call super when overriding
     */
    protected open fun afterSimulation() {
        closeChannels()
    }

    private fun closeChannels() {
        afterReplicationChannel.close()
        currentReplicationChannel.close()
    }

    protected abstract fun beforeSimulation()

    protected abstract fun toState(replication: Int, simTime: Double): S

    protected val rndSeed = Random()

    /**
     * Always call super when overriding
     */
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
        closeChannels()
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

    private fun isSimulationRunning() = isRunning && !stop

    private fun shouldSimulate() = (currentTime < maxSimTime + warmUpSeconds) && timeline.isNotEmpty()

    fun log(s: Any) = if (log) println(s) else Unit

}