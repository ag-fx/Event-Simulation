package Core

import Core.Simulation.State.ReplicationState
import Core.Simulation.State.SimulationState
import TestSim.Newsstand.NewsStandState
import TestSim.Newsstand.NewsstandReplication
import kotlinx.coroutines.experimental.channels.consumeEach
import kotlinx.coroutines.experimental.channels.produce

abstract class Simulation<S : State>(val replicationCount: Int) {

    /**
      *  @return Instance of one replication run.
      */
    abstract fun replication(): Replication<S>


    /**
     * This list contains latest states from  each replication
     */
    private val replicationFinalStates = mutableListOf<S>()

    /**
     * Currently running replication. It's here so we can pause and resume replication
     */
    private lateinit var currentReplication: Replication<S>


    private lateinit var lastReplicationState: S

    /**
     * this list contains every replication that simulation will run.
     */
    private val replications = List(replicationCount) { replication() }


    sealed class State<out S> {
        abstract val state: S

        class ReplicationState<out S> (override val state: S) : State<S>()
        class SimulationState <out S> (override val state: S) : State<S>()
    }

    /**
     *  This is will create Channel where simulation and replications states will be send trough
     */
    fun start() = produce {
        replications.forEach {
            currentReplication = it
            it.start().consumeEach {
                lastReplicationState = it
                send(ReplicationState(it))
            }
            replicationFinalStates += lastReplicationState
            send(SimulationState(toState(replicationFinalStates)))
        }
    }

    /**
     * @return Simulation state
     */
    abstract fun toState(replications: List<S>): S

    fun pause() {
        currentReplication.pause()
    }

    fun resume() {
        currentReplication.resume()
    }

        }

class NewsSim : Simulation<NewsStandState>(5_000) {

    override fun replication(): Replication<NewsStandState> = NewsstandReplication()

    override fun toState(replications: List<NewsStandState>) = NewsStandState(
            avgQueueSize = replications.map { it.avgQueueSize }.average(),
            avgWaitTime  = replications.map { it.avgWaitTime  }.average(),
            currentTime  = -1.0,
            running      = false,
            run          = -1,
            stopped      = false
    )

}