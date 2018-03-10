package Core

import kotlinx.coroutines.experimental.channels.produce
import org.jetbrains.annotations.TestOnly
import java.util.*

abstract class Simulation<out S : State>(private val maxSimTime: Double) {


    private val timeline = PriorityQueue<Event>()

    private val executedEvents = mutableListOf<Event>()

    protected val rndSeed = Random()

    protected var simTime = 0.0

    protected var isRunning = true

    fun pause() {
        isRunning = false
    }

    fun resume() {
        isRunning = true
    }

    fun isSimualtionRunning() = isRunning

    fun start() = produce {
        while (simTime < maxSimTime && timeline.isNotEmpty()) {
            if (isSimualtionRunning()) {
                val currentEvent = timeline.poll()
                simTime += currentEvent.occurrenceTime
                currentEvent.execute()
                executedEvents.add(currentEvent)
                send(toState(simTime, executedEvents))
            }
        }
        close()
        println("Simulation stopped")
    }

    fun plan(event: Event) = timeline.add(event)

    @TestOnly
    fun poll() = timeline.poll()

    protected abstract fun toState(simTime: Double, executedEvents: List<Event>): S
}