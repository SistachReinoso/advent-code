package com.cucarell.advent.y2024

import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.useLines

fun main() {
    val file: Path = object {}.javaClass.getResource(
        "/2024/11/demo"
        //"/2024/11/input"
    )!!.path.let(::Path)

    part1(file) // 36
}

private fun part1(file: Path) {
    val list = parser(file)
    brutus1(list)
}

private fun brutus1(list: List<String>) {
    repeat(6) {
        TODO()
    }
}

private fun parser(file: Path): List<String> = file.useLines { lines -> lines.first().split("""\s""".toRegex()) }
