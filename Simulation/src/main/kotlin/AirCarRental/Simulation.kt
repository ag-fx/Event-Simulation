package AirCarRental

//data class SimTate(val avgWaitTime: Long = 5, override var running: Boolean) : State

//class AirCarRentalSimulation(
//        private val numberOFMiniBuses: Int,
//        private val employees: Int,
//        maxSimTime: Long
//) : Replication<SimTate>(maxSimTime) {
//
//    private val rndService    = Random(rndSeed.nextLong())
//
//    private val rndGetOnBus   = Random(rndSeed.nextLong())
//
//    private val rndGetFromBus = Random(rndSeed.nextLong())
//
//    val busses = Collections.nCopies(numberOFMiniBuses,MiniBus(location = Building.TerminalOne))
//
//
//    override fun toState(simTime: Long) = SimTate(avgWaitTime = simTime, running = isRunning)
//
//}