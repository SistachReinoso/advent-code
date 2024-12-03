package org.example

import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.useLines

val file: Path = object {}.javaClass.getResource(
    //"/03/demo"
    "/03/input"
)!!.path.let(::Path)

fun main() {
    part1(file) // 161
    part2(file) // 48
}

fun part1(file: Path) {
    val regex = """mul\((\d++),(\d++)\)""".toRegex()
    file.useLines { lines ->
        lines
            .sumOf { line ->
                regex
                    .findAll(line)
                    .sumOf { match ->
                        val (a, b) = match.destructured
                        a.toLong() * b.toLong()
                    }
            }
    }.let(::println)
}

fun part2(file: Path) {
    val regex = """mul\((?<a>\d++),(?<b>\d++)\)|(?<e>do\(\))|(?<d>don't\(\))""".toRegex()
    var enable: Boolean = true
    file.useLines { lines ->
        lines
            .sumOf { line ->
                regex
                    .findAll(line)
                    .sumOf { match ->
                        when {
                            match.groups["e"] != null -> { enable = true ; 0L }
                            match.groups["d"] != null -> { enable = false; 0L }
                            enable -> {
                                val a = match.groups["a"]!!.value.toLong()
                                val b = match.groups["b"]!!.value.toLong()
                                a * b
                            }
                            else -> 0L
                        }
                    }
            }
    }.let(::println)
}
