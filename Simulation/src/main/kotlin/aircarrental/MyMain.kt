package aircarrental

import kotlinx.coroutines.experimental.channels.consumeEach
import kotlinx.coroutines.experimental.runBlocking

fun main(args: Array<String>) = runBlocking {
    val conf = AirCarConfig(numberOfEmployees = 20, numberOfMinibuses = 5)
    val sim = AirCarRentalSimulation(conf, 60.0 * 60 * 24 * 30, 30)
    sim.speed = 12000.0
    sim.stopWatching()
    sim.log = false
//    sim.log = true

    sim.start()
    sim.afterReplicationChannel.consumeEach {
        //delay(250)
        println(it.map { it.customersTimeInSystem/ 60 }.average() )
//        println(it.last().totalTerminal2)
//        println(it.last().totalTerminal2)
//        println(it.map { it.customersTimeInSystem /60}.average())
//        println("Avg que size ${it.avgQueueSizeTerminalOne}")
//        println("Avg que wait time ${it.avgQueueWaitTimeTerminalOne / 60}")
    }


}