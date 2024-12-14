package com.cucarell.advent.y2024.d12

import com.cucarell.advent.y2024.Coordinates
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.readLines

fun main() {
    val file: Path = object {}.javaClass.getResource(
        //"/2024/12/demo" //   80
        //"/2024/12/demo2" // 436
        //"/2024/12/demo4" // 236
        //"/2024/12/demo5" // 368
        "/2024/12/input"
    )!!.path.let(::Path)

    part2(file) // 80, 436
        .let(::println)
}

private class AreaPerimeterPrice3(val char: Char, var area: Long, var sides: Long) {
    fun getPrice(): Long = area * sides
    override fun toString(): String = "${super.toString()} ($char, $area * $sides) = ${getPrice()}"
}

fun part2(file: Path): Long {
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
): AreaPerimeterPrice3 {
    steps.add(c)
    val area = AreaPerimeterPrice3(char = table[c.y][c.x], area = 1, sides = 0)
    val left = c.copy(x = c.x - 1)
    val right = c.copy(x = c.x + 1)
    val down = c.copy(y = c.y - 1)
    val up = c.copy(y = c.y + 1)

    if (table.getOrNull(left) == area.char) {
       if (table.getOrNull(up) == area.char && table.getOrNull(up.copy(x = left.x)) != area.char)
           area.sides += 1
        if (table.getOrNull(down) == area.char && table.getOrNull(down.copy(x = left.x)) != area.char)
            area.sides += 1
    } else {
        if (table.getOrNull(up) != area.char)
            area.sides += 1
        if (table.getOrNull(down) != area.char)
            area.sides += 1
    }

    if (table.getOrNull(right) == area.char) {
        if (table.getOrNull(up) == area.char && table.getOrNull(up.copy(x = right.x)) != area.char)
            area.sides += 1
        if (table.getOrNull(down) == area.char && table.getOrNull(down.copy(x = right.x)) != area.char)
            area.sides += 1
    } else {
        if (table.getOrNull(up) != area.char)
            area.sides += 1
        if (table.getOrNull(down) != area.char)
            area.sides += 1
    }

    listOf(left, right, down, up)
        .forEach { newC ->
            explore(table, newC, area, steps)
        }
    return area
}

private fun explore(
    table: List<String>,
    newC: Coordinates,
    area: AreaPerimeterPrice3,
    steps: MutableSet<Coordinates>
) {
    if (table.getOrNull(newC) == area.char && newC !in steps) {
        val tmp = recursive(newC, table, steps)
        area.area += tmp.area
        area.sides += tmp.sides
    }
}

private fun List<String>.getOrNull(coordinates: Coordinates) = this
    .getOrNull(coordinates.y)
    ?.getOrNull(coordinates.x)

private fun parser(file: Path): List<String> = file.readLines()
