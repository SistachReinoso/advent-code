package com.cucarell.advent.y2024.d14

import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.useLines

fun main() {
    val file: Path = object {}.javaClass.getResource(
        //"/2024/14/demo" // 12
        "/2024/14/input"
    )!!.path.let(::Path)

    part1(
        file,
        //Coordinates(11, 7) // Example demo (12)
        Coordinates(101, 103)
    )
        .let(::println)
}

data class Coordinates(val x: Int, val y: Int) {
    operator fun plus(o: Coordinates) = Coordinates(x + o.x, y + o.y)
    operator fun rem(o: Coordinates) = Coordinates(Math.floorMod(x, o.x), Math.floorMod(y, o.y))
}

operator fun Int.times(o: Coordinates): Coordinates = Coordinates(this * o.x, this * o.y)

data class Robot(val p: Coordinates, val v: Coordinates, val size: Coordinates) {
    fun getPositionAfter(seconds: Int): Coordinates = (p + seconds * v) % size
}

private fun part1(file: Path, size: Coordinates): Long {
    return parser(file, size, ::toPart1)
}

private fun toPart1(r: Robot): Solution {
    val c = r.getPositionAfter(seconds = 100)
    if (c.x == r.size.x / 2) return Solution(0, 0, 0, 0)
    if (c.y == r.size.y / 2) return Solution(0, 0, 0, 0)

    val sol = if (c.x < r.size.x / 2) {
        if (c.y < r.size.y / 2) Solution(1, 0, 0, 0)
        else Solution(0, 1, 0, 0)
    } else {
        if (c.y < r.size.y / 2) Solution(0, 0, 1, 0)
        else Solution(0, 0, 0, 1)
    }

    //println("DBG: $c: $sol") // TODO
    return sol
}

private data class Solution(val a: Int, val b: Int, val c: Int, val d: Int) {
    fun toSolution(): Long = a.toLong() * b * c * d
    operator fun plus(o: Solution) = Solution(a + o.a, b + o.b, c + o.c, d + o.d)
}

val robotRegex = """^p=(-?\d++),(-?\d++) v=(-?\d++),(-?\d++)$""".toRegex()
private fun parser(file: Path, size: Coordinates, wii: (Robot) -> (Solution)): Long {
    file.useLines { lines ->
        lines.fold(Solution(0, 0, 0, 0)) { acc, line ->
            val (px, py, vx, vy) = robotRegex.matchEntire(line)!!.destructured
            val p = Coordinates(px.toInt(), py.toInt())
            val v = Coordinates(vx.toInt(), vy.toInt())
            val robot = Robot(p, v, size)

            acc + wii(robot)
        }
            .toSolution()
            .let { return it }
    }
}
