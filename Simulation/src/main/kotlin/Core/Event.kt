package Core

/**
 * Even has time of occurence - when it's going to happen
 * nema casove trvanie
 */
abstract class Event(val occurrenceTime: Double) : Comparable<Event> {

    abstract fun execute(simulation: Simulation<State>)

    override fun compareTo(other: Event) = occurrenceTime.compareTo(other.occurrenceTime)

    override fun toString() = "${this.javaClass.name} $occurrenceTime"

}

class Tick(occurrenceTime: Double) : Event(occurrenceTime) {
    override fun execute(simulation: Simulation<State>) {
        Thread.sleep(simulation.sleepTime)
        simulation.plan(Tick(simulation.currentTime + simulation.speed))
        if(simulation.currentTime > 11)
            simulation.sleepTime = 2000L
    }
}