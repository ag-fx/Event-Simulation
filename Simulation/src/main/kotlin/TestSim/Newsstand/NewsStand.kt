package TestSim.Newsstand

 import Core.*
 import XRandom.ExponentialRandom

data class Customer(override var arrivedToSystem: Double = 0.0) : Statistical

data class NewsStandState(
        val avgWaitTime: Double,
        val avgQueueSize: Double,
        override val running: Boolean,
        override val currentTime: Double,
        override val run: Int,
        override val stopped: Boolean
) : State


class NewsstandReplication : Replication<NewsStandState>(maxSimTime = 10_000_000.0) {

    private val costumerArrivalLambda = 10.0 / 60.0
    private val costumerServiceLambda = 1.00 / 5.00

    val rndArrival      = ExponentialRandom(costumerArrivalLambda, rndSeed.nextLong())
    val costumerService = ExponentialRandom(costumerServiceLambda, rndSeed.nextLong())

    val queue = StatisticQueue<Customer, NewsStandState>(this)
    var isFree = true

    override fun afterReplication() {
    }

    override fun beforeReplication() {
        plan((CostumerArrival(rndArrival.next())))
    }

    override fun toState(run:Int,simTime: Double) = NewsStandState(
            avgWaitTime = queue.averageWaitTime(),
            running = true,
            avgQueueSize = queue.averageSize(),
            currentTime = currentTime,
            run = run,
            stopped = stop
    )

}