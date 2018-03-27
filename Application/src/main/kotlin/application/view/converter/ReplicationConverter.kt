package application.view.converter

import application.model.AirCarRentalStateModel

class ReplicationConverter : SimModelConverter() {
    override fun toString(`object`: AirCarRentalStateModel?) = `object`?.item?.let {
        it.replicationNumber.toString()
    } ?: (-1).toString()

    override fun fromString(string: String?): AirCarRentalStateModel {
        TODO("not implemented")
    }

}