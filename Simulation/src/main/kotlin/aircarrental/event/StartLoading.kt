package aircarrental.event

import aircarrental.entities.Minibus
import aircarrental.entities.Terminal
import aircarrental.entities.nextStop

class StartLoading(
    private val minibus: Minibus,
    private val terminal: Terminal,
    time: Double
) : AcrEvent(time) {

    override fun execute() = with(core) {
        minibus.isInSource = true
        if (minibus.isNotFull() && terminal.queue.isNotEmpty())
            plan(EnterToMinibus(
                minibus = minibus,
                terminal = terminal,
                customer = terminal.queue.pop(),
                time = currentTime + rndTimeToEnterBus.next()
            ))
         else
            plan(MinibusGoTo(
                minibus = minibus,
                source = terminal.description,
                destination = terminal.description.nextStop(),
                time =  currentTime
            ))
    }

}