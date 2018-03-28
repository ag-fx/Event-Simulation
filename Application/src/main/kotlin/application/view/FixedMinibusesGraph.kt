package application.view

import application.controller.VariableMinibusFixedEmployeeController
import javafx.scene.chart.NumberAxis
import tornadofx.*

class FixedEmployeeView : View("Závislosť na počte mikrobusov") {

    val controller: VariableMinibusFixedEmployeeController by inject()
    private val x = NumberAxis()
    private val y = NumberAxis()


    val graphName = "Závislosť priemerného času stráveného zákazníkom\nna zapožičanie vozidla na počte mikrobusov."
    override val root = borderpane {
        top = vbox {
            hbox {
                spacer()
                button("Start") { action { clearData();controller.start() } }
                spacer()
                button("Stop") { action { controller.stop() } }
                spacer()
                button("Pause") { action { controller.pause() } }
                spacer()
                button("Resume") { action { controller.resume() } }
                spacer()
            }
            spacer()
            hbox {
                spacer()
                label("Pocet pracovnikov ")
                choicebox(values = (1..20).toList()) {
                    valueProperty().bindBidirectional(controller.maxNumberOfEmployeesProperty)
                }
                spacer()
                label("Min Pocet minibusov ")
                choicebox(values = (1..30).toList()) {
                    valueProperty().bindBidirectional(controller.minNumberOfMinibusesProperty)
                }
                label("Max Pocet minibusov ")
                choicebox(values = (2..30).toList()) {
                    valueProperty().bindBidirectional(controller.maxNumberOfMinibusesProperty)
                }
                spacer()

                label("Pocet replikacii")
                choicebox(values = listOf(10, 25, 50, 100, 150, 200, 500)) {
                    valueProperty().bindBidirectional(controller.numberOfReplicationProperty)
                }
                spacer()

                label("Pocet dni")
                choicebox(values = listOf(1, 2, 5, 7, 14, 30).toList()) {
                    valueProperty().bindBidirectional(controller.numberOfDaysProperty)
                }
                spacer()


            }
        }
        center =
            linechart<Number, Number>("", x, y) {
                series(" ", controller.data)
//                series("20 minut", controller.line)
                createSymbols = true
                with(xAxis as NumberAxis) {
                    isForceZeroInRange = false
                    isAutoRanging = true
                    lowerBoundProperty().bindBidirectional(controller.lowerBoundProperty)
                    upperBoundProperty().bindBidirectional(controller.upperBoundProperty)
                }
                with(yAxis as NumberAxis) {
                    isForceZeroInRange = false
                    isAutoRanging = true

                }

            }


    }

    private fun clearData() {
    }
}
