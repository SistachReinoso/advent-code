package com.cucarell.advent.y2024.d23

import java.nio.file.Path
import kotlin.io.path.Path

data class MultiplayerGame(val a: String, val b: String, val c: String): Comparable<MultiplayerGame> {
    override fun toString(): String = "$a,$b,$c"
    override fun compareTo(other: MultiplayerGame): Int = toString().compareTo(other.toString())

    companion object {
        fun of(list: List<String>): MultiplayerGame {
            val declare: List<String> = list.sorted()
            return MultiplayerGame(declare[0], declare[1], declare[2])
        }
    }
}

fun main() {
    val file: Path = object {}.javaClass.getResource("/2024/23/input")!!.path.let(::Path)
    println(part1(file))
}

fun part1(file: Path): Long {
    val map: Map<String, List<String>> = parser23(file)
    return getDiferentMultiplayerStartedByT(map)
        .let { return it.size.toLong() }
}

private fun getDiferentMultiplayerStartedByT(map: Map<String, List<String>>): Set<MultiplayerGame> = map.keys.toList()
    .filter { key: String -> key.startsWith("t") }
    .map { key: String ->
        recursivePermutation(map.getValue(key), 2)
            .filter { (a: String, b: String) -> b in map.getValue(a) }
            .map { list: List<String> -> MultiplayerGame.of(listOf(key) + list) }
            .toList()
    }.flatten()
    .toSet()

fun recursivePermutation(list: List<String>, int: Int): Sequence<List<String>> = sequence {
    if (int == 1) yieldAll(list.map { element: String -> listOf(element) })
    else
        list.indices.drop(1).map { i: Int ->
            val pivot: List<String> = listOf(list[i - 1])
            recursivePermutation(list.drop(i), int - 1)
                .forEach { newList: List<String> -> yield(pivot + newList) }
        }
}
