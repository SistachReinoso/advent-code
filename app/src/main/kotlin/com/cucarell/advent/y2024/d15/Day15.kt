package com.cucarell.advent.y2024.d15

import com.cucarell.advent.utils.CoordinatesMove
import com.cucarell.advent.utils.Map2D
import com.cucarell.advent.utils.MutableCoordinates
import java.nio.file.Path
import kotlin.io.path.Path

fun main() {
    val file: Path = object {}.javaClass.getResource(
        //"/2024/15/demo" // 10092
        //"/2024/15/demo2"
        "/2024/15/input"
    )!!.path.let(::Path)

    part1(file)
        .let(::println)
}

fun part1(file: Path): Long {
    val (map2d, moves) = parser(file)
    // TODO println(map2d.toString(empty = '.')); println(moves)

    val r = moveRobot(map2d, moves)
    //println(map2d.toString(empty = '.'))
    return r
}

private fun moveRobot(map2D: Map2D, moves: List<CoordinatesMove>): Long {
    val robot: MutableCoordinates = map2D.getElement(Parser.ROBOT) as MutableCoordinates
    val wall = map2D.getCollection(Parser.WALL)
    val boxes: Collection<MutableCoordinates> = map2D.getCollection(Parser.BOX) as Collection<MutableCoordinates>

    moves.forEach { movement: CoordinatesMove ->
        getBoxesToMove(movement, robot, map2D)
            .forEach { box -> box.move(movement) }
        // println("Move: $movement\n${map2D.toString(' ')}") // TODO
    }

    return boxes.sumOf { c -> c.x + 100L * c.y }
}

private fun getBoxesToMove(
    movement: CoordinatesMove,
    robot: MutableCoordinates,
    map2D: Map2D
): List<MutableCoordinates> {
    var move: MutableCoordinates = robot.getMovement(movement).toMutable()
    val boxes: Collection<MutableCoordinates> = map2D.getCollection(Parser.BOX) as Collection<MutableCoordinates>

    val solutions: MutableList<MutableCoordinates> = mutableListOf(robot)
    while (true) {
        val enum = map2D.getEnumCharOrNull(move)
        return when (enum) {
            null -> solutions
            Parser.WALL -> emptyList()
            Parser.BOX -> {
                move = boxes.first { b -> b == move }
                solutions.add(move)
                move = move.getMovement(movement).toMutable()
                continue
            }

            else -> TODO("curios: ${map2D.getEnumCharOrNull(move)}")
        }
    }
}
