package com.cucarell.advent.y2024.d18

import com.cucarell.advent.utils.Coordinates
import com.cucarell.advent.utils.Map2DObstacleCollection
import com.cucarell.advent.utils.Map2DVersion2
import java.nio.file.Path
import kotlin.io.path.Path

fun main() {
    val file: Path = object {}.javaClass.getResource("/2024/18/input")!!.path.let(::Path)
    part2(size = 70, fallen = 1024, file = file)
        .let(::println)
}

fun part2(size: Int, fallen: Int, file: Path): String {
    val (map2d: Map2DVersion2, coordinates: List<Coordinates>) = parsePart2(file = file, size = size)
    val iterator: Iterator<Coordinates> = putMoreWalls(coordinates, map2d).iterator()
    val end = Coordinates(size, size)

    repeat(fallen - 1) { iterator.next() }

    val block = resolveMazePart2(map2d = map2d, end = end, iterator = iterator)
    return "${block.x},${block.y}"
}

fun putMoreWalls(coordinates: List<Coordinates>, map2d: Map2DVersion2) = sequence<Coordinates> {
    val mutablePointer = (map2d.getObstacle(ParseDay18.WALL.id) as Map2DObstacleCollection)
        .collection as MutableSet<Coordinates>

    coordinates.forEach { c: Coordinates ->
        mutablePointer.add(c)
        yield(c)
    }
}

fun resolveMazePart2(map2d: Map2DVersion2, end: Coordinates, iterator: Iterator<Coordinates>): Coordinates {
    val start = Coordinates(0, 0)
    val alive: Set<Alive> = setOf(Alive(start, listOf(start), end))
    var last: Coordinates = iterator.next()
    while (true) {
        val output = resolveMaze(map2d, end, alive.toMutableSet())
        //println(); println(map2d.toString(other = Pair('O', output))); println(last) // TODO
        if (output.isEmpty()) return last
        last = nextBlock(iterator, output)
    }
}

fun nextBlock(iterator: Iterator<Coordinates>, output: List<Coordinates>): Coordinates {
    iterator.forEach { newBlock: Coordinates ->
        if (newBlock in output) {
            return newBlock
        }
    }
    error("No more blocks \\O.o/")
}
