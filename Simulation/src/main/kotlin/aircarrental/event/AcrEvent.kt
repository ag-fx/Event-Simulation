package aircarrental.event

import aircarrental.AirCarRentalSimulation
import core.Event

abstract class AcrEvent(time: Double) : Event(time) {

    lateinit var core: AirCarRentalSimulation

}