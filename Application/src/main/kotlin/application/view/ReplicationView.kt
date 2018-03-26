package application.view

import application.controller.MyController
import application.model.AirCarRentalStateModel
import javafx.scene.control.TabPane
import tornadofx.*


class ControlsView : View("Controls") {

    private val controller: MyController by inject()

    override val root = vbox {
        hbox {
            button("Pause") { action { controller.pause() } }
            spacer()
            button("Start") { action { controller.start() } }
            spacer()
            button("Resume") { action { controller.resume() } }
            spacer()
            button("Stop watching") { action { controller.stopWatching() } }
            spacer()
            button("Start watching") { action { controller.startWatching() } }
        }
    }
}

class ReplicationView : View("Replikácia") {

    private val controller: MyController by inject()

    override val root = borderpane {
        top = vbox {
            spacer()
            this+=ControlsView()
            slider(min = 0, max = 1000) {
                valueProperty().bindBidirectional(controller.speedProperty)
                setOnMouseClicked { controller.updateSpeed() }
            }
        }

        center = tabpane{
            tabClosingPolicy = TabPane.TabClosingPolicy.UNAVAILABLE
            tab(Terminal1View::class)
            tab(Terminal2View::class)
            tab(AirCarRentalView::class)
            tab(MinibusesView::class)
        }
        right = tableview(controller.currentReplicationState) {
            columnResizePolicy = SmartResize.POLICY

            column("Simulacny cas", AirCarRentalStateModel::currentTime) {
                isSortable = false
            }
            column("Priemerny straveny cas", AirCarRentalStateModel::customersTimeInSystem) {
                isSortable = false
            }
        }

    }
}
