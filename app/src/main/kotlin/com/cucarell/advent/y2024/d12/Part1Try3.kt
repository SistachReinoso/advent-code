package com.cucarell.advent.y2024.d12

import com.cucarell.advent.y2024.Coordinates
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.readLines

fun main() {
    val file: Path = object {}.javaClass.getResource(
        //"/2024/12/demo"
        //"/2024/12/demo2"
        //"/2024/12/demo3"
        "/2024/12/input"
    )!!.path.let(::Path)

    part1Try3(file) // 140, 772, 88
        .let(::println)
}

private class AreaPerimeterPrice2(val char: Char, var area: Long, var perimeter: Long) {
    fun getPrice(): Long = area * perimeter
    override fun toString(): String = "${super.toString()} ($char, $area * $perimeter) = ${getPrice()}"
}

fun part1Try3(file: Path): Long {
    val steps: MutableSet<Coordinates> = mutableSetOf()
    val table = parser(file)

    val rows = table.indices
    val columns = table.first().indices

    return columns.sumOf { x ->
        rows.sumOf loop@{ y ->
            val coordinates = Coordinates(x, y)
            if (coordinates in steps) return@loop 0L

            val area = recursive(coordinates, table, steps)
            // TODO println(area)
            area.getPrice()
        }
    }
}

private fun recursive(
    c: Coordinates,
    table: List<String>,
    steps: MutableSet<Coordinates>,
): AreaPerimeterPrice2 {
    steps.add(c)
    val area = AreaPerimeterPrice2(char = table[c.y][c.x], area = 1, perimeter = 0)

    listOf(
        c.copy(x = c.x - 1),
        c.copy(x = c.x + 1),
        c.copy(y = c.y - 1),
        c.copy(y = c.y + 1),
    )
        .forEach { newC ->
            explore(table, newC, area, steps)
        }
    return area
}

private fun explore(
    table: List<String>,
    newC: Coordinates,
    area: AreaPerimeterPrice2,
    steps: MutableSet<Coordinates>
) {
    if (table.getOrNull(newC) != area.char) area.perimeter += 1
    else if (newC !in steps) {
        val tmp = recursive(newC, table, steps)
        area.area += tmp.area
        area.perimeter += tmp.perimeter
    }
}

private fun List<String>.getOrNull(coordinates: Coordinates) = this
    .getOrNull(coordinates.y)
    ?.getOrNull(coordinates.x)

private fun parser(file: Path): List<String> = file.readLines()
