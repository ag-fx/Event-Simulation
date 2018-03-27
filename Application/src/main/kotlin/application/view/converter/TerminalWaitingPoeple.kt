package application.view.converter

import aircarrental.AirCarRentalState
import aircarrental.entities.Employee
import application.model.AirCarRentalStateModel
import java.text.DecimalFormat

class TerminalOneWaitingPoeple : SimModelConverter(){
    override fun toString(`object`: AirCarRentalStateModel?) = `object`?.item?.terminal1Queue?.size?.toString() ?: "0"

    override fun fromString(string: String?): AirCarRentalStateModel {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}

class TerminalTwoWaitingPoeple : SimModelConverter(){
    override fun toString(`object`: AirCarRentalStateModel?) = `object`?.item?.terminal2Queue?.size?.toString() ?: "0"

    override fun fromString(string: String?): AirCarRentalStateModel {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}

class AirCarRentalWaitingPoeple : SimModelConverter(){
    override fun toString(`object`: AirCarRentalStateModel?) = `object`?.item?.carRentalQueue?.size?.toString() ?: "0"

    override fun fromString(string: String?): AirCarRentalStateModel {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
class AirCarRentalFreeEmployee : SimModelConverter(){
    override fun toString(`object`: AirCarRentalStateModel?) = `object`?.item?.employees?.filter(Employee::isNotBusy)?.size?.toString() ?: "0"

    override fun fromString(string: String?): AirCarRentalStateModel {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
val decimalFormat = DecimalFormat("0.00")
class XConverter(private val f:(AirCarRentalState?)->String) : SimModelConverter(){
     val decimalFormat = DecimalFormat("0.00")

    override fun toString(`object`: AirCarRentalStateModel?) = f(`object`?.item)

    override fun fromString(string: String?): AirCarRentalStateModel {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}