package com.cucarell.advent.y2024

import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.useLines

fun main() {
    val file: Path = object {}.javaClass.getResource(
        //"/2024/11/demo"
        "/2024/11/input"
    )!!.path.let(::Path)

    part1(file, 75) // 55312 (25) 26(6)
        .let(::println)
}

private fun part1(file: Path, steps: Int): Long {
    val stones = parser(file)

    return stones.sumOf { stone ->
        val a = countStone(stone, steps)
        println("stone: $stone, steps $steps, total: $a")
        a
    }
}

private val cache: MutableMap<Pair<Long, Int>, Long> = mutableMapOf()
private fun withCache(stone: Long, steps: Int, action: (stone: Long, steps: Int) -> (Long)): Long {
    val a = cache.getOrPut(Pair(stone, steps)) { action(stone, steps) }
    return a
}

private fun countStone(stone: Long, steps: Int): Long = withCache(stone, steps) { stone, steps ->
    if (steps == 0) return@withCache 1
    if (stone == 0L) return@withCache countStone(1L, steps - 1)

    val string = stone.toString()
    if (string.length % 2 == 0) {
        val (left, right) = string.chunked(string.length / 2).map { e -> e.toLong() }
        return@withCache countStone(left, steps - 1) + countStone(right, steps - 1)
    }
    return@withCache countStone(stone * 2024, steps - 1)
}

private fun parser(file: Path): List<Long> =
    file.useLines { lines -> lines.first().split("""\s""".toRegex()).map { e -> e.toLong() } }
