package com.cucarell.advent.y2024.d19

import java.nio.file.Path
import kotlin.io.path.Path

private val cache: MutableMap<String, Long> = mutableMapOf()
fun decorateInt(key: String, action: (String) -> (Long)): Long = cache.getOrPut(key) { action(key) }

fun main() {
    val file: Path = object {}.javaClass.getResource("/2024/19/input")!!.path.let(::Path)
    part2(file)
        .let(::println)
}

data class TowelsData(val towels: Set<String>, val sizes: List<Int>) {
    companion object {
        fun of(towels: Collection<String>): TowelsData = TowelsData(
            towels = towels.toSet(),
            sizes = towels.map { towel -> towel.length }.distinct().sorted()
        )
    }
}

fun part2(file: Path): Long {
    val parsed: Parser19 = parser19(file)
    val towels: TowelsData = TowelsData.of(parsed.towels)

    return parsed.designs.sumOf { design -> numberCombinations(design, towels) }
}


fun numberCombinations(design: String, towels: TowelsData): Long = decorateInt(key = design) {
    towels.sizes.filter { i: Int -> i in 1..<design.length }
        .filter { i: Int -> design.take(i) in towels.towels }
        .sumOf { i: Int -> numberCombinations(design.drop(i), towels) } +
            if (design in towels.towels) 1L else 0L
}
