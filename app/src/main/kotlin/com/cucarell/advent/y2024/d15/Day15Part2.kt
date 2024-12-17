package com.cucarell.advent.y2024.d15

import com.cucarell.advent.utils.*
import java.nio.file.Path
import kotlin.io.path.Path

fun main() {
    val file: Path = object {}.javaClass.getResource(
        //"/2024/15/demo" // 9021
        //"/2024/15/demo3"
        "/2024/15/input"
    )!!.path.let(::Path)

    part2(file)
        .let(::println)
}

class Map2ObstacleBox(override val id: String, val collection: Collection<MutableCoordinates>) :
    Map2DObstacleInterface {
    override fun getCoordinatesPointer(c: CoordinatesInterface): List<CoordinatesInterface> = collection
        .filter { e -> c in Map2ObstacleBox("", listOf(e)) }

    override fun getMovementCoordinates(c: CoordinatesInterface, move: CoordinatesMove): List<CoordinatesInterface> {
        val elements = collection.filter { e -> c in Map2ObstacleBox("", listOf(e)) }
        return when (move) {
            CoordinatesMove.LEFT -> elements.map { e -> e.getMovement(move) }
            CoordinatesMove.RIGHT -> elements.map { e -> e.getMovement(move).getMovement(move) }
            CoordinatesMove.UP, CoordinatesMove.DOWN -> elements
                .map { e -> listOf(e, e.getMovement(CoordinatesMove.RIGHT)) }
                .flatten()
                .map { e -> e.getMovement(move) }
        }
    }

    override fun contains(o: CoordinatesInterface): Boolean = collection
        .any { e ->
            // TODO println("contains: $o in $e: ${e == o} || $e == ${o.getMovement(CoordinatesMove.RIGHT)} ${e == o.getMovement(CoordinatesMove.RIGHT)}")
            e == o || e == o.getMovement(CoordinatesMove.LEFT)
        }

    override fun getChar(oi: CoordinatesInterface): Char {
        val o = Coordinates(oi)
        val r = if (collection.any { c -> o == c }) '['
        else ']'
        return r
    }
}

fun part2(file: Path): Long {
    val (map2d: Map2DVersion2, moves) = parserPart2(file)
    // println("Initial state:"); println(map2d); println(moves) // TODO

    val r = moveRobot(map2d, moves)
    // TODO println(map2d)
    return r
}

private fun moveRobot(map2d: Map2DVersion2, moves: List<CoordinatesMove>): Long {
    val boxes = (map2d.getObstacle(Parser.BOX.char.toString()) as Map2ObstacleBox).collection

    moves.forEach { movement: CoordinatesMove ->
        // TODO println("Move: $movement")
        val pointers = getPointersToMove(movement, map2d)
        pointers.forEach { p -> p.move(movement) }

        //println(map2d) // TODO
    }

    return boxes.sumOf { c -> c.x + 100L * c.y }
}

private fun getPointersToMove(
    movement: CoordinatesMove,
    map2d: Map2DVersion2
): List<MutableCoordinates> {
    val robot: Map2DObstacleElement = map2d.getObstacle(Parser.ROBOT.char.toString()) as Map2DObstacleElement
    val pointersSolution: MutableList<MutableCoordinates> = mutableListOf(robot.coordinates as MutableCoordinates)
    val coordinatesCheck: MutableList<CoordinatesInterface> = robot.getMovementCoordinates(movement)
        .toMutableList()

    while (coordinatesCheck.isNotEmpty()) {
        val next: CoordinatesInterface = coordinatesCheck.removeLast()

        val obstacle: Map2DObstacleInterface? = map2d.getObstacleOrNull(next)
        when {
            obstacle == null -> continue
            obstacle.id == Parser.WALL.char.toString() -> return emptyList()
            obstacle.id == Parser.BOX.char.toString() -> {

                obstacle
                    .getCoordinatesPointer(next)
                    .map { e -> e as MutableCoordinates }
                    .let { p -> pointersSolution.addAll(p) }

                obstacle
                    .getMovementCoordinates(next, movement)
                    .let { v -> coordinatesCheck.addAll(v) }
            }
        }
    }

    return pointersSolution.distinct()
}
