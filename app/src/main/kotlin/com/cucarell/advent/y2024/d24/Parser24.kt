package com.cucarell.advent.y2024.d24

import java.nio.file.Path
import kotlin.io.path.useLines

data class Parser24(
    val wiresValue: MutableMap<WireKey, Boolean>,
    val gates: List<GateData>,
    val allWireKeys: Set<WireKey>
)

fun parser24(file: Path): Parser24 {
    file.useLines { lines: Sequence<String> ->
        val iterator: Iterator<String> = lines.iterator()
        val wiresValue: MutableMap<WireKey, Boolean> = declareWires(iterator)
        val (gates: List<GateData>, allWireKeys: Set<WireKey>) = declareGates(iterator.asSequence())
        return Parser24(wiresValue, gates, allWireKeys)
    }
}

fun declareWires(iterator: Iterator<String>): MutableMap<WireKey, Boolean> {
    var line: String = iterator.next()
    val output: MutableMap<WireKey, Boolean> = mutableMapOf()
    do {
        val (key: WireKey, value: String) = line.split(":", limit = 2).map { element: String -> element.trim() }
        output[key] = when (value) {
            "0" -> false
            "1" -> true
            else -> error("Unexpected value on parsing: $value !in 0..1")
        }
        line = iterator.next()
    } while (line.isNotBlank())
    return output
}

/**
 * jtd AND qwj -> bsf
 * dgm OR cth -> msm
 */
data class GateData(val input1: WireKey, val input2: WireKey, val gate: Gate, val output: WireKey) {
    override fun toString(): String = "($input1, $input2) $gate -> $output"
}

val GATE_REGEX = """^(\w+)\s+(\w+)\s+(\w+)\s+->\s+(\w+)$""".toRegex()
fun declareGates(lines: Sequence<String>): Pair<List<GateData>, Set<WireKey>> {
    val allWiresKey: MutableSet<WireKey> = mutableSetOf()
    val list: List<GateData> = lines.map { line: String ->
        val (wI1: WireKey, gate: String, wI2: WireKey, wo: WireKey) = GATE_REGEX.matchEntire(line)!!.destructured
        allWiresKey.addAll(listOf(wI1, wI2, wo))
        GateData(input1 = wI1, input2 = wI2, gate = Gate.of(gate), output = wo)
    }.toList()
    return Pair(list, allWiresKey)
}
