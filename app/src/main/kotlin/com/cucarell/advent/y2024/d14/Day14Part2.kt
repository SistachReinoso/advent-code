package com.cucarell.advent.y2024.d14

import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.useLines

fun main() {
    val file: Path = object {}.javaClass.getResource(
        //"/2024/14/demo" // 12
        "/2024/14/input"
    )!!.path.let(::Path)

    part2(
        file,
        //Coordinates(11, 7) // Example demo (12)
        Coordinates(101, 103)
    )
        .let(::println)
}

private fun part2(file: Path, size: Coordinates) {
    var robots = parser2(file, size)

    var seconds = 0
    while (true) {
        if (robots.map { r -> r.p }.distinct().size == robots.size) {
            printRobots(robots)
            println("\n===${seconds}===")
        }
        seconds++
        robots = robots.map { robot ->
            val c = robot.getPositionAfter(1)
            robot.copy(p = c)
        }
    }
}

private fun printRobots(list: List<Robot>) {
    val size = list.first().size
    val rowsI = 0..<size.y
    val colI = 0..<size.x

    rowsI
        .forEach { y ->
            colI.fold("") { acc, x ->
                val c = Coordinates(x, y)
                acc + if (list.any { r -> c == r.p }) "0" else " "
            }.let { str -> println(str) }
        }
}

private fun parser2(file: Path, size: Coordinates): List<Robot> {
    file.useLines { lines ->
        return lines.map { line ->
            val (px, py, vx, vy) = robotRegex.matchEntire(line)!!.destructured
            val p = Coordinates(px.toInt(), py.toInt())
            val v = Coordinates(vx.toInt(), vy.toInt())
            Robot(p, v, size)
        }.toList()
    }
}
