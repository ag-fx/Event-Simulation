package Core

import TestSim.Tick
import kotlinx.coroutines.experimental.channels.produce
import kotlinx.coroutines.experimental.sync.Mutex
import org.jetbrains.annotations.TestOnly
import java.util.*
import java.util.concurrent.PriorityBlockingQueue

abstract class Simulation<out S : State>(
        private val maxSimTime: Double = 30 * 24.0
) {

    private val timeLine = PriorityBlockingQueue<Event>()
    private val allEvents = Collections.synchronizedCollection(LinkedList<Event>())
    private val oneSecond = 1.0//1 / (60 * 60) // 1 seconds in one hour

    var sleepTime = 0L
    var currentTime = 0.0
        private set(value) {
            field = value
        }

    var speed = oneSecond * 60

    private var isRunning = true

    protected val rndSeed = Random()

    fun start() = produce {
        while (currentTime < maxSimTime && timeLine.isNotEmpty()) {
            if (isSimulationRunning()) {
                val currentEvent = timeLine.poll()
                currentTime = currentEvent.occurrenceTime
                currentEvent.execute(this@Simulation)
                println(currentEvent)
                allEvents.add(currentEvent)
                val state = toState(currentTime, allEvents)
                send(state)
//                plan(Tick(currentTime+speed))
            }
        }
        isRunning = false
        send(toState(currentTime, allEvents))
        close()

        print ("Simulation stopped ")
        when {
            currentTime > maxSimTime -> println("cause we ran out of time")
            timeLine.isEmpty() -> println("cause time line is empty")
        }


    }

    fun plan(event: Event) {
        if (event.occurrenceTime >= currentTime)
            timeLine.add(event)
        else
            throw IllegalStateException("Time travel exception")
    }

    @TestOnly
    fun poll() = timeLine.poll()

    protected abstract fun toState(simTime: Double, lastEvent: MutableCollection<Event>): S

    fun pause() {
        isRunning = false
    }

    fun resume() {
        isRunning = true
    }

    private fun isSimulationRunning() = isRunning
}