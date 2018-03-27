package application.view

import application.controller.MyController
import application.model.AirCarRentalStateModel
import application.view.converter.*
import javafx.geometry.Insets
import javafx.geometry.Orientation
import javafx.scene.control.TabPane
import tornadofx.*


class ControlsView : View("Controls") {

    private val controller: MyController by inject()

    override val root = hbox {
        padding = Insets(12.0)
        button("Pause") { action { controller.pause() } }
        button("Resume") { action { controller.resume() } }
        spacer()
        button("Stop watching") { action { controller.stopWatching() } }
        button("Start watching") { action { controller.startWatching() } }
        spacer()
        button("Start") { action { controller.start() } }
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
                hbox {
                    label("max speed")
                    slider(min = 0, max = 500) {
                        valueProperty().bindBidirectional(controller.speedProperty)
                        setOnMouseReleased { controller.updateSpeed() }
                    }
                    label("min speed")
                    padding = Insets(6.0)
                }
                separator(Orientation.VERTICAL)
                hbox {
                    padding = Insets(6.0)

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
            padding = Insets(24.0)
            minWidth = 300.0
            hbox {
                label("Simulacny cas")
                spacer()
                label(controller.currentRepProperty, converter = SimTimeConverter())
            }
            hbox {
                label("Simulacny cas")
                spacer()
                label(controller.currentRepProperty, converter = SimTimeToRealTimeConverter())

            }
            hbox {
                label("Replikacia")
                spacer()
                label(controller.currentRepProperty, converter = XConverter{
                    it?.replicationNumber?.toString() ?: "err"
                })
            }
            separator()
            hbox {
                label("Počet ľudí Terminál 1")
                spacer()
                label(controller.currentRepProperty, converter = TerminalOneWaitingPoeple())
            }
            hbox {
                label("Priemerna dlzka radu Terminál 1")
                spacer()
                label(controller.currentRepProperty, converter = XConverter{
                    it?.avgQueueSizeTerminalOne?.let { decimalFormat.format(it) } ?: "err"
                })
            }
            hbox {
                label("Priemerny cas v rade Terminál 1")
                spacer()
                label(controller.currentRepProperty, converter = XConverter{
                    it?.avgQueueWaitTimeTerminalOne?.let { decimalFormat.format(it/60) + " min" } ?: "err"
                })
            }
            separator()

            hbox {
                label("Počet ľudí Terminál 2")
                spacer()
                label(controller.currentRepProperty, converter = TerminalTwoWaitingPoeple())
            }
            hbox {
                label("Priemerna dlzka radu Terminál 2")
                spacer()
                label(controller.currentRepProperty, converter = XConverter{
                    it?.avgQueueSizeTerminalTwo?.let { decimalFormat.format(it) }  ?: "err"
                })
            }
            hbox {
                label("Priemerny cas v rade Terminál 2")
                spacer()
                label(controller.currentRepProperty, converter = XConverter{
                    it?.avgQueueWaitTimeTerminalTwo?.let { decimalFormat.format(it/60) }  ?: "err"
                })
            }
            separator()

            hbox {
                label("Počet ľudí v autobusoch")
                spacer()
                label(controller.currentRepProperty, converter = XConverter {
                    it?.minibuses?.map { it.seats.size() }?.sum()?.toString() ?: "0"
                })
            }
            separator()
            hbox {
                label("Počet ľudí v rade AirCarRental")
                spacer()
                label(controller.currentRepProperty, converter = AirCarRentalWaitingPoeple())
            }
            hbox {
                label("Priemerna dlzka radu AirCarRental")
                spacer()
                label(controller.currentRepProperty, converter = XConverter{
                    it?.avgQueueSizeCarRental?.let { decimalFormat.format(it) }  ?: "err"
                })
            }

            hbox {
                label("Priemerna dlzka cakania AirCarRental")
                spacer()
                label(controller.currentRepProperty, converter = XConverter{
                    it?.avgQueueWaitTimeCarRental?.let { decimalFormat.format(it/60) }  ?: "err"
                })
            }
            hbox {
                label("Počet voľných pracovníkov")
                spacer()
                label(controller.currentRepProperty, converter = AirCarRentalFreeEmployee())
            }
            hbox {
                label("Počet obsluhovaných zákazníkov")
                spacer()
                label(controller.currentRepProperty, converter = XConverter{
                    it?.employees?.filter { it.isBusy }?.size?.toString() ?: "0"
                })
            }
            hbox {
                label("Počet vybavených zákazníkov")
                spacer()
                label(controller.currentRepProperty, converter = XConverter {
                    it?.numberOfServedCustomers?.toString() ?: "0"
                })
            }

        }

    }
}
