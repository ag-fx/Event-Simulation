package TestSim.Newsstand

import Core.Event
import Core.Replication
import Core.State


class CostumerArrival(arrivalTime: Double) : Event(arrivalTime) {

    override fun execute(replication: Replication<State>) = with(replication as NewsstandReplication) {
        val nextArrival = CostumerArrival(currentTime + rndArrival.next())
        plan(nextArrival)
        val customer = Customer(arrivedToSystem = currentTime)
        queue.push(customer)
        plan(StartService(currentTime))
    }

}

class StartService(startTime: Double) : Event(startTime) {

    override fun execute(replication: Replication<State>) = with(replication as NewsstandReplication) {
        if (isFree && queue.isNotEmpty()) {
            queue.pop()
            plan(CostumerServiceEnded(currentTime + costumerService.next()))
            isFree = false
        }
    }

}

class CostumerServiceEnded(endTime: Double) : Event(endTime) {

    override fun execute(replication: Replication<State>) = with(replication as NewsstandReplication) {
        isFree = true
        plan(StartService(currentTime))
    }

}