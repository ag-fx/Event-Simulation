import aircarrental.AirCarConfig
import aircarrental.AirCarRentalSimulation
import kotlinx.coroutines.experimental.channels.consumeEach
import kotlinx.coroutines.experimental.channels.filterIndexed
import kotlinx.coroutines.experimental.runBlocking

fun main(args: Array<String>) = runBlocking {
    val configurations = listOf(
        AirCarConfig(numberOfEmployees = 20, numberOfMinibuses = 5) to 20.09
//        AirCarConfig(numberOfEmployees = 30, numberOfMinibuses = 30) to 18.5664
//        AirCarConfig(numberOfEmployees = 1, numberOfMinibuses = 2) to 18127.1370


//        AirCarConfig(numberOfEmployees = 29, numberOfMinibuses = 29) to 18.6024
//        AirCarConfig(numberOfEmployees = 6, numberOfMinibuses = 30) to 740.0,
//        AirCarConfig(numberOfEmployees = 1, numberOfMinibuses = 1) to 16595.1310
//       AirCarConfig(numberOfEmployees = 29, numberOfMinibuses = 28) to 18.6404,
        //       AirCarConfig(numberOfEmployees = 1, numberOfMinibuses = 10) to 18126.2936,
//        AirCarConfig(numberOfEmployees = 1, numberOfMinibuses = 30) to 18127.3547
//        AirCarConfig(numberOfEmployees = 30, numberOfMinibuses = 1) to 3645.3335
//1	30	3645,3335

    )
    val repcount = 500
    configurations.forEach { configuration ->
        val sim = AirCarRentalSimulation(configuration.first, 60 * 60.0 * 24 * 30, repcount)
        sim.speed = 1200.0
        sim.stopWatching()
        sim.log = false
        sim.start()

        sim.afterReplicationChannel
            .filterIndexed { index, list -> index % 100 == 0 }
            .consumeEach {
                val avgInSystem = it.map { it.customersTimeInSystem / 60 }.average()
                println("$avgInSystem \t ${avgInSystem - configuration.second} \t $configuration")
            }

    }

}