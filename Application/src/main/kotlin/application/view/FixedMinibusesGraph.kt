package application.view

import application.controller.FixedEmployeeController
import com.sun.jmx.remote.util.EnvHelp
import javafx.scene.chart.NumberAxis
import tornadofx.*

class FixedMinibusesView : View("Závislosť na počte mikrobusov") {

    val controller : FixedEmployeeController by inject()
    private val x = NumberAxis()
    private val y = NumberAxis()
    val graphName = "Závislosť priemerného času stráveného zákazníkom\nna zapožičanie vozidla na počte mikrobusov."
    override val root = linechart<Number,Number>(graphName,x,y){
        series("Poc",controller.data)
    }
}
