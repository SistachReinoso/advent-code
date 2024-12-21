package com.cucarell.advent.y2024.d19

import java.nio.file.Path
import kotlin.io.path.Path

fun main() {
    val file: Path = object {}.javaClass.getResource("/2024/19/input")!!.path.let(::Path)
    part1(file)
        .let(::println)
}

private val cache: MutableMap<String, Boolean> = mutableMapOf()
fun decoratorCache(key: String, action: (String) -> (Boolean)): Boolean = cache.getOrPut(key) { action(key) }

fun part1(file: Path): Int {
    val parsed = parser19(file)
    cache.putAll(parsed.towels.associate { towel: String -> towel to true })

    return parsed.designs.count { design -> isPossibleDesign(design) }
}

fun isPossibleDesign(design: String): Boolean = decoratorCache(key = design) {
    return@decoratorCache design.indices.drop(1).any { i: Int ->
        isCombiningTwoDesign(design.take(i)) && isPossibleDesign(design.drop(i))
    }
}

fun isCombiningTwoDesign(design: String): Boolean = decoratorCache(key = design) {
    return@decoratorCache design.indices.drop(1).any { i: Int ->
        cache.getOrPut(design.take(i)) { false } && cache.getOrDefault(design.drop(i), false)
    }
}
