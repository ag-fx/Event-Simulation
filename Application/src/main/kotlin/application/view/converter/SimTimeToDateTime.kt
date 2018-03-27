package application.view.converter

import application.model.AirCarRentalStateModel
import javafx.util.StringConverter

abstract class SimModelConverter :StringConverter<AirCarRentalStateModel>()

class SimTimeToRealTimeConverter : SimTimeConverter() {
    private val time = MyTime()
    override fun toString(`object`: AirCarRentalStateModel?) = `object`?.item?.let {
        time.actualTime = it.currentTime
        time.toString()
    } ?: "err"

    override fun fromString(string: String?): AirCarRentalStateModel? = null
}

class MyTime {
    var actualTime: Double = 0.0
        set(value) {
            field = value
            calculateTime(value)
        }

    private var d: Int = 0
    private var h: Int = 0
    private var m: Int = 0
    private var s: Double = 0.toDouble()
    private val lengthOfDayHours = 24.0

    private fun calculateTime(actualTime: Double) {
        s = 0.0
        m = 0
        h = 0
        d = 0

        h = Math.floor(actualTime / 3600 % this.lengthOfDayHours).toInt()

        m = Math.floor((actualTime / 3600 % lengthOfDayHours - h) * 60).toInt()
        s = actualTime % 60

        d = Math.floor(actualTime / 3600 / this.lengthOfDayHours).toInt()

    }

    override fun toString() =
        if (d > 0) "Deň $d" + " " + h + ":" + m + ":" + String.format("%.0f", s) + " sec" else "" + h + ":" + m + ":" + String.format("%.0f", s) + " sec"


}