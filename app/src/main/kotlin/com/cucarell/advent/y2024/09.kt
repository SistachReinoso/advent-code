package com.cucarell.advent.y2024

import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.useLines

fun main() {
    val file: Path = object {}.javaClass.getResource(
        //"/2024/09/demo"
        "/2024/09/input"
    )!!.path.let(::Path)

    part1(file) // 1928
//    part2(file) // ??
}

private fun part1(file: Path) {
    val input = parser(file)
    val reserved = reversed(input).iterator()
    val map = generateMap(input)
    inOrder(input, reserved, map.toMutableMap())
        // debug .toList().joinToString("") { it.toString() }.let(::println)
        .withIndex().sumOf { (i, v) -> i * v }.let(::println)
}

private fun inOrder(input: String, reserved: Iterator<Long>, map: MutableMap<Long, Int>): Sequence<Long> = sequence {
    input.withIndex()
        .map { (i, c) ->
            if (i % 2 == 0) {
                val id = i / 2L
                val total = map[id]!!
                if (total == 0) return@map
                val repeat = c.digitToInt()

                if (total >= repeat) {
                    map[id] = total - repeat
                    repeat(repeat) { yield(id) }
                } else {
                    map[id] = 0
                    repeat(total) { yield(id) }
                }
            } else {
                repeat(c.digitToInt()) {
                    val id = reserved.next()
                    val total = map[id]!!
                    if (total == 0) return@repeat

                    map[id] = total - 1
                    yield(id)
                }
            }
        }
    // TODO check all values of map == 0
}

private fun reversed(input: String): Sequence<Long> = sequence {
    input.withIndex().reversed()
        .forEach { (i, c) ->
            if (i % 2 != 0) return@forEach
            val r = i / 2L
            repeat(c.digitToInt()) {
                yield(r)
            }
        }
}

private fun generateMap(input: String): Map<Long, Int> = input
    .withIndex().filter { (i, _) -> i % 2 == 0 }
    .associateBy(keySelector = { (i, _) -> i / 2L }, valueTransform = { (_, c) -> c.digitToInt() })

private fun parser(file: Path): String = file.useLines { lines -> lines.first() }
