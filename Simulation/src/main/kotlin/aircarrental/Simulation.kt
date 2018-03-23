package aircarrental

import XRandom.ExponentialRandom
import XRandom.RandomRange
import aircarrental.entities.*
import aircarrental.event.AcrEvent
import aircarrental.event.MinibusGoTo
import aircarrental.event.TerminalOneCustomerArrival
import aircarrental.event.TerminalTwoCustomerArrival
import core.Event
import core.SimCore
import core.StatisticQueue
import core.StatisticalPriorityQueue


class AirCarRentalSimulation(
    conf: AirCarConfig,
    maxSimTime: Double,
    numberOfReplication: Int
) : SimCore<AirCarRentalState>(maxSimTime, numberOfReplication) {

    //region entities
    private val minibuses = List(conf.numberOfMinibuses) {
        Minibus(
            id = it + 1,
            destination = Buildings.TerminalOne,
            source = Buildings.AirCarRental,
            distanceToDestination = Buildings.AirCarRental.distanceToNext(),
            leftAt = 0.0,
            seats = StatisticQueue(this@AirCarRentalSimulation)
        )
    }

    val terminalOne = Terminal(
        description = Buildings.TerminalOne,
        queue = StatisticQueue(this)
    )

    val terminalTwo = Terminal(
        description = Buildings.TerminalTwo,
        queue = StatisticQueue(this)
    )

    val carRental = CarRental(
        description = Buildings.AirCarRental,
        queue = StatisticalPriorityQueue(this),
        employees = List(conf.numberOfEmployees) { Employee() }
    )
    //endregion

    //region generators
    /*
     * Prúd zákazníkov prilietajúcich na terminál 1 je poissonovský prúd s intenzitou z1 = 43 zákazníkov za hodinu.
     *  43/60      43 za hodinu
     *  43/(60*60) 43 za sekundu
     */
    val rndArrivalTerminalOne = ExponentialRandom(43.0 / (60.0 * 60.0), rndSeed.nextLong())

    /**
     * same as [rndArrivalTerminalOne] but with 19
     *
     */
    val rndArrivalTerminalTwo = ExponentialRandom(19.0 / (60.0 * 60.0), rndSeed.nextLong())

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

    override var speed = 1.0
    var totalCustomersTime = 0.0
    var numberOfServedCustomers = 0.0

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
                time = currentTime
            ))
        }

        val terminalOneArrival = TerminalOneCustomerArrival(currentTime + rndArrivalTerminalOne.next())
        val terminalTwoArrival = TerminalTwoCustomerArrival(currentTime + rndArrivalTerminalTwo.next())
        plan(terminalOneArrival)
        plan(terminalTwoArrival)

    }

    override fun afterSimulation() {
        super.afterSimulation()
    }

    override fun beforeSimulation() {
    }

    override fun afterReplication() {
    }

    override fun clear() {
        super.clear()
        terminalOne.queue.clear()
        terminalOne.arrivals = 0
        terminalTwo.queue.clear()
        terminalTwo.arrivals = 0
        carRental.queue.clear()
        carRental.employees.forEach { it.isBusy = false }
        minibuses.forEach {
            it.destination = Buildings.TerminalOne
            it.source = Buildings.AirCarRental
            it.leftAt = 0.0
            it.seats.clear()
        }
        totalCustomersTime = 0.0
        numberOfServedCustomers = 0.0
    }


    override fun toState(run: Int, simTime: Double) = AirCarRentalState(
        avgQueueWaitTimeTerminalOne = terminalOne.queue.averageWaitTime(),
        avgQueueWaitTimeTerminalTwo = terminalTwo.queue.averageWaitTime(),
        avgQueueSizeTerminalOne = terminalOne.queue.averageSize(),
        avgQueueSizeTerminalTwo = terminalTwo.queue.averageSize(),
        customersTimeInSystem = totalCustomersTime / numberOfServedCustomers,
        totalCustomersTime = totalCustomersTime,
        numberOfServedCustomers = numberOfServedCustomers,
        running = isRunning,
        stopped = stop,
        minibuses = minibuses,
        currentTime = simTime,
        totalTerminal1 = terminalOne.arrivals,
        totalTerminal2 = terminalTwo.arrivals,
        run = run
    )
}

