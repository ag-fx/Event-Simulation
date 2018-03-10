package Core

interface State {
   val running : Boolean
   val executedEvents: List<Event>
}