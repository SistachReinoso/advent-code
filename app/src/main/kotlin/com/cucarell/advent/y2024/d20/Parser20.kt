package com.cucarell.advent.y2024.d20

import com.cucarell.advent.utils.Coordinates
import com.cucarell.advent.utils.Map2DObstacleCollection
import com.cucarell.advent.utils.Map2DObstacleElement
import com.cucarell.advent.utils.Map2DVersion2
import java.nio.file.Path
import kotlin.io.path.useLines

fun parse20Raze(file: Path): Map2DVersion2 {
    file.useLines { lines ->
        var weight: Int? = null
        var height: Int = 0

        val walls: MutableSet<Coordinates> = mutableSetOf()
        var end: Coordinates? = null
        var start: Coordinates? = null

        lines.forEachIndexed { y: Int, line: String ->
            if (weight == null) weight = line.length
            height = y
            line.forEachIndexed { x: Int, char: Char ->
                val coordinates = Coordinates(x, y)
                when (Map20Elements.of(char)) {
                    Map20Elements.Track -> Unit
                    Map20Elements.START -> if (start == null) start = coordinates else error("start 2 times: $start and $coordinates")
                    Map20Elements.END -> if (end == null) end = coordinates else error("end 2 times: $end and $coordinates")
                    Map20Elements.WALL -> walls.add(coordinates)
                }
            }
        }

        return Map2DVersion2(
            weight = weight!!, height = ++height,
            obstacles = listOf(
                Map2DObstacleCollection(id = Map20Elements.WALL.id, char = Map20Elements.WALL.char, collection = walls),
                Map2DObstacleElement(id = Map20Elements.END.id, char = Map20Elements.END.char, coordinates = end!!),
                Map2DObstacleElement(id = Map20Elements.START.id, char = Map20Elements.START.char, coordinates = start!!)
            )
        )
    }
}
