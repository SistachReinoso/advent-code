package org.example

import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.useLines
import kotlin.math.abs

val realFile: Path = object {}.javaClass.getResource("/day2.txt")!!.path.let(::Path)
val demoFile = object {}.javaClass.getResource("/day2.demo.txt")!!.path.let(::Path)

fun main() {
    readFile(realFile, Sequence<String>::day2Part2)
        .let(::println)
}

fun Sequence<String>.day2Part2() = this
    .map { line -> line.split("""\s++""".toRegex()).map(String::toInt) }
    .filter { list -> increaseOrDecrease2(list) }
    .count()

fun Sequence<String>.day2First() = this
    .map { line -> line.split("""\s++""".toRegex()).map(String::toInt) }
    .filter { list -> increaseOrDecrease(list) }
    .count()

fun increaseOrDecrease2(list: List<Int>): Boolean {
    if (increaseOrDecrease(list)) return true
    val range = list.indices
    return range.any { i -> increaseOrDecrease(list.slice(range - i)) }
}

fun increaseOrDecrease(list: List<Int>): Boolean {
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

private fun <T> readFile(file: Path, kFunction1: (Sequence<String>) -> T): T =
    file.useLines { lines -> return kFunction1(lines) }
