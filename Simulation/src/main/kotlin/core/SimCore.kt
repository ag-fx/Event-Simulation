package core

import com.sun.org.apache.xml.internal.security.Init
import com.sun.org.apache.xpath.internal.operations.Bool
import kotlinx.coroutines.experimental.DefaultDispatcher
import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import java.util.*
import java.util.concurrent.PriorityBlockingQueue
import kotlin.coroutines.experimental.CoroutineContext

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
    open var speed = oneSecond //* 60
    protected var isRunning = true
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

    fun start(context: CoroutineContext = DefaultDispatcher) = launch(context) {
        beforeSimulation()
        repeat(replications) {
            beforeReplication()
            simulate()
            coolDown()
            afterReplication()
            replicationStates += toState(it, currentTime)
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

    private val tick = Tick<S>(currentTime + speed)

    fun planTick() {
        tick.core = this
        tick.occurrenceTime = currentTime + speed
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

    protected open fun afterReplication() {

    }

    protected abstract fun beforeReplication()


    /**
     * Always call super when overriding
     */
    protected open fun afterSimulation() {
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

    private fun shouldSimulate() = (currentTime < maxSimTime) && timeline.isNotEmpty()

    fun log(s: Any) = if (log) println(s) else Unit

}