package core

abstract class Event(var occurrenceTime: Double) : Comparable<Event> {

    abstract fun execute()

    override fun compareTo(other: Event) = occurrenceTime.compareTo(other.occurrenceTime)

    override fun toString() = "${this.javaClass.simpleName} $occurrenceTime"

}

internal class Tick<S:State>(occurrenceTime: Double) : Event(occurrenceTime){
    lateinit var core: SimCore<S>

    override fun execute() = with(core) {
        if (isWatched()) {
            Thread.sleep(sleepTime)
            planTick()
        }
    }

}