package Core

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import java.util.*
import java.util.concurrent.PriorityBlockingQueue


abstract class SimCore<State>(private val maxSimTime: Double, private val numberOfReplications: Int) {

    private val simulation = BehaviorSubject.create<State>().toSerialized().apply { observeOn(Schedulers.newThread()) }
    fun simulation(): Observable<State> = simulation
    private val timeLine = PriorityBlockingQueue<Event>()
    private var isRunning = true

    protected var stop = false

    var currentTime = 0.0
        private set(value) {
            field = value
        }

    private fun shouldSimulate() = currentTime < maxSimTime && timeLine.isNotEmpty()

    fun start() {
        beforeSimulation()
        for (i in 0..numberOfReplications) {
            beforeReplication()
            simulate()
            afterReplication()
        }
        afterSimulation()
    }

    private fun simulate() {
        var run = 0
        while (shouldSimulate()) {
            if (isSimulationRunning()) {
                val currentEvent = timeLine.poll()
                currentTime = currentEvent.occurrenceTime
                currentEvent.execute()
                simulation.onNext(toState(run++))
            }
        }
    }

    open fun plan(event: Event) {
        if (event.occurrenceTime >= currentTime)
            timeLine.add(event)
        else
            throw IllegalStateException("Time travel")
    }

    private fun isSimulationRunning() = isRunning && !stop

    protected abstract fun toState(run: Int): State
    protected abstract fun beforeSimulation()
    protected abstract fun afterSimulation()
    protected abstract fun beforeReplication()
    protected abstract fun afterReplication()

    protected val rndSeed = Random()

    protected fun clear(){
        timeLine.clear()
        currentTime = 0.0
    }

    fun pause(){
        isRunning = false
    }

    fun resume(){
        isRunning = true
    }

}