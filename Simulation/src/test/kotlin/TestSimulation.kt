package test

import Core.Event
import Core.Simulation
import Core.State

data class TestState(val time: Double, override val running: Boolean,  override val events: MutableCollection<Event>) : State

class TestSimulation : Simulation<TestState>(maxSimTime = 10_000.0) {
    override fun toState(simTime: Double, events: MutableCollection<Event>) = TestState(
            time = simTime,
            running = true,
            events = events)

}
