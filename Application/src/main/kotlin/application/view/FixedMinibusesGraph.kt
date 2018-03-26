package application.view

import application.controller.VariableMinibusFixedEmployeeController
import javafx.scene.chart.NumberAxis
import tornadofx.*

class FixedEmployeeView : View("Závislosť na počte mikrobusov") {

    val controller: VariableMinibusFixedEmployeeController by inject()
    private val x = NumberAxis(2.0, 30.0, 1.0)
    private val y = NumberAxis()

    val graphName = "Závislosť priemerného času stráveného zákazníkom\nna zapožičanie vozidla na počte mikrobusov."
    override val root = borderpane {
        top = hbox {
            spacer()
            button("Start") { action { controller.start() } }
            spacer()
            button("Stop")  { action { controller.stop() } }
            spacer()
            button("Pause") { action { controller.pause() } }
            spacer()
            button("Resume") { action { controller.resume() } }
            spacer()
        }
        center =
            linechart<Number, Number>(controller.graphName, x, y) {
                series(" ", controller.data)
                series("20 minut", controller.line)

                createSymbols = false
                with(xAxis as NumberAxis) {
                    isForceZeroInRange = false
                    isAutoRanging = true
                }
                with(yAxis as NumberAxis) {
                    isForceZeroInRange = false
                    isAutoRanging = true
                }
            }


    }
}
