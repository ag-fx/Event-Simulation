package TestSim.Newsstand

import Core.Event
import Core.Simulation
import Core.State


class CostumerArrival(arrivalTime: Double) : Event(arrivalTime) {

    override fun execute(simulation: Simulation<State>) = with(simulation as NewsstandSimulation) {
        val nextArrival = CostumerArrival(currentTime + rndArrival.next())
        plan(nextArrival)
        val customer = Customer(0.0, arrivedToSystem = currentTime)
        queue.push(customer)
        plan(StartService(currentTime))
    }

}

class StartService(startTime: Double) : Event(startTime) {

    override fun execute(simulation: Simulation<State>) = with(simulation as NewsstandSimulation) {
        if (isFree && queue.isNotEmpty()) {
            queue.pop()
            plan(CostumerServiceEnded(currentTime + costumerService.next()))
            isFree = false
        }
    }

}

class CostumerServiceEnded(endTime: Double) : Event(endTime) {

    override fun execute(simulation: Simulation<State>) = with(simulation as NewsstandSimulation) {
        isFree = true
        plan(StartService(currentTime))
    }

}