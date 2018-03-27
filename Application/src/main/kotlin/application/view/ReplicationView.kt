package application.view

import application.controller.MyController
import application.model.AirCarRentalStateModel
import application.view.converter.ReplicationConverter
import application.view.converter.SimTimeConverter
import application.view.converter.SimTimeToRealTimeConverter
import javafx.geometry.Insets
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
            this += ControlsView()
            spacer()
            label("Dlžka uspania po jednej sekunde")
            hbox {
                fitToWidth(this)
                hbox {
                    label("max speed")
                    slider(min = 0, max = 500) {
                        valueProperty().bindBidirectional(controller.speedProperty)
                        setOnMouseReleased { controller.updateSpeed() }
                    }
                    label("min speed")
                }
                spacer()
                hbox {
                    label("1 sekunda")
                    slider(min = 1, max = 300) {
                        valueProperty().bindBidirectional(controller.tickProperty)
                        setOnMouseReleased { controller.updateTick() }
                    }
                    label("300 sekund")
                }
            }
        }

        center = tabpane {
            tabClosingPolicy = TabPane.TabClosingPolicy.UNAVAILABLE
            tab(Terminal1View::class)
            tab(Terminal2View::class)
            tab(AirCarRentalView::class)
            tab(MinibusesView::class)
        }
        right = vbox {
            padding = Insets(10.0)
            minWidth = 200.0
            hbox {
                label("Simulacny cas")
                spacer()
                label(controller.currentRepProperty, converter = SimTimeConverter())
            }
            spacer()
            hbox {
                label("Simulacny cas")
                spacer()
                label(controller.currentRepProperty, converter = SimTimeToRealTimeConverter())

            }
            spacer()
            hbox {
                label("Replikacia")
                spacer()
                label(controller.currentRepProperty, converter = ReplicationConverter())

            }

        }

    }
}
