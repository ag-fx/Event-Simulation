package application.view

import javafx.util.StringConverter
import java.text.DecimalFormat

class SecondsToMinutesConverter : StringConverter<Double>() {
    val formatter = DecimalFormat("0.000")
    override fun toString(`object`: Double?): String {
        return if (`object` != null) {
            formatter.format(`object`/60)
        } else
            "0"
    }

    override fun fromString(string: String?) = string?.toDouble() ?: 0.0

}