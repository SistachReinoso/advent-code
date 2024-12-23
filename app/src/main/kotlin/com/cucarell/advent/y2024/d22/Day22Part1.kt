package com.cucarell.advent.y2024.d22

import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.useLines

fun main() {
    val file: Path = object {}.javaClass.getResource("/2024/22/input")!!.path.let(::Path)
    part1(file).let(::println)
}

fun part1(file: Path): Long {
    file.useLines { lines: Sequence<String> ->
        return lines
            .map { line: String -> line.toLong() }
            .sumOf(::pseudorandom)
    }
}

// * 2048 === shl 11
// * 64 === shl 6
// / 32 === shr 5
private fun pseudorandom(init: Long): Long {
    var secret: Long = init

    repeat(2000) {
        secret = (secret shl 6 xor secret) % 16777216
        secret = (secret shr 5 xor secret) % 16777216
        secret = (secret shl 11 xor secret) % 16777216
    }

    return secret
}
