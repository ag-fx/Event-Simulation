package application.model

import aircarrental.entities.Minibus
import javafx.beans.property.SimpleDoubleProperty
import tornadofx.*

class MinibusModel(curretnTime:Double, minibus: Minibus) : ItemViewModel<Minibus>(minibus) {
    val id = bind(Minibus::id)
    val capacity = bind(Minibus::capacity)
    val averageSpeed = bind(Minibus::averageSpeed)
    val seats =  minibus.seats.toList().observable() //bind(Minibus::seats)
    val destination = bind(Minibus::destination)
    val source = bind(Minibus::source)
    val leftAt = bind(Minibus::leftAt)
    val isInSource = bind(Minibus::isInSource)
    val distanceFromSource = SimpleDoubleProperty(minibus.distanceFromSource(curretnTime))
}
