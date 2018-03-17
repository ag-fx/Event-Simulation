package test

import TestSim.Newsstand.NewsstandSimulation
import kotlinx.coroutines.experimental.channels.consumeEach
import kotlinx.coroutines.experimental.runBlocking


fun main(args: Array<String>) = runBlocking {

    val sim = NewsstandSimulation()

    sim.start().consumeEach {
        println("${it.avgWaitTime}\t${it.avgQueueSize}" )
    }

}

