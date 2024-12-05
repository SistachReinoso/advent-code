package com.cucarell.advent.y2024

import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.useLines

private val file: Path = object {}.javaClass.getResource(
    //"/2024/05/demo"
    "/2024/05/input"
)!!.path.let(::Path)

fun main() {
    part1(file) // 143
    part2(file) // 123
}

private fun part1(file: Path) {
    file.useLines { lines ->
        val (pair, lists) = lines
            .mapNotNull { line ->
                when {
                    "|" in line -> line.split("|").let { (a, b) -> Pair(a.toLong(), b.toLong()) }
                    "," in line -> line.split(",").map(String::toLong)
                    line.isBlank() -> null
                    else -> TODO("ups parsing line: '$line'")
                }
            }
            .partition { element -> element is Pair<*, *> }
        val group = pair
            .filterIsInstance<Pair<Long, Long>>()
            .groupBy(keySelector = { it.first }, valueTransform = { it.second })

        lists
            .filterIsInstance<List<Long>>()
            .filter { list -> isCorrectOrder(list, group) }
            .sumOf { list -> list[list.size / 2] }
            .let(::println)
    }
}

private fun isCorrectOrder(list: List<Long>, group: Map<Long, List<Long>>) =
    list.withIndex().all { (i, k) ->
        group[k]?.all { v ->
            val find = list.indexOf(v)
            if (find == -1) true else i < find
        } ?: true
    }
/****************************************************************************************************/
/*                                             Part 2                                               */
/****************************************************************************************************/
private fun part2(file: Path) {
    file.useLines { lines ->
        val (pair, lists) = lines
            .mapNotNull { line ->
                when {
                    "|" in line -> line.split("|").let { (a, b) -> Pair(a.toLong(), b.toLong()) }
                    "," in line -> line.split(",").map(String::toLong)
                    line.isBlank() -> null
                    else -> TODO("ups parsing line: '$line'")
                }
            }
            .partition { element -> element is Pair<*, *> }
        val group = pair
            .filterIsInstance<Pair<Long, Long>>()
            .groupBy(keySelector = { it.first }, valueTransform = { it.second })

        lists
            .filterIsInstance<List<Long>>()
            .filter { list -> !isCorrectOrder(list, group) }
            .map { list -> correctList(list, group) }
            .sumOf { list -> list[list.size / 2] }
            .let(::println)
    }
}


private fun correctList(list: List<Long>, group: Map<Long, List<Long>>): List<Long> {
    var output = list
    do {
        output = correctOneTime(output, group)
    } while (!isCorrectOrder(output, group))
    return output
}

private fun correctOneTime(list: List<Long>, group: Map<Long, List<Long>>): List<Long> {
    val mutable = list.toMutableList()
    list.withIndex()
        .forEach onlyOne@{ (i, k) ->
            group[k]?.forEach { v ->
                val find = mutable.indexOf(v)
                if (find in 0..i) {
                    mutable[find] = k
                    mutable[i] = v
                    return@onlyOne
                }
            }
        }
    return mutable
}
