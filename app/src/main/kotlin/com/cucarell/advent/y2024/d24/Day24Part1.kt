package com.cucarell.advent.y2024.d24

import java.nio.file.Path
import kotlin.io.path.Path

fun main() {
    val file: Path = object {}.javaClass.getResource("/2024/24/input")!!.path.let(::Path)
    part1(file).let(::println)
}

data class GateInput(val wireKey1: WireKey, val wireKey2: WireKey, val gate: Gate)

fun part1(file: Path): Long {
    val (values: MutableMap<WireKey, Boolean>, gates: List<GateData>, allWiresKey: Set<WireKey>) = parser24(file)
    val mapValues = gates.associate { gateData: GateData -> gateData.run { output to GateInput(input1, input2, gate) } }
    cacheWireKey = values

    return allWiresKey.filter { it.startsWith('z') }.sortedDescending()
        .map { wireKey: WireKey -> getBool(wireKey = wireKey, mapValues = mapValues) }
        .joinToString("") { bool: Boolean -> if (bool) "1" else "0" }
        .let { output: String -> output.toLong(2) }
}

lateinit var cacheWireKey: MutableMap<WireKey, Boolean>
fun cacheGetBool(wireKey: WireKey, action: () -> (Boolean)): Boolean = cacheWireKey.getOrPut(wireKey) { action() }
fun getBool(wireKey: WireKey, mapValues: Map<WireKey, GateInput>): Boolean = cacheGetBool(wireKey) {
    val (wireKey1: WireKey, wireKey2: WireKey, gate: Gate) = mapValues.getValue(wireKey)
    val bool1 = getBool(wireKey1, mapValues)
    val bool2 = getBool(wireKey2, mapValues)
    when (gate) {
        Gate.AND -> bool1 and bool2
        Gate.OR -> bool1 or bool2
        Gate.XOR -> bool1 xor bool2
    }
}
