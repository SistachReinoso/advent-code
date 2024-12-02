package org.example

import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.useLines
import kotlin.math.abs

val realFile: Path = object {}.javaClass.getResource("/day2.txt")!!.path.let(::Path)
val demoFile = object {}.javaClass.getResource("/day2.demo.txt")!!.path.let(::Path)

fun main() {
    readFile(demoFile, Sequence<List<Long>>::day2Part2)
        .let(::println)
}

fun Sequence<List<Long>>.day2Part2() = this
    .filter(::increaseOrDecrease2)
    .count().toLong()

fun Sequence<List<Long>>.day2First() = this
    .filter(::increaseOrDecrease)
    .count().toLong()

fun increaseOrDecrease2(list: List<Long>): Boolean {
    if (increaseOrDecrease(list)) return true
    val range = list.indices
    return range.any { i -> increaseOrDecrease(list.slice(range - i)) }
}

fun increaseOrDecrease(list: List<Long>): Boolean {
    val increase: Boolean = list[0] < list[1]
    list
        .windowed(2)
        .forEach { (a, b) ->
            val diff = a - b
            if (abs(diff) !in 1..3) return false
            if (increase xor (a < b)) return false
        }
    return true
}

private fun readFile(file: Path, advent02: (Sequence<List<Long>>) -> Long): Long =
    file.useLines { lines ->
        lines
            .map { line -> line.split("""\s++""".toRegex()).map(String::toLong) }
            .let(advent02)
    }
