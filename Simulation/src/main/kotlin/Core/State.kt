package Core

interface State {
    val running: Boolean
    val events:  MutableCollection<Event>
}