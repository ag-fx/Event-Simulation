package aircarrental.entities

import core.Statistical

data class Customer(
        var rentCarWaitingTime:Double = 0.0,
        var getOnBusTime:Double = 0.0,
        var getFromBusTime:Double = 0.0,
        override var arrivedToSystem: Double
) : Statistical