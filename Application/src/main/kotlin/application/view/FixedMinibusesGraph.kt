package application.view

import application.controller.FixedMinibusController
import javafx.scene.chart.NumberAxis
import tornadofx.*

class FixedMinibusesView : View("Závislosť na počte mikrobusov") {

    val controller: FixedMinibusController by inject()
    private val x = NumberAxis()
    private val y = NumberAxis()
    val graphName = "Závislosť priemerného času stráveného zákazníkom\nna zapožičanie vozidla na počte mikrobusov."
    override val root = vbox {
        hbox {
            spacer()
            button("Start") { action { controller.start() } }
            spacer()
        }
        linechart<Number, Number>(graphName, x, y) {
            series("Poc", controller.data)
            createSymbols = false
        }
    }
}
