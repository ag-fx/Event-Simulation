package TestSim.Newsstand

 import Core.*
 import XRandom.ExponentialRandom
import kotlinx.coroutines.experimental.channels.consumeEach
import kotlinx.coroutines.experimental.runBlocking
import java.util.*

var uid = 1

data class Customer(val waitingTime: Double = 0.0, override var arrivedToSystem: Double = 0.0, val id: Int = uid++) : Statistical


data class NewsStandState(
        val nvm: Double,
        val nvm2: Double,
        override val running: Boolean,
        override val events: MutableCollection<Event>
) : State


class NewsstandSimulation : Simulation<NewsStandState>(maxSimTime = 999999999.0) {

    // 10 zakaznikov za hodinu
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

    override fun toState(simTime: Double, lastEvent: MutableCollection<Event>) = NewsStandState(
            nvm = queue.averageWaitTime(), running = true, events = mutableListOf(), nvm2 = queue.averageSize()/*newsstandSuma / newsstandTotal*/
    )

}


fun main(args: Array<String>) = runBlocking {

    val sim = NewsstandSimulation()

    sim.start().consumeEach {
        println("${it.nvm}\t${it.nvm2}" )

    }

}

