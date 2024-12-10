package com.cucarell.advent.y2024

import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.useLines


fun main() {
    val file: Path = object {}.javaClass.getResource(
        //"/2024/10/demo"
        "/2024/10/input"
    )!!.path.let(::Path)

    part1(file) // 36
    part2(file) // 81
}

private fun part1(file: Path) {
    val table = parse(file)

    table.withIndex().sumOf { (y, row) ->
        row.withIndex()
            .filter { (_, v) -> v == 0L }
            .sumOf { (x, _) -> walkTo9(table, Coordinates(x, y)).toSet().size }
    }.let(::println)
}

private fun walkTo9(table: List<List<Long>>, c: Coordinates, value: Long = 0): Collection<Coordinates> {
    if (value != table[c.y][c.x]) return emptyList()
    if (value == 9L) return listOf(c)

    val rows = table.size
    val columns = table.first().size

    return listOf(c.copy(x = c.x - 1), c.copy(x = c.x + 1), c.copy(y = c.y - 1), c.copy(y = c.y + 1))
        .filter { e -> e.run { x in 0..<columns && y in 0..<rows } }
        .map { e -> walkTo9(table, e, value + 1) }
        .flatten()
}

private fun parse(file: Path): List<List<Long>> = file
    .useLines { lines -> lines.map { line -> line.map { c -> c.digitToInt().toLong() } }.toList() }

/****************************************************************************************************/
/*                                             Part 2                                               */
/****************************************************************************************************/

private fun part2(file: Path) {
    val table = parse(file)

    table.withIndex().sumOf { (y, row) ->
        row.withIndex()
            .filter { (_, v) -> v == 0L }
            .sumOf { (x, _) -> walkTo9(table, Coordinates(x, y)).size }
    }.let(::println)
}
