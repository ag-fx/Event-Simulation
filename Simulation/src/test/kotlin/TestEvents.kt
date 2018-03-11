package test

import Core.Event
import Core.Simulation
import Core.State

class TestEventOne : Event(occurrenceTime = 1.00) {

    override fun execute(simulation: Simulation<State>) {
        simulation.plan(TestEventTwo())
    }
}

class TestEventTwo : Event(occurrenceTime = 11.00) {

    override fun execute(simulation: Simulation<State>) {
        simulation.plan(TestEventThree())
    }
}

class TestEventThree : Event(occurrenceTime = 11.10) {

    override fun execute(simulation: Simulation<State>) {
        simulation.plan(TestEventFour())
    }

}

class TestEventFour : Event(occurrenceTime = 15.10) {

    override fun execute(simulation: Simulation<State>) {
        simulation.plan(Tick(simulation.currentTime + simulation.speed))
    }

}

class Tick(occurrenceTime: Double) : Event(occurrenceTime) {
    override fun execute(simulation: Simulation<State>) {
        simulation.plan(Tick(simulation.currentTime + simulation.speed))
    }

}