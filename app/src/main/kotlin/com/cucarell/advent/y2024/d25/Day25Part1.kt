package com.cucarell.advent.y2024.d25

import java.nio.file.Path
import kotlin.io.path.Path

fun main() {
    val file: Path = object {}.javaClass.getResource("/2024/25/input")!!.path.let(::Path)
    part1(file)
        .let { result -> println(result) }
}

fun part1(file: Path): Long {
    val (keys: List<Schematic>, locks: List<Schematic>) = parser25(file).partition { s: Schematic -> s is Schematic.Key }
    return keys
        .sumOf { k: Schematic -> locks.count { l: Schematic -> k.withoutOverlap(l) } }
        .toLong()
}
