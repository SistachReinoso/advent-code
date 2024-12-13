package com.cucarell.advent.y2024.d12

import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.useLines

fun main() {
    val file: Path = object {}.javaClass.getResource(
        //"/2024/12/demo"
        //"/2024/12/demo2"
        //"/2024/12/demo3"
        "/2024/12/input"
    )!!.path.let(::Path)

    part1(file) // 140, 772, 88
}

fun part1(file: Path): Long {
    val result = parser(file, ::areaPerimeter2)
    println(result)
    return result
}

private class AreaPerimeterPrice(val char: Char, var area: Long, var perimeter: Long) {
    fun getPrice(): Long = area * perimeter
    override fun toString(): String = "${super.toString()} ($char, $area * $perimeter)"
}

private fun areaPerimeter2(sequence: Sequence<String>): Long {
    var lineBefore: List<AreaPerimeterPrice> = emptyList()

    val result = sequence
        .sumOf { line ->
            var leftAPP: AreaPerimeterPrice? = null
            val newLine = line.mapIndexed { x, c ->
                val up = lineBefore.getOrNull(x)

                // check left
                var newAPP: AreaPerimeterPrice = if (leftAPP == null)
                    AreaPerimeterPrice(char = c, area = 1, perimeter = 1)
                else if (leftAPP.char == c) {
                    leftAPP.area += 1
                    leftAPP
                } else {
                    leftAPP.perimeter += 1
                    AreaPerimeterPrice(char = c, area = 1, perimeter = 1)
                }

                // check up
                if (up == null)
                    newAPP.perimeter += 1
                else if (c == up.char) {
                    val tmp = newAPP
                    newAPP = up

                    if (leftAPP !== up) {
                        newAPP.area += tmp.area
                        newAPP.perimeter += tmp.perimeter
                    }
                } else { // c != up.char
                    up.perimeter += 1
                    newAPP.perimeter += 1
                }

                leftAPP = newAPP
                newAPP
            }
            newLine.last().perimeter += 1

            val result = (lineBefore - newLine).distinct().sumOf { e ->
                println("${e.char}: ${e.area} * ${e.perimeter} = ${e.getPrice()}")
                e.getPrice()
            }
            lineBefore = newLine
            result
            // TODO println("====================") ; newLine.forEach { e -> println("wii: $e") } ; println("====================")
        }

    val result2 = lineBefore
        .onEach { e -> e.perimeter += 1 }
        .distinct()
        .sumOf { e ->
            println("${e.char}: ${e.area} * ${e.perimeter} = ${e.getPrice()}")
            e.getPrice()
        }
    return result2 + result
}

private fun areaPerimeter(sequence: Sequence<String>) {
    val elements: MutableSet<Char> = mutableSetOf()
    val area: MutableMap<Char, Long> = mutableMapOf()
    val perimeter: MutableMap<Char, Long> = mutableMapOf()

    sequence
        .windowed(2)
        .forEachIndexed { y, (a, b) ->
            if (y == 0) {
                a.forEach { c -> perimeter[c] = (perimeter[c] ?: 0) + 1 }
            } else if (y == a.length - 2) { // XXX Square examples
                b.forEachIndexed { x, c ->
                    elements.add(c)
                    area[c] = (area[c] ?: 0) + 1
                    perimeter[c] = (perimeter[c] ?: 0) + 1 // Down

                    leftAndRight(x, perimeter, c, b)
                }
            }
            a.forEachIndexed { x, c ->
                elements.add(c)
                area[c] = (area[c] ?: 0) + 1

                leftAndRight(x, perimeter, c, a)
                down(x, perimeter, c, b)
            }
        }

    // elements.forEach { e -> println("$e: a: ${area[e]}, p: ${perimeter[e]}") } // TODO
    elements.sumOf { c -> area[c]!! * perimeter[c]!! }.let { println("Error: $it") }
}

private fun leftAndRight(x: Int, perimeter: MutableMap<Char, Long>, c: Char, a: String) {
    if (x == 0) perimeter[c] = (perimeter[c] ?: 0) + 1
    else if (a[x - 1] != c) perimeter[c] = (perimeter[c] ?: 0) + 1

    if (x == a.length - 1) perimeter[c] = (perimeter[c] ?: 0) + 1
    else if (a[x + 1] != c) perimeter[c] = (perimeter[c] ?: 0) + 1
}

private fun down(x: Int, perimeter: MutableMap<Char, Long>, c: Char, b: String) {
    val o = b[x]
    if (o != c) {
        perimeter[c] = (perimeter[c] ?: 0) + 1
        perimeter[o] = (perimeter[o] ?: 0) + 1
    }
}

private fun parser(file: Path, function: (Sequence<String>) -> (Long)): Long = file
    .useLines { lines -> return function(lines) }
