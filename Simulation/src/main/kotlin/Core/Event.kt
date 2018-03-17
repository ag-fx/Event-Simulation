package Core

/**
 * Even has time of occurence - when it's going to happen
 * nema casove trvanie
 */
abstract class Event(val occurrenceTime: Double) : Comparable<Event> {

    abstract fun execute(simulation: Simulation<State>)

    override fun compareTo(other: Event) = occurrenceTime.compareTo(other.occurrenceTime)

    override fun toString() = "${this.javaClass.simpleName} $occurrenceTime"

}