package core

interface State {
    val running: Boolean
    val stopped: Boolean
    val currentTime:Double
    val run :Int
}