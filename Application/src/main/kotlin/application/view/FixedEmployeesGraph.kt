package application.view

import application.controller.FixedEmployeeController
import application.controller.MyController
import javafx.scene.Parent
import javafx.scene.chart.NumberAxis
import tornadofx.*

class FixedEmployeeView : View("Závislosť na počte pracovníkov") {

    private val x = NumberAxis()
    private val y = NumberAxis()
    private val controller: FixedEmployeeController by inject()
    val graphName = "Závislosť priemerného času stráveného zákazníkom\nna zapožičanie vozidla na počte pracovníkov"
    override val root = vbox {
        hbox {
            spacer()
            button("Start") { action { controller.start() } }
            spacer()
        }
        linechart<Number, Number>(graphName, x, y)
    }

}
