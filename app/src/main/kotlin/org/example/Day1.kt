package org.example

import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.useLines
import kotlin.math.abs

fun distance(a: Long, b: Long): Long = abs(a - b)

fun main() {
    val realFile = object {}.javaClass.getResource("/day1.txt")!!.path.let(::Path)
    val demoFile = object {}.javaClass.getResource("/day1.demo.txt")!!.path.let(::Path)

    parser(demoFile, ::day1Part2)
        .let(::println)
}

private fun day1Part1(left: List<Long>, right: List<Long>): Long =
    (left.sorted() zip right.sorted())
        .sumOf { (e, d) -> distance(e, d) }

private fun day1Part2(left: List<Long>, rightInput: List<Long>): Long {
    val right = rightInput.groupingBy { it }.eachCount()
    return left.sumOf { value -> value * (right[value] ?: 0) }
}

private fun parser(file: Path, operation: (List<Long>, List<Long>) -> Long): Long =
    file.useLines { lines ->
        lines
            .map { line -> line.split("""\s++""".toRegex()).map(String::toLong) }
            .map { values -> values[0] to values[1] }
            .unzip()
    }.let { (left, right) -> operation(left, right) }
