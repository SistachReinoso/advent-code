package com.cucarell.advent.y2024

import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.useLines

fun main() {
    val file: Path = object {}.javaClass.getResource(
        //"/2024/08/demo"
        "/2024/08/input"
    )!!.path.let(::Path)

    part1(file) // 14
    part2(file) // 34
}

private fun part1(file: Path) {
    val (map, size) = parse(file)

    map
        .map { (_, list) -> getAllAntinodes(list) }
        .flatten()
        .toSet()
        .filter { coordinates -> isIn(coordinates, size) }
        .also { println(it.size) }
}

private fun isIn(c: Coordinates, size: Coordinates): Boolean = (c.x in 0..size.x) and (c.y in 0..size.y)

private fun getAllAntinodes(list: List<Coordinates>): Collection<Coordinates> = (0..list.size - 2)
    .map { i -> combinationElementList(list[i], list.drop(i + 1)) }
    .flatten()

private fun combinationElementList(c: Coordinates, list: List<Coordinates>): List<Coordinates> =
    list.map { e -> antinodes(c, e) }.flatten()

private fun antinodes(a: Coordinates, b: Coordinates): List<Coordinates> {
    val diff = a - b
    return listOf(a + diff, b - diff)
}

private val wRegex = """\w""".toRegex()
private fun parse(file: Path): Pair<Map<Char, List<Coordinates>>, Coordinates> {
    file.useLines { lines ->
        var xMax = 0
        var yMax = 0
        return lines
            .mapIndexed { y, line ->
                yMax = if (yMax < y) y else yMax
                line.mapIndexedNotNull { x, c ->
                    xMax = if (xMax < x) x else xMax
                    wRegex.matchEntire(c.toString())
                        ?.let { Pair(c, Coordinates(x, y)) }
                }
            }
            .flatten()
            .groupBy(keySelector = { it.first }, valueTransform = { it.second })
            .let { map -> Pair(map, Coordinates(xMax, yMax)) }
    }
}

/****************************************************************************************************/
/*                                             Part 2                                               */
/****************************************************************************************************/

private fun part2(file: Path) {
    val (map, size) = parse(file)

    map
        .map { (_, list) -> getAllAntinodes2(list, size) }
        .flatten()
        .toSet()
        .filter { coordinates -> isIn(coordinates, size) }
        .also { println(it.size) }
}

private fun getAllAntinodes2(list: List<Coordinates>, size: Coordinates): Collection<Coordinates> = (0..list.size - 2)
    .map { i -> combinationElementList2(list[i], list.drop(i + 1), size) }
    .flatten()

private fun combinationElementList2(c: Coordinates, list: List<Coordinates>, size: Coordinates): List<Coordinates> =
    list.map { e -> antinodes2(c, e, size) }.flatten()

private fun antinodes2(a: Coordinates, b: Coordinates, size: Coordinates): List<Coordinates> {
    val diff = a - b
    val result = mutableListOf(a, b)

    var i = 1
    do {
        val r = a + diff * i++
        if (isIn(r, size)) result.add(r)
    } while (isIn(r, size))

    i = 1
    do {
        val r = b - diff * i++
        if (isIn(r, size)) result.add(r)
    } while (isIn(r, size))

    return result
}
