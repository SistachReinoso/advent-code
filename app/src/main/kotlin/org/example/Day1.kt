/*
 * This source file was generated by the Gradle 'init' task
 */
package org.example

import kotlin.io.path.*
import kotlin.math.abs

val d1 = """
3   4
4   3
2   5
1   3
3   9
3   3
"""

fun distance(a: Int, b: Int): Int = abs(a - b)

fun main() {
    val file = object {}.javaClass.getResource("/day1.txt")!!.file
    val path = Path(file)
    if (path.notExists()) TODO("Sembla que no existeix el path: $path")

    val d2 = path.readText()

    parser(d2)
        .let(::day1)
}

private fun parser(d2: String): List<List<Int>> {
    val list = d2
        .split("\n")
        .filter { line -> line.isNotBlank() }
        .map { line ->
            line
                .split("""\s++""".toRegex())
                .mapNotNull { element -> element.toIntOrNull() }
        }
    println(list)
    return list
}

private fun day1(list: List<List<Int>>) {
    val left = list.map { numbers -> numbers[0] }.sorted()
    val right = list.map { numbers -> numbers[1] }.sorted()

    println(left)
    println(right)

    val zip = left zip right

    println(zip)

    zip.sumOf { (e, d) -> distance(e, d) }.let(::println)
}