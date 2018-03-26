package application.view

import application.controller.VariableEmployeeFixedMinibusController
import javafx.scene.chart.NumberAxis
import tornadofx.*

class FixedMinibusView : View("Závislosť na počte pracovníkov") {

    private val x = NumberAxis(2.0,30.0,1.0)
    private val y = NumberAxis()
    private val controller: VariableEmployeeFixedMinibusController by inject()

    override val root = vbox {
        hbox {
            spacer()
            button("Start") { action { controller.start() } }
            spacer()
        }
        spacer()
        linechart<Number, Number>(controller.graphName, x, y){
            series(" ",controller.data)
            series("20 minut", controller.line)

            createSymbols = false
            with(xAxis as NumberAxis){
                isForceZeroInRange = false
            }
            with(yAxis as NumberAxis){
                isForceZeroInRange = false
                isAutoRanging = true
            }
        }
        spacer()
    }

}
