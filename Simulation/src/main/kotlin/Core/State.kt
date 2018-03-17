package Core

interface State {
    val running: Boolean
    val currentTime:Double
    val run :Int
}