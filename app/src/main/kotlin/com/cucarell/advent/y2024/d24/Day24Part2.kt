package com.cucarell.advent.y2024.d24

import java.nio.file.Path
import kotlin.io.path.Path

fun main() {
    val file: Path = object {}.javaClass.getResource("/2024/24/input")!!.path.let(::Path)
    part2(file).let(::println)
}

data class WiresSwapped(val v1: WireKey, val v2: WireKey)

fun part2(file: Path): String {
    val (_, gates: MutableMap<WireKey, GateInput>, _) = parser24(file)

    var wiresSwapped = whichWiresToSwap(gates)
    return wiresSwapped
        .map { (w1: WireKey, w2: WireKey) -> listOf(w1, w2) }
        .flatten()
        .sorted()
        .joinToString(",")
}

fun whichWiresToSwap(gates: MutableMap<WireKey, GateInput>): Set<WiresSwapped> {
    var nextError: Int = firstErrorOnAddition(gates) ?: error("We can't started the investigation")
    val solution: MutableSet<WiresSwapped> = mutableSetOf()
    loop@ while (true) {
        //println("I ara que fem amb el $nextError?\n$wii")
        for (wireKey1: WireKey in searchDependencies(gates, nextError)) {
            for (wireKey2: WireKey in gates.keys - wireKey1) {
                val exploringSwap = WiresSwapped(wireKey1, wireKey2)
                swapWires(gates, exploringSwap)
                val exploringNextError: Int? = firstErrorOnAddition(gates)
                if (exploringNextError == null) {
                    solution.add(exploringSwap)
                    return solution
                } else if (exploringNextError > nextError) {
                    solution.add(exploringSwap)
                    nextError = exploringNextError
                    continue@loop
                }
                swapWires(gates, exploringSwap)
            }
        }
        error("Solution not found: $nextError: $solution")
    }
}

fun firstErrorOnAddition(gates: Map<WireKey, GateInput>): Int? = gates.keys
    .asSequence()
    .filter { wireKey: WireKey -> wireKey.startsWith('z') }
    .sorted()
    .map { wiredKey: WireKey -> wiredKey.drop(1).toInt() }
    .firstOrNull { i: Int -> !isValidGateAddition(gates, i) }

// TODO improvement with Gate (Xor, And or Or) + WireKey -- invalidWireKeys: Set<WireKey>? ... + expected for each¿?
fun isValidGateAddition(gates: Map<WireKey, GateInput>, i: Int): Boolean {
    // z[0] = x[0] (XOR) y[0]
    // z[i] = x[i] XOR y[i] (XOR) carry[i] // check on test: binarySum
    // z[-1]= carry[i]
    val wireKey = generateWiredKey('z', i)
    val gate: GateInput = gates.getValue(wireKey)
    if (i == 45) return isCarry(gates, wireKey, i)
    if (gate.gate != Gate.XOR) return false // setOf(wireKey)
    if (i == 0) return listWiredKey(0) in gate // if (listWiredKey(0) in gate) null else setOf(wireKey)
    return (isXorWiredKey(gates, gate.wireKey1, i) && isCarry(gates, gate.wireKey2, i))
            || (isXorWiredKey(gates, gate.wireKey2, i) && isCarry(gates, gate.wireKey1, i))
}

fun isXorWiredKey(gates: Map<WireKey, GateInput>, wireKey: WireKey, i: Int): Boolean { //Set<WireKey>? {
    val gate: GateInput = gates[wireKey] ?: return false // TODO() // emptySet()
    if (gate.gate != Gate.XOR) return false // setOf(wireKey)
    return listWiredKey(i) in gate // return if (listWiredKey(i) in gate) null else setOf(wireKey)
}

fun isCarry(gates: Map<WireKey, GateInput>, wireKey: WireKey, index: Int): Boolean {
    val i = index - 1
    // carry[1] = x[0] & y[0]
    // carry[i] = (x[i -1] & y[i -1]) || ((x[i -1] XOR y[i -1]) & carry[i -1])
    val gate: GateInput = gates[wireKey] ?: return false
    if (i == 0) {
        if (gate.gate != Gate.AND) return false
        return listWiredKey(i) in gate
    }

    if (gate.gate != Gate.OR) return false
    return (isFirstCarry(gates, gate.wireKey1, i) && isSecondCarry(gates, gate.wireKey2, i))
            || (isFirstCarry(gates, gate.wireKey2, i) && isSecondCarry(gates, gate.wireKey1, i))
}

// (x[i -1] & y[i -1])
fun isFirstCarry(gates: Map<WireKey, GateInput>, wireKey: WireKey, i: Int): Boolean {
    val gate: GateInput = gates[wireKey] ?: return false
    if (gate.gate != Gate.AND) return false
    return listWiredKey(i) in gate
}

// ((x[i -1] XOR y[i -1]) & carry[i -1])
fun isSecondCarry(gates: Map<WireKey, GateInput>, wireKey: WireKey, i: Int): Boolean {
    val gate: GateInput = gates[wireKey] ?: return false
    if (gate.gate != Gate.AND) return false
    return (isXorWiredKey(gates, gate.wireKey1, i) && isCarry(gates, gate.wireKey2, i))
            || (isXorWiredKey(gates, gate.wireKey2, i) && isCarry(gates, gate.wireKey1, i))
}

fun listWiredKey(i: Int): Set<WireKey> = setOf(generateWiredKey('x', i), generateWiredKey('y', i))
fun generateWiredKey(char: Char, i: Int): WireKey = char + i.toString().padStart(2, '0')

fun getLongValue(char: Char, values: Map<WireKey, Boolean>, allWiresKey: Set<WireKey>): Long = allWiresKey
    .filter { it.startsWith(char) }.sortedDescending()
    .map { wireKey: WireKey -> values.getValue(wireKey) }
    .joinToString("") { bool: Boolean -> if (bool) "1" else "0" }
    .let { output: String -> output.toLong(2) }

fun searchDependencies(gates: Map<WireKey, GateInput>, i: Int): Set<WireKey> =
    searchDependencies(gates, generateWiredKey('z', i))

fun searchDependencies(gates: Map<WireKey, GateInput>, wireKey: WireKey): Set<WireKey> {
    val gate: GateInput = gates[wireKey] ?: return setOf()
    return setOf(wireKey) +
            searchDependencies(gates, gate.wireKey1) +
            searchDependencies(gates, gate.wireKey2)
}

fun swapWires(gates: MutableMap<WireKey, GateInput>, s: WiresSwapped): Unit {
    val tmp: GateInput = gates.getValue(s.v1)
    gates[s.v1] = gates.getValue(s.v2)
    gates[s.v2] = tmp
}
