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
    part2(file) // 2858
    part3(file) // 2858
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

/****************************************************************************************************/
/*                                             Part 2                                               */
/****************************************************************************************************/

private fun part2(file: Path) {
    val input = parser(file)
    val list = generateList(input)
    val map = generateMap(input)
    val count = list.last() as Long
    val emptyList = emptyListIndexed(list)

    (count downTo 1)
        .forEach { k ->
            val elements = map[k]!!
            val emptyRange = emptyList.withIndex().find { (_, range) -> elements <= range.len() }

            if (emptyRange == null) return@forEach
            list.withIndex().filter { (_, v) -> v == k }.forEach { (i, _) -> list[i] = null }
            emptyRange.value.run { first..<first + elements }.forEach { i -> list[i] = k }

            // println(list) // debug
            if (emptyRange.value.len() == elements) {
                emptyList.removeAt(emptyRange.index)
            } else {
                val newRange = emptyRange.value.run { first + elements..last }
                emptyList[emptyRange.index] = newRange
            }
        }

    list
        .withIndex().filter { (_, v) -> v != null }
        //.filterNotNull().withIndex()
        .sumOf { (i, v) -> i * v!! }.let(::println)
}

private fun IntRange.len() = last - first + 1

private fun emptyListIndexed(list: List<Long?>): MutableList<IntRange> {
    val result = mutableListOf<IntRange>()
    list.withIndex()
        .filter { (_, v) -> v == null }
        .forEach { (i, _) ->
            val r = result.lastOrNull()
            if ((r?.last ?: -2) + 1 == i) result[result.size - 1] = r!!.first..i
            else result.add(i..i)
        }
    return result
}

private fun generateList(input: String): MutableList<Long?> = input
    .map { c -> c.digitToInt() }
    .withIndex()
    .map { (i, v) ->
        if (i % 2 == 0) List(v) { i / 2L }
        else List(v) { null }
    }
    .flatten()
    .toMutableList()

private fun part3(file: Path) {
    val parsed = parser(file)
    getPart2Sequence(parsed)
        .withIndex().filter { (_, v) -> v != null }
        .sumOf { (i, v) -> i * v!! }.let(::println)
}

private fun getPart2Sequence(input: String): Sequence<Long?> = sequence {
    val elements = getElements(input)

    input.map { v -> v.digitToInt() }.withIndex()
        .map { (i, v) ->
            if (i % 2 == 0) {
                val id = i / 2
                if (elements[id] == 0) {
                    yieldAll(List(v) { null })
                    return@map
                }
                elements[id] = 0
                repeat(v) { yield(id.toLong()) }
            } else {
                var empties = v
                while (empties != 0) {
                    val id = elements.indexOfLast { e -> e in 1..empties }
                    if (id == -1) {
                        yieldAll(List(empties) { null })
                        return@map
                    }
                    val repeat = elements[id]
                    empties -= repeat
                    elements[id] = 0
                    repeat(repeat) { yield(id.toLong()) }
                }
            }
        }
}

private fun getElements(input: String): MutableList<Int> = input
    .withIndex().filter { (i, _) -> i % 2 == 0 }
    .map { (_, c) -> c.digitToInt() }
    .toMutableList()
