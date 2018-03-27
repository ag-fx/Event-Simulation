package application.view

import application.controller.VariableEmployeeFixedMinibusController
import javafx.scene.chart.NumberAxis
import tornadofx.*

class FixedMinibusView(val name: String = "Závislosť na počte pracovníkov") : View(name) {

    private val x = NumberAxis()
    private val y = NumberAxis()
    private val controller: VariableEmployeeFixedMinibusController by inject()

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
                label("Pocet mikrobusov ")
                choicebox(values = (3..20).toList()) {
                    valueProperty().bindBidirectional(controller.minNumberOfMinibusesProperty)
                }
                spacer()
                label("Min Pocet pracovnikov ")
                choicebox(values = (10..30).toList()) {
                    valueProperty().bindBidirectional(controller.minNumberOfEmployeesProperty)
                }
                label("Max Pocet pracovnikov ")
                choicebox(values = (11..30).toList()) {
                    valueProperty().bindBidirectional(controller.maxNumberOfEmployeesProperty)
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
        center = linechart<Number, Number>("", x, y) {
            series(" ", controller.data)
            series("20 minut", controller.line)

            createSymbols = false
            with(xAxis as NumberAxis) {
                isForceZeroInRange = false
                isAutoRanging= true
            }
            with(yAxis as NumberAxis) {
                isForceZeroInRange = false
                isAutoRanging = true
            }
        }

    }

    private fun clearData() {
        controller.data.clear()

    }

}
