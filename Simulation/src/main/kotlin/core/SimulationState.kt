package core

enum class SimulationState {
    Init,
    WarmingUp,
    CoolingDown,
    WarmedUp
}

fun SimulationState.isValid() : Boolean = when (this) {
    SimulationState.Init,
    SimulationState.WarmingUp -> false
    SimulationState.CoolingDown,
    SimulationState.WarmedUp -> true
}
