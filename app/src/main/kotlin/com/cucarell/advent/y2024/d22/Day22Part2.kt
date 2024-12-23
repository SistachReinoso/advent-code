package com.cucarell.advent.y2024.d22

import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.useLines

fun main() {
    val file: Path = object {}.javaClass.getResource("/2024/22/input")!!.path.let(::Path)
    part2(file).let(::println)
}

fun part2(file: Path): Long {
    file.useLines { lines: Sequence<String> ->
        val mapsChanges = lines
            .map { line: String -> line.toLong() }
            .map { secret: Long -> generateChanges(secret = secret, repeat = 2000) }
            .toList()

        return mapsChanges
            .flatMap { it.keys }
            .toSet()
            .maxOf { sequence: FourConsecutiveChanges ->
                mapsChanges.sumOf { map: Map<FourConsecutiveChanges, Long> ->
                    map[sequence] ?: 0L
                }
            }
    }
}

data class BananasAndChange(val bananas: Long, val changes: Long)
data class FourConsecutiveChanges(val a: Long, val b: Long, val c: Long, val d: Long)

fun generateChanges(secret: Long, repeat: Int): Map<FourConsecutiveChanges, Long> {
    val map : MutableMap<FourConsecutiveChanges, Long> = mutableMapOf()
    pseudorandomSequence(input = secret, repeat = repeat)
        .zipWithNext { a: Long, b: Long -> BananasAndChange(b, b - a) }
        .windowed(4)
        .forEach { (a, b, c, d) ->
            map.putIfAbsent(
                FourConsecutiveChanges(a.changes, b.changes, c.changes, d.changes),
                d.bananas
            )
        }
    return map
}

private fun pseudorandomSequence(input: Long, repeat: Int): Sequence<Long> = sequence {
    var secret: Long = input
    yield(secret % 10)
    repeat(repeat - 1) {
        secret = pseudorandom(secret)
        yield(secret % 10)
    }
}

private fun pseudorandom(input: Long): Long {
    var secret: Long = input
    secret = (secret shl 6 xor secret) % 16777216
    secret = (secret shr 5 xor secret) % 16777216
    secret = (secret shl 11 xor secret) % 16777216
    return secret
}
