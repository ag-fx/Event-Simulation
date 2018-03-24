import aircarrental.AirCarConfig
import aircarrental.AirCarRentalSimulation
import io.kotlintest.specs.StringSpec
import kotlinx.coroutines.experimental.runBlocking

class SimulationTests : StringSpec() {
    init {
        val configuration = AirCarConfig(numberOfEmployees = 20, numberOfMinibuses = 5)
        val sim = AirCarRentalSimulation(configuration, 60 * 60.0 * 24, 1)

        "AirCarRental to terminal one time check" {
            runBlocking {

            }
        }
    }
}