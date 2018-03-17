package TestSim

import Core.Event
import Core.Simulation
import Core.State


data class MyTestState(
        val time: Double,
        override val running: Boolean,
        override val events: MutableCollection<Event>
) : State

//class MyTestSimulation : Simulation<MyTestState>() {
//
//    init {
//        plan(Tick(speed))
//        plan(MyTestEventOne())
//    }
//
//    override fun toState(simTime: Double, events: MutableCollection<Event>) = MyTestState(
//            time = simTime,
//            running = true,
//            events = events)
//
//}
//

class MyTestEventOne : Event(occurrenceTime = 5.0) {

    override fun execute(simulation: Simulation<State>) {
        simulation.plan(TestEventTwo())
    }
}

class TestEventTwo : Event(occurrenceTime = 10.5) {

    override fun execute(simulation: Simulation<State>) {
        simulation.plan(TestEventThree())
    }
}

class TestEventThree : Event(occurrenceTime = 13.0) {

    override fun execute(simulation: Simulation<State>) {
        simulation.plan(TestEventFour())
    }

}

class TestEventFour : Event(occurrenceTime = 18.1) {

    override fun execute(simulation: Simulation<State>) {
//        simulation.plan(Tick(simulation.currentTime + simulation.speed))
    }

}

class Tick(occurrenceTime: Double) : Event(occurrenceTime) {
    override fun execute(simulation: Simulation<State>) {
        Thread.sleep(simulation.sleepTime)
        simulation.plan(Tick(simulation.currentTime + simulation.speed))
        if(simulation.currentTime > 11)
            simulation.sleepTime = 2000L
    }

}
