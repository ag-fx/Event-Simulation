import aircarrental.AirCarConfig
import aircarrental.AirCarRentalSimulation
import kotlinx.coroutines.experimental.channels.consumeEach
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.runBlocking

fun main(args: Array<String>) = runBlocking {

    val repcount = 100
//    val configuration = AirCarConfig(numberOfEmployees = 20, numberOfMinibuses = 5) to 20.1
    val configuration = AirCarConfig(numberOfEmployees = 1, numberOfMinibuses = 1) to 16595.1310
    val sim = AirCarRentalSimulation(configuration.first, 60.0 * 60 * 24 * 30, repcount)
    sim.speed = 1200.0
    sim.log = true
    sim.start()

    sim.currentReplicationChannel
        .consumeEach {
            val avgInSystem = it.totalCustomersTime
      //      println("$avgInSystem \t ${avgInSystem - configuration.second} \t $configuration")
        }

}

