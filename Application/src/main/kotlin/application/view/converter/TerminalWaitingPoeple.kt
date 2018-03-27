package application.view.converter

import aircarrental.AirCarRentalState
import aircarrental.entities.Employee
import application.model.AirCarRentalStateModel
import java.text.DecimalFormat

class TerminalOneWaitingPeople : SimModelConverter() {

    override fun toString(`object`: AirCarRentalStateModel?) = `object`?.item?.terminal1Queue?.size?.toString() ?: "0"

    override fun fromString(string: String?) = throw NotImplementedError("netreba")

}

class TerminalTwoWaitingPeople : SimModelConverter() {

    override fun toString(`object`: AirCarRentalStateModel?) = `object`?.item?.terminal2Queue?.size?.toString() ?: "0"

    override fun fromString(string: String?) = throw NotImplementedError("netreba")

}

class AirCarRentalWaitingPoeple : SimModelConverter() {

    override fun toString(`object`: AirCarRentalStateModel?) = `object`?.item?.carRentalQueue?.size?.toString() ?: "0"

    override fun fromString(string: String?) = throw NotImplementedError("netreba")

}

class AirCarRentalFreeEmployee : SimModelConverter() {

    override fun toString(`object`: AirCarRentalStateModel?) = `object`?.item?.employees?.filter(Employee::isNotBusy)?.size?.toString()
        ?: "0"

    override fun fromString(string: String?) = throw NotImplementedError("netreba")

}

val decimalFormat = DecimalFormat("0.000")

class XConverter(private val f: (AirCarRentalState?) -> String) : SimModelConverter() {

    override fun toString(`object`: AirCarRentalStateModel?) = f(`object`?.item)

    override fun fromString(string: String?) = throw NotImplementedError("netreba")

}