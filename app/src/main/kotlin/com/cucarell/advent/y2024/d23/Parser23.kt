package com.cucarell.advent.y2024.d23

import java.nio.file.Path
import kotlin.io.path.useLines

fun parser23(file: Path): Map<String, List<String>> {
    file.useLines { lines: Sequence<String> ->
        return lines.map { line: String ->
            val (a: String, b: String) = line.split("-", limit = 2)
            listOf(a to b, b to a)
        }.flatten()
            .groupBy(keySelector = { (k: String, _) -> k }, valueTransform = { (_, v: String) -> v })
    }
}
