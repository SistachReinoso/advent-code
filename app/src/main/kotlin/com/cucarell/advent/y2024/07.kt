package com.cucarell.advent.y2024

import com.google.common.math.LongMath.pow
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.useLines

val NON_DIGIT_REGEX = """\D++""".toRegex()

fun main() {
    val file: Path = object {}.javaClass.getResource(
        //"/2024/07/demo"
        "/2024/07/input"
    )!!.path.let(::Path)

    part1(file) // 3749
    //part2(file) //
}

private fun part1(file: Path) {
    file.useLines { lines ->
        lines.map(String::parser)
            .sumOf { (result, list) ->
                val operations = list.zipWithNext()
                val operationsSize = operations.size

                val r = (0L..<pow(2L, operationsSize))
                    .any { s: Long ->
                        result == s.toString(2).padStart(operations.size, '0')
                            .withIndex()
                            .fold(list.first()) { acc, (i, c) ->
                                val next = list[i + 1]
                                when (c) {
                                    '0' -> acc + next
                                    '1' -> acc * next
                                    else -> TODO("unexpected char: '$c'")
                                }
                            }
                    }
                if (r) result else 0
            }
            .let(::println)
    }
}

private fun String.parser() =
    this.split(NON_DIGIT_REGEX).map { element -> element.toLong() }.let { list -> Pair(list.first(), list.drop(1)) }
