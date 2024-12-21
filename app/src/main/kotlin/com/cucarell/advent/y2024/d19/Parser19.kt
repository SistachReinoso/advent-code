package com.cucarell.advent.y2024.d19

import java.nio.file.Path
import kotlin.io.path.useLines

data class Parser19(val towels: List<String>, val designs: List<String>)

fun parser19(file: Path): Parser19 {
    file.useLines { lines: Sequence<String> ->
        val iterator: Iterator<String> = lines.iterator()
        val towels: List<String> = iterator.next().split(", ")
        iterator.next()
        val designs: List<String> = iterator.asSequence().toList()
        return Parser19(towels = towels, designs = designs)
    }
}
