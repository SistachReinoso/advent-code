package com.cucarell.advent.y2024.d21

import com.cucarell.advent.utils.Coordinates
import com.cucarell.advent.utils.CoordinatesInterface
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.readLines

fun main() {
    val file: Path = object {}.javaClass.getResource("/2024/21/input")!!.path.let(::Path)
    part1(file).let(::println)
}

fun part1(file: Path): Long =
    file.readLines()
        .sumOf(::calculateOutput)

fun calculateOutput(code: String): Long {
    val parse: Long = code.dropLast(1).toLong()
    val output: String = robot1depressurized(code)
    val length: Int = output.length
    // TODO println("$code: $output --  $length * $parse")
    return parse * length
}

fun robot1depressurized(code: String): String =
    "A$code".toList().windowed(2)
        .joinToString("") { (a: Char, b: Char) ->
            bestRoutes(a, b, numericKeypad)
                .map { list: List<DirectionsKeypad> -> list.instructions() }
                .map { list: String -> robot2radiation(list) }
                .minBy { instructions: String -> instructions.length }
        }

fun robot2radiation(code: String): String {
    return "A$code".toList().windowed(2)
        .joinToString("") { (a, b) ->
            bestRoutes(a, b, directionalKeypad)
                .map { list: List<DirectionsKeypad> -> list.instructions() }
                .map { list: String -> robot340degrees(list) }
                .minBy { instructions: String -> instructions.length }
        }
}

fun robot340degrees(code: String): String {
    return "A$code".toList().windowed(2)
        .joinToString("") { (a, b) ->
            bestRoutes(a, b, directionalKeypad)
                .map { list -> list.instructions() }
                .first()
        }
}

fun List<DirectionsKeypad>.instructions(): String = this.joinToString("") { dk -> dk.char.toString() }

// (?) decorator (?)
fun bestRoutes(start: Char, end: Char, map: List<String>, not: Char = ' '): Sequence<List<DirectionsKeypad>> {
    val startC: Coordinates = getCoordinates(start, map)
    val endC: Coordinates = getCoordinates(end, map)
    val not: Coordinates = getCoordinates(not, map)
    return bestRoutes(startC, endC, not).map { list -> list + listOf(DirectionsKeypad.A) }
}

// (?) decorator (?)
fun getCoordinates(find: Char, map: List<String>): Coordinates {
    map.forEachIndexed { y: Int, line: String ->
        line.forEachIndexed { x: Int, char: Char -> if (char == find) return Coordinates(x, y) }
    }
    error("Not find this coordinates")
}

fun getValue(c: CoordinatesInterface, map: List<String>): Char = map[c.y][c.x]

// TODO utils of combinations! <<^^^ -> all combinations!
fun bestRoutes(start: Coordinates, end: Coordinates, not: Coordinates): Sequence<List<DirectionsKeypad>> = sequence {
    if (start == end) yield(emptyList())
    else if (start != not) {
        if (end.x < start.x) bestRoutes(start.left(), end, not)
            .forEach { list -> yield(listOf(DirectionsKeypad.LEFT) + list) }
        else if (start.x < end.x) bestRoutes(start.right(), end, not)
            .forEach { list -> yield(listOf(DirectionsKeypad.RIGHT) + list) }
        if (start.y < end.y) bestRoutes(start.down(), end, not)
            .forEach { list -> yield(listOf(DirectionsKeypad.DOWN) + list) }
        else if (start.y > end.y) bestRoutes(start.up(), end, not)
            .forEach { list -> yield(listOf(DirectionsKeypad.UP) + list) }
    }
}
