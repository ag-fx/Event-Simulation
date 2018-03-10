package XRandom

import java.util.*

abstract class XRandom<out T : Number>(seed: Long? = null) : NextRandom<T> {
    protected val uniformRandom = if (seed == null) Random() else Random(seed)
}
