package test

import Core.Event
import Core.Simulation
import Core.State

data class TestState(val time: Double, override val running: Boolean, override val executedEvents: List<Event>) : State

class TestSimulation : Simulation<TestState>(maxSimTime = 10_000.0) {

    override fun toState(simTime: Double, executedEvents: List<Event>) = TestState(simTime, true, executedEvents)

}
