package aircarrental.entities

import core.Statistical

data class Customer(
        val id:Int,
        val terminal:Buildings,
        var rentCarWaitingTime:Double = 0.0,
        var getOnBusTime:Double = 0.0,
        var getFromBusTime:Double = 0.0,
        override var arrivedToSystem: Double
) : Statistical{
    override fun toString() = "Customer $id from $terminal"
}