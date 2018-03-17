package Core

import kotlinx.coroutines.experimental.channels.produce
import org.jetbrains.annotations.TestOnly
import java.util.*
import java.util.concurrent.PriorityBlockingQueue

abstract class Simulation<out S : State>(
        private val maxSimTime: Double = 30 * 24.0 * 60
) {

    private val timeLine = PriorityBlockingQueue<Event>()
    private val oneSecond = 1.0
    private var runs = 0

    var sleepTime = 0L

    var currentTime = 0.0
        private set(value) {
            field = value
        }

    var speed = oneSecond * 60

    private var isRunning = true

    protected val rndSeed = Random()

    fun start() = produce {
        repeat(5) {
            beforeReplication()

            while (shouldSimulate()) {
                if (isSimulationRunning()) {
                    val currentEvent = timeLine.poll()
                    currentTime = currentEvent.occurrenceTime
                    currentEvent.execute(this@Simulation)
                    //println(currentEvent)
                    val state = toState(runs++, currentTime)
                    send(state)
                }
            }

            isRunning = false
            send(toState(runs++, currentTime))
            afterReplication()
        }
        close()

        println("Simulation stopped")

    }

    fun plan(event: Event) {
        if (event.occurrenceTime >= currentTime)
            timeLine.add(event)
        else
            throw IllegalStateException("Time travel")
    }

    @TestOnly
    fun poll() = timeLine.poll()


    fun pause() {
        isRunning = false
    }

    fun resume() {
        isRunning = true
    }

    private fun isSimulationRunning() = isRunning

    private fun shouldSimulate() = currentTime < maxSimTime && timeLine.isNotEmpty()

    protected abstract fun afterReplication()

    protected abstract fun beforeReplication()

    protected abstract fun toState(run: Int, simTime: Double): S

}
