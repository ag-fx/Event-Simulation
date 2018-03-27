package application.view.converter

import application.model.AirCarRentalStateModel
import javafx.util.StringConverter
import java.text.DecimalFormat

open class SimTimeConverter : StringConverter<AirCarRentalStateModel>() {
    private val format = DecimalFormat("0.00")
    override fun toString(`object`: AirCarRentalStateModel?) = `object`?.let { format.format(it.item.currentTime) }
        ?: "err"

    override fun fromString(string: String?): AirCarRentalStateModel? = null
}