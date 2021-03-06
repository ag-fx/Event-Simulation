package aircarrental

import XRandom.ExponentialRandom
import XRandom.RandomRange
import aircarrental.entities.*
import aircarrental.event.*
import core.*
import kotlin.math.pow
import kotlin.math.sqrt

class TimeInSystem : Stat()

class AirCarRentalSimulation(
    val conf: AirCarConfig,
    maxSimTime: Double = 60 * 60 * 24 * 30.0,
    val numberOfReplication: Int = 100
) : SimCore<AirCarRentalState>(maxSimTime, numberOfReplication) {

    //region entities
    private val minibuses = List(conf.numberOfMinibuses) {
        Minibus(
            id = it + 1,
            source = Buildings.AirCarRental,
            destination = Buildings.TerminalTwo,
            distanceToDestination = Buildings.AirCarRental.distanceToNext(),
            leftAt = 0.0,
            seats = StatisticQueue(this@AirCarRentalSimulation)
        )
    }

    val terminalOne = Terminal(
        description = Buildings.TerminalOne,
        busCount = 0,
        queue = StatisticQueue(this)
    )

    val terminalTwo = Terminal(
        description = Buildings.TerminalTwo,
        busCount = 0,
        queue = StatisticQueue(this)
    )

    val carRental = CarRental(
        description = Buildings.AirCarRental,
        queue = StatisticPriorityQueue(this),
        employees = List(conf.numberOfEmployees) { Employee(1 + it) }
    )
    //endregion

    val avgTimeInSystemTEST = TimeInSystem()

    var totalCustomersTime = 0.0
    var numberOfServedCustomers = 0.0

    override var warmUpSeconds = 60 * 60 * 24.0

    var statistics: Statistics = Statistics()
    val ppl = StatisticQueue<Customer, AirCarRentalState>(this)

    override fun plan(event: Event) {
        val simEvent = (event as AcrEvent).apply { core = this@AirCarRentalSimulation }
        super.plan(simEvent)
    }

    override fun beforeReplication() {
        clear()
        minibuses.forEach {
            plan(MinibusGoTo(
                source = Buildings.AirCarRental,
                destination = Buildings.TerminalOne,
                minibus = it,
                time = 0.0
            ))
        }
        val terminalOneArrival = TerminalOneCustomerArrival(currentTime)
        val terminalTwoArrival = TerminalTwoCustomerArrival(currentTime)
        plan(terminalOneArrival)
        plan(terminalTwoArrival)
    }

    override fun coolDownEventFilter(event: Event) = when (event) {
        is TerminalOneCustomerArrival -> false
        is TerminalTwoCustomerArrival -> false
        is MinibusGoTo -> terminalOne.queue.isNotEmpty() || terminalTwo.queue.isNotEmpty() || event.minibus.isNotEmpty()
        else -> true
    }

    override fun beforeSimulation() {
    }

    override fun  afterWarmUp() {
        avgTimeInSystemTEST.clear()
        terminalOne.queue.clearStat()
        terminalTwo.queue.clearStat()
        carRental.queue.clearStat()
    }

    var totalwaittime = 0.0
    var sumaxinadruhu = 0.0
    var sumaxi = 0.0
    override fun afterReplication(replicationNumber: Int) {
        terminalOne.queue.fixTimeAtEndOfSimulation(currentTime)
        terminalTwo.queue.fixTimeAtEndOfSimulation(currentTime)
        carRental.queue.fixTimeAtEndOfSimulation(currentTime)

        statistics.avgTimeInSystem.add(avgTimeInSystemTEST.average())

        totalwaittime += avgTimeInSystemTEST.average()
        sumaxinadruhu += avgTimeInSystemTEST.average().pow(2)
        sumaxi += avgTimeInSystemTEST.average()


        val xavg = totalwaittime / replicationNumber
        val s = 1.645 * sqrt(
            sumaxinadruhu / replicationNumber - (sumaxi / replicationNumber).pow(2)
        )

        val l = (xavg - s / sqrt(replicationNumber - 1.0))
        val r = (xavg + s / sqrt(replicationNumber - 1.0))

        with(statistics) {
            interval.add(l to r)
            avgWaitTimeTerminal1.add(terminalOne.queue.averageWaitTime())
            avgWaitTimeTerminal2.add(terminalTwo.queue.averageWaitTime())

            avgQueueSizeTerminal1.add(terminalOne.queue.averageSize())
            avgQueueSizeTerminal2.add(terminalTwo.queue.averageSize())

            avgWaitTimeAirCarRental.add(carRental.avgWaitTimeForService())
            avgQueueSizeAirCarRental.add(carRental.queue.averageSize())
        }

        avgTimeInSystemTEST.clear()
    }

    override fun clear() {
        super.clear()
        terminalOne.queue.clear()
        terminalOne.arrivals = 0
        terminalTwo.queue.clear()
        terminalTwo.arrivals = 0
        carRental.queue.clear()
        carRental.employees.forEach { it.isBusy = false }
        carRental.served = 0
        carRental.serviceTotalWaitTime = 0.0

        minibuses.forEach {
            val source = Buildings.AirCarRental
            it.source = source
            it.destination = source.nextStop()
            it.leftAt = 0.0
            it.seats.clear()
        }

        totalCustomersTime = 0.0
        numberOfServedCustomers = 0.0
    }

    override fun afterSimulation() {
        super.afterSimulation()
        totalwaittime = 0.0
        sumaxinadruhu = 0.0
        sumaxi = 0.0
    }

    //region generators

    // Prúd zákazníkov prilietajúcich na terminál 1 je poissonovský prúd s intenzitou z1 = 43 zákazníkov za hodinu.
    val rndArrivalTerminalOne = ExponentialRandom((60.0 * 60.0) / 43.0, rndSeed.nextLong())

    // Prúd zákazníkov prilietajúcich na terminál 2 je poissonovský prúd s intenzitou z1 = 19 zákazníkov za hodinu.
    val rndArrivalTerminalTwo = ExponentialRandom((60.0 * 60.0) / 19.0, rndSeed.nextLong())

    // Časová náročnosť základných operácií, ktoré je potrebné modelovať pomocou spojitého rovnomerného rozdelenia
    // Čas potrebný na obslúženie jedného zákazníka (zapožičanie vozidla): o = 6min ± 4min
    val rndTimeToOneCustomerService = RandomRange(
        min = (6.0 - 4.0) * 60.0,
        max = (6.0 + 4.0) * 60.0,
        rndSeedNumber = rndSeed.nextLong()
    )

    // Doba nástupu cestujúceho je: p = 12s ± 2s
    val rndTimeToEnterBus = RandomRange(
        min = (12.0 - 2.0),
        max = (12.0 + 2.0),
        rndSeedNumber = rndSeed.nextLong()
    )

    // Doba výstupu cestujúceho je: r = 8s ± 4s
    val rndTimeToExitBus = RandomRange(
        min = (8.0 - 4.0),
        max = (8.0 + 4.0),
        rndSeedNumber = rndSeed.nextLong()
    )
    //endregion

    override fun toState(replication: Int, simTime: Double) = AirCarRentalState(
        statistics = statistics,
        avgQueueWaitTimeTerminalOne = terminalOne.queue.averageWaitTime(),
        avgQueueWaitTimeTerminalTwo = terminalTwo.queue.averageWaitTime(),
        avgQueueWaitTimeCarRental = carRental.avgWaitTimeForService(),

        avgQueueSizeTerminalOne = terminalOne.queue.averageSize(),
        avgQueueSizeTerminalTwo = terminalTwo.queue.averageSize(),
        avgQueueSizeCarRental = carRental.queue.averageSize(),

        terminal1Queue = terminalOne.queue.toList(),
        terminal2Queue = terminalTwo.queue.toList(),
        carRentalQueue = carRental.queue.toList(),

        employees = carRental.employees.toList(),
        averageTimeOfCustomerInSystem = totalCustomersTime / numberOfServedCustomers,
        totalCustomersTime = totalCustomersTime,
        numberOfServedCustomers = numberOfServedCustomers,
        running = isRunning,
        stopped = stop,
        minibuses = minibuses,
        currentTime = simTime,
        totalTerminal1 = terminalOne.arrivals,
        totalTerminal2 = terminalTwo.arrivals,
        run = replication,
        config = conf,
        replicationNumber = replication,
        interval = .0 to .0
    )
}
