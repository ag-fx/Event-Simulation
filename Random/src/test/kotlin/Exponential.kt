import XRandom.ExponentialRandom
import java.io.File

fun main(args: Array<String>) {
    val prand = ExponentialRandom(5.0)
    File("testData").mkdir()
    val f = File("testData/ExponentialRandom.txt")
    f.delete()
    val genValues = mutableListOf<Double>()
    repeat(1000) {
        genValues += prand.next()
    }
    f.writeText(genValues.joinToString("\n"))
    println("data in testData/ExponentialRandom.txt")
}