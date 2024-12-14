package com.cucarell.advent.y2024.d13

import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.useLines

fun main() {
    val file: Path = object {}.javaClass.getResource(
        //"/2024/13/demo" //   480
        "/2024/13/input"
    )!!.path.let(::Path)

    part1(file) // 80, 436
        .let(::println)
}

private data class Coordinates(val x: Long, val y: Long) {
    constructor(x: String, y: String) : this(x.toLong(), y.toLong())
}

fun part1(file: Path): Long {
    return parser(file, ::systemLinearEquationByReduction)
}

private fun systemLinearEquationByReduction(a: Coordinates, b: Coordinates, p: Coordinates): Long {
    val numerador = p.x * a.y - p.y * a.x
    val denominador = b.x * a.y - b.y * a.x

    if (numerador % denominador != 0L) return 0L

    val sb = numerador / denominador
    val san = p.x - b.x * sb

    if (san % a.x != 0L) return 0L

    val sa = san / a.x

    return if (sb >= 0L && sa >= 0L)
        sa * 3L + sb
    else 0L
}

private fun parser(file: Path, function: (a: Coordinates, b: Coordinates, p: Coordinates) -> (Long)): Long =
    file.useLines { lines ->
        lines.chunked(4)
            .sumOf { chunked ->
                val a = parseButton(chunked[0])
                val b = parseButton(chunked[1])
                val p1 = parsePrice(chunked[2])
                val p = Coordinates(x = p1.x + 10000000000000L, y = p1.y + 10000000000000L)

                //val sol = function(a, b, p); println("A $a, B $b, p $p: sol $sol"); sol
                function(a, b, p)
            }
    }

private val buttonRegex = """^Button [AB]: X([+-]\d++), Y([+-]\d++)$""".toRegex()
private fun parseButton(input: String): Coordinates {
    val (x, y) = buttonRegex.matchEntire(input)!!.destructured
    return Coordinates(x, y)
}

private val prizeRegex = """^Prize: X=(\d++), Y=(\d++)$""".toRegex()
private fun parsePrice(input: String): Coordinates {
    val (x, y) = prizeRegex.matchEntire(input)!!.destructured
    return Coordinates(x, y)
}
