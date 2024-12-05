package com.cucarell.advent.y2024

import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.io.path.useLines


private val file: Path = object {}.javaClass.getResource(
    //"/2024/04/demo"
    //"/2024/04/demo2"
    "/2024/04/input"
)!!.path.let(::Path)

const val XMAS = "XMAS"
val XMAS_regex = XMAS.toRegex()
val SAMX_regex = XMAS.reversed().toRegex()

private fun main() {
    part1(file) // 18
    part2(file) //  9 + 1
}

private fun part1(file: Path) {
    val list = file.readLines()
    val h = countHoritzontal(list, ::countXmas) // 5
    val v = countVertical(list, ::countXmas) // 3
    val d = countDiagonal(list, ::countXmas) // 5 / + 5 \
    println(h + v + d)
}

fun countHoritzontal(list: List<String>, count: (String) -> Long): Long = list.sumOf(count)

fun countVertical(list: List<String>, count: (String) -> Long): Long = list
    .first()
    .indices
    .map { i -> list.map { string -> string[i] }.joinToString("") }
    //.onEach(::println)
    .sumOf(count)

fun countDiagonal(list: List<String>, count: (String) -> Long): Long {
    val rowI = list.size - 1
    val colI = list.first().length - 1
    val diaI = 0..rowI + colI
    val d1 = diaI
        .map { i ->
            indexDiagonal1(i, row = rowI, col = colI)
                .map { (x, y) -> list[x][y] }
                .joinToString("")
        }.sumOf(count)
    val d2 = diaI
        .map { i ->
            indexDiagonal2(i, row = rowI, col = colI)
                .map { (x, y) -> list[x][y] }
                .joinToString("")
        }.sumOf(count)
    return d1 + d2
}

fun countXmas(input: String): Long = XMAS_regex.findAll(input).count().toLong() + SAMX_regex.findAll(input).count()

fun indexDiagonal1(index: Int, row: Int, col: Int): Sequence<Pair<Int, Int>> = sequence {
    (0..index)
        .map { i -> Pair(i, index - i) }
        .filter { (a, b) -> a <= row && b <= col }
        .forEach { yield(it) }
}

fun indexDiagonal2(index: Int, row: Int, col: Int): Sequence<Pair<Int, Int>> = sequence {
    (0..index)
        .map { i -> Pair(i, col + i - index) }
        .filter { (a, b) -> a <= row && b in 0..col }
        .forEach { yield(it) }
}

/****************************************************************************************************/
/*                                        Part 2                                                    */
/****************************************************************************************************/

val aRegex = "A".toRegex()
private fun part2(file: Path) {
    file.useLines { lines ->
        lines
            .windowed(3)
            .sumOf { (x, y, z) ->
                val len = x.length - 1
                aRegex
                    .findAll(y)
                    .mapNotNull { match -> match.range.last.takeIf { value -> value in 1..<len } }
                    .count { index -> isDiagonalCross(index, x, z) }
            }
    }.let(::println)
}

fun isDiagonalCross(i: Int, x: String, z: String): Boolean =
    (x[i - 1] == 'M' && z[i + 1] == 'S' || x[i - 1] == 'S' && z[i + 1] == 'M') &&
            (x[i + 1] == 'M' && z[i - 1] == 'S' || x[i + 1] == 'S' && z[i - 1] == 'M')
