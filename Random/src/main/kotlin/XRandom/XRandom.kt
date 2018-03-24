package XRandom

import java.util.*

abstract class XRandom<out T : Number>(seed: Long) : NextRandom<T> {

    protected val uniformRandom =  Random(seed)

}