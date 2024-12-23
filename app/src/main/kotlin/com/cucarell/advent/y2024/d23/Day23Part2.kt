package com.cucarell.advent.y2024.d23

import java.nio.file.Path
import kotlin.io.path.Path

fun main() {
    val file: Path = object {}.javaClass.getResource("/2024/23/input")!!.path.let(::Path)
    println(part2(file))
}

fun part2(file: Path): String {
    val map: Map<String, Set<String>> = parser23(file)
        .mapValues { it.value.toSet() }

    return getMaximumLAN(map)
        .sorted().joinToString(",")
}

fun getMaximumLAN(map: Map<String, Set<String>>): Set<String> =
    map.map { map2 ->
        val newMax = getMaximumLan2(map, setOf(map2.key) + map2.value)
        if (cacheMaximumLong < newMax.size) cacheMaximumLong = newMax.size.toLong()
        newMax
    }
        .maxBy { set: Set<String> -> set.size }

// TODO el decorador també tindrá valor mínim, si detecta que no, elimina el resultat (?)
val cacheMaximumMap: MutableMap<Set<String>, Set<String>> = mutableMapOf()
var cacheMaximumLong: Long = 0
fun cacheMaximumLan(set: Set<String>, action: () -> (Set<String>)): Set<String> {
    if (set.size <= cacheMaximumLong) return emptySet()
    return cacheMaximumMap.getOrPut(set) { action() }
}

fun getMaximumLan2(map: Map<String, Set<String>>, set: Set<String>): Set<String> = cacheMaximumLan(set) {
    if (checkLan(map, set = set)) return@cacheMaximumLan set

    return@cacheMaximumLan lessOneElement(set)
        .map { newList: List<String> -> getMaximumLan2(map, newList.toSet()) }
        .maxBy { newSet: Set<String> -> newSet.size }
}

fun lessOneElement(set: Set<String>): List<List<String>> {
    if (set.size in 0..1) return emptyList()

    val list = set.toList()
    val indices = list.indices
    return indices.map { i: Int -> list.slice(indices - i) }
}

// TODO this to utils(?)
fun recursivePermutation2(list: List<String>, int: Int): Sequence<List<String>> = sequence {
    when {
        list.size < int -> Unit
        list.size == int -> yield(list)
        int == 1 -> list.forEach { element: String -> yield(listOf(element)) }
        else -> {
            list.indices.drop(1).map { i: Int ->
                val pivot: List<String> = listOf(list[i - 1])
                recursivePermutation(list.drop(i), int - 1)
                    .forEach { newList: List<String> -> yield(pivot + newList) }
            }
        }
    }
}

val cacheCheckMap: MutableMap<Set<String>, Boolean> = mutableMapOf()
fun cacheCheckLan(set: Set<String>, action: () -> (Boolean)): Boolean = cacheCheckMap.getOrPut(set) { action() }
fun checkLan(map: Map<String, Set<String>>, set: Set<String>): Boolean = cacheCheckLan(set) {
    val list: List<String> = set.toList()
    if (list.size in 0..1) return@cacheCheckLan true
    val key: Set<String> = map.getValue(list.first())
    val drop: List<String> = list.drop(1)

    if (!drop.all { element: String -> element in key }) return@cacheCheckLan false
    return@cacheCheckLan checkLan(map, drop.toSet())
}
