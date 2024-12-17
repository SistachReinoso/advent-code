package com.cucarell.advent.y2024.d15

import com.cucarell.advent.utils.CoordinatesInterface
import com.cucarell.advent.utils.CoordinatesMove
import com.cucarell.advent.utils.Map2DObstacleInterface
import com.cucarell.advent.utils.Map2DVersion2
import com.cucarell.advent.utils.MutableCoordinates
import java.nio.file.Path
import kotlin.io.path.Path

fun main() {
    val file: Path = object {}.javaClass.getResource(
        "/2024/15/demo" // 10092
        //"/2024/15/demo2"
        //"/2024/15/input"
    )!!.path.let(::Path)

    part2(file)
        .let(::println)
}

class Map2ObstacleBox(val collection: Collection<MutableCoordinates>): Map2DObstacleInterface {
    override fun contains(o: CoordinatesInterface): Boolean = collection
        .any { e -> o == e || o == e.getMovement(CoordinatesMove.RIGHT) }
    override fun getChar(c: CoordinatesInterface) = if (c in collection) '[' else ']'
}

fun part2(file: Path): Long {
    val (map2d: Map2DVersion2, moves) = parserPart2(file)
    println(map2d.toString()); println(moves) // TODO

    val r = 42L// TODOmoveRobot(map2d, moves)
    //println(map2d.toString(empty = '.'))
    return r
}
