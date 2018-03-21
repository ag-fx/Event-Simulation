package aircarrental

import XRandom.ExponentialRandom
import XRandom.IntPlusMinus
import aircarrental.entities.*
import aircarrental.event.AcrEvent
import aircarrental.event.MinibusGoTo
import aircarrental.event.TerminalOneCustomerArrival
import aircarrental.event.TerminalTwoCustomerArrival
import core.Event
import core.SimCore
import core.State
import core.StatisticQueue
import kotlinx.coroutines.experimental.channels.consumeEach
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.runBlocking

data class AirCarRentalState(
        val minibuses: List<Minibus>,
        override val running: Boolean,
        override val stopped: Boolean,
        override val currentTime: Double,
        override val run: Int
) : State

data class AirCarConfig(
        val numberOfMinibuses: Int,
        val numberOfEmployees: Int
)

class AirCarRentalSimulation(
        conf: AirCarConfig,
        maxSimTime: Double,
        numberOfReplication: Int
) : SimCore<AirCarRentalState>(maxSimTime, numberOfReplication) {

    override var speed = 1.0

    private fun resetSimulation() {
        terminalOne.queue.clear()
        terminalTwo.queue.clear()
        carRental.queue.clear()
        carRental.employees.forEach { it.isBusy = false }
        minibuses.forEach {
            it.seats.clear()
            //   it.destination = Buildings.TerminalOne
            //   it.distanceToDestination = Buildings.aircarrental.distanceToNext()
            //   it.leftAt = 0.0
        }

    }

    override fun plan(event: Event) {
        val simEvent = (event as AcrEvent).apply { core = this@AirCarRentalSimulation }
        super.plan(simEvent)
    }

    override fun afterReplication() {

    }

    override fun beforeReplication() {
        clear()
        minibuses.forEach {
            plan(MinibusGoTo(
                    source = it.source,
                    destination = it.destination,
                    minibus = it,
                    time = currentTime)
            )
        }

        val terminalOneArrival = TerminalOneCustomerArrival(currentTime + rndArrivalTerminalOne.next())
        val terminalTwoArrival = TerminalTwoCustomerArrival(currentTime + rndArrivalTerminalTwo.next())
        plan(terminalOneArrival)
        plan(terminalTwoArrival)

    }

    override fun afterSimulation() {
    }

    override fun beforeSimulation() {
    }

    override fun toState(run: Int, simTime: Double) = AirCarRentalState(
            running = isRunning,
            stopped = stop,
            minibuses = minibuses,
            currentTime = simTime,
            run = run
    )


    val minibuses = List(conf.numberOfMinibuses) {
        Minibus(
                id = it,
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
            queue = StatisticQueue(this),
            employees = List(conf.numberOfEmployees) { Employee() }
    )

    /*
     * Prúd zákazníkov prilietajúcich na terminál 1 je poissonovský prúd s intenzitou z1 = 43 zákazníkov za hodinu.
     *  43/60      43 za hodinu
     *  43/(60*60) 43 za sekundu
     */
    val rndArrivalTerminalOne = ExponentialRandom(43.0 / (60.0 * 60.0), rndSeed.nextLong())

    /**
     * same as [rndArrivalTerminalOne] but with 19
     */
    val rndArrivalTerminalTwo = ExponentialRandom(19.0 / (60.0 * 60.0), rndSeed.nextLong())


    // Časová náročnosť základných operácií, ktoré je potrebné modelovať pomocou spojitého rovnomerného rozdelenia

    // Čas potrebný na obslúženie jedného zákazníka (zapožičanie vozidla): o = 6min ± 4min
    val rndTimeToOneCustomerService = IntPlusMinus(
            fixedNumber = 6 * 60,
            variableNumber = 4 * 60,
            rndSeedNumber = rndSeed.nextLong(),
            rndSeedPlusMinus = rndSeed.nextLong()
    )

    // b.) Doba nástupu cestujúceho je: p = 12s ± 2s
    val rndTimeToEnterBus = IntPlusMinus(
            fixedNumber = 12,
            variableNumber = 2,
            rndSeedNumber = rndSeed.nextLong(),
            rndSeedPlusMinus = rndSeed.nextLong()
    )

    // c.) Doba výstupu cestujúceho je: r = 8s ± 4s
    val rndTimeToExitBus = IntPlusMinus(
            fixedNumber = 8,
            variableNumber = 4,
            rndSeedNumber = rndSeed.nextLong(),
            rndSeedPlusMinus = rndSeed.nextLong()
    )
}

fun main(args: Array<String>) = runBlocking {
    val conf = AirCarConfig(numberOfEmployees = 2, numberOfMinibuses = 3)
    val sim = AirCarRentalSimulation(conf, 10_0000.0, 3)
    sim.start()
    sim.speed = 120.0
    sim.currentReplicationChannel.consumeEach { sim ->
        // println(sim.minibuses.map { it.distanceFromDestinatioN(sim.currentTime) })
        delay(500)
    }
}
