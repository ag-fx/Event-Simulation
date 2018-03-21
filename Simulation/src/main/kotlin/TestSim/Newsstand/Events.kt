package TestSim.Newsstand

import core.Event

abstract class NewsstandEvent(time: Double) : Event(time) {
    lateinit var core: NewsstandSimulation
}


class CostumerArrival(arrivalTime: Double) : NewsstandEvent(arrivalTime) {

    override fun execute() = with(core) {
        val nextArrival = CostumerArrival(currentTime + rndArrival.next())
        plan(nextArrival)
        val customer = Customer(arrivedToSystem = currentTime)
        queue.push(customer)
        plan(StartService(currentTime))
    }

}

class StartService(startTime: Double) : NewsstandEvent(startTime) {

    override fun execute() = with(core) {
        if (isFree && queue.isNotEmpty()) {
            queue.pop()
            plan(CostumerServiceEnded(currentTime + costumerService.next()))
            isFree = false
        }
    }

}

class CostumerServiceEnded(endTime: Double) : NewsstandEvent(endTime) {

    override fun execute() = with(core) {
        isFree = true
        plan(StartService(currentTime))
    }

}