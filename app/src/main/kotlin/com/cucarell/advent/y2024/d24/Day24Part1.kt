package com.cucarell.advent.y2024.d24

import java.nio.file.Path
import kotlin.io.path.Path

fun main() {
    val file: Path = object {}.javaClass.getResource("/2024/24/input")!!.path.let(::Path)
    part1(file).let(::println)
}

data class GateInput(val wireKey1: WireKey, val wireKey2: WireKey, val gate: Gate) {
    operator fun contains(o: WireKey) = o in setOf(wireKey1, wireKey2)
    operator fun contains(o: Collection<WireKey>) = o.any { wo: WireKey -> wo in this }
}

fun part1(file: Path): Long {
    val (values: MutableMap<WireKey, Boolean>, gates: Map<WireKey, GateInput>, allWiresKey: Set<WireKey>) =
        parser24(file)
    return resolveZOutput(gates, allWiresKey, values)
}

fun resolveZOutput(
    mapValues: Map<WireKey, GateInput>,
    allWiresKey: Set<WireKey>,
    values: Map<WireKey, Boolean>
): Long {
    val cacheValues: MutableMap<WireKey, Boolean> = values.toMutableMap()
    fun cacheGetBool(wireKey: WireKey, action: () -> (Boolean)): Boolean = cacheValues.getOrPut(wireKey) { action() }
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
    return allWiresKey.filter { it.startsWith('z') }.sortedDescending()
        .map { wireKey: WireKey -> getBool(wireKey = wireKey, mapValues = mapValues) }
        .joinToString("") { bool: Boolean -> if (bool) "1" else "0" }
        .let { output: String -> output.toLong(2) }
}

