import aircarrental.entities.Buildings
import aircarrental.entities.distanceToNext
import io.kotlintest.matchers.shouldBe
import io.kotlintest.specs.StringSpec

class ConversionTests:StringSpec(){
    init {
        "AirCar Rental -> Terminál 1"{
            Buildings.AirCarRental.distanceToNext() shouldBe 6400.0
        }

        "Terminál 1 -> Terminál 2"{
            Buildings.TerminalOne.distanceToNext() shouldBe 500.0
        }

        "Terminál 2 -> AirCar Rental"{
            Buildings.TerminalTwo.distanceToNext() shouldBe 2500.0
        }

    }
}