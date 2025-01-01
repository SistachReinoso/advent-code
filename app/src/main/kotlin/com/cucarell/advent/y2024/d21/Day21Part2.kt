package com.cucarell.advent.y2024.d21

import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.readLines

fun main() {
    val file: Path = object {}.javaClass.getResource("/2024/21/input")!!.path.let(::Path)
    part2(file).let(::println)
}

fun part2(file: Path, robots: Int = 25): Long =
    file.readLines()
        .sumOf { code: String -> calculateOutput2(code, robots) }

fun calculateOutput2(code: String, robots: Int): Long {
    val parse: Long = code.dropLast(1).toLong()
    val length: Long = resolvePart2(code, robots)
    println("$code: --  $length * $parse") // TODO delate me please
    return parse * length
}

fun resolvePart2(code: String, robots: Int): Long =
    "A$code".toList().asSequence().windowed(2)
        .sumOf { (a: Char, b: Char) ->
            bestRoutes(a, b, numericKeypad)
                .map { list: List<DirectionsKeypad> -> list.instructions() }
                .minOf { list: String -> directionalRobot(list, robots) }
        }

val directionRobotCache: MutableMap<Pair<String, Int>, Long> = mutableMapOf()
fun decoratorRobot(key: Pair<String, Int>, action: () -> (Long)): Long =
    directionRobotCache.getOrPut(key) { action() }

fun directionalRobot(code: String, robots: Int): Long = decoratorRobot(Pair(code, robots)) {
    if (robots == 1) robot340degrees(code).length.toLong()
    else "A$code".toList().asSequence().windowed(2)
        .sumOf { (a: Char, b: Char) ->
            bestRoutes(a, b, directionalKeypad)
                .map { list: List<DirectionsKeypad> -> list.instructions() }
                .minOf { list: String -> directionalRobot(list, robots - 1) }
        }
}
