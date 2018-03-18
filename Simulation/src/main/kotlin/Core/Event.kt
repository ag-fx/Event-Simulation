package Core

abstract class Event(val occurrenceTime: Double) : Comparable<Event> {

    abstract fun execute()

    override fun compareTo(other: Event) = occurrenceTime.compareTo(other.occurrenceTime)

    override fun toString() = "${this.javaClass.simpleName} $occurrenceTime"

}

internal class Tick<S:State>(occurrenceTime: Double, private val core: SimCore<S>) : Event(occurrenceTime) {

    override fun execute() = with(core) {
        if (isWatched()) {
            Thread.sleep(sleepTime)
            planTick()
        }
    }

}