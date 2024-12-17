package com.cucarell.advent.y2024.d15

import com.cucarell.advent.utils.CoordinatesMove
import com.cucarell.advent.utils.Map2DObstacleCollection
import com.cucarell.advent.utils.Map2DObstacleElement
import com.cucarell.advent.utils.Map2DVersion2
import com.cucarell.advent.utils.MutableCoordinates
import java.nio.file.Path
import kotlin.io.path.useLines

fun parserPart2(file: Path): Pair<Map2DVersion2, List<CoordinatesMove>> {
    file.useLines { lines ->
        val iterable = lines.iterator()
        val map2d: Map2DVersion2 = parserMap2D(iterable)
        val orders: List<CoordinatesMove> = parserMovements(iterable.asSequence())
        return Pair(map2d, orders)
    }
}

private fun parserMap2D(iterable: Iterator<String>): Map2DVersion2 {
    var y = 0
    val walls: MutableSet<MutableCoordinates> = mutableSetOf()
    val boxes: MutableSet<MutableCoordinates> = mutableSetOf()
    var robot: MutableCoordinates? = null
    var width: Int? = null

    while (iterable.hasNext()) {
        val line = iterable.next()
        if (line.isBlank()) break
        if (width == null) width = 2 * line.length

        line.forEachIndexed { x, char ->
            val c = MutableCoordinates(2 * x, y)
            when (char) {
                Parser.EMPTY.char -> Unit
                Parser.WALL.char -> {
                    walls.add(c)
                    walls.add(c.getMovement(CoordinatesMove.RIGHT).toMutable())
                }
                Parser.BOX.char -> boxes.add(c)
                Parser.ROBOT.char -> if (robot == null) robot = c else TODO("Robot: $robot, new $c")
                else -> TODO("Unexpected char: '$char'")
            }
        }

        y++
    }

    return Map2DVersion2(
        weight = width!!, height = y,
        obstacles = listOf(
            Map2DObstacleCollection(char = Parser.WALL.char, collection = walls),
            Map2ObstacleBox(boxes),
            Map2DObstacleElement(char = Parser.ROBOT.char, coordinates = robot!!)
        )
    )
}
