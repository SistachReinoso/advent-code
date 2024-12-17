package com.cucarell.advent.y2024.d16

import com.cucarell.advent.utils.Coordinates
import com.cucarell.advent.utils.Map2DObstacleCollection
import com.cucarell.advent.utils.Map2DObstacleElement
import com.cucarell.advent.utils.Map2DVersion2
import com.cucarell.advent.utils.MutableCoordinates
import java.nio.file.Path
import kotlin.String
import kotlin.io.path.useLines

enum class Parser(val char: Char, val id: String) {
    REINDER('S', "Reinder"),
    END('E', "End"),
    WALL('#', "Wall"),
    EMPTY('.', "Empty");
}

fun parser(file: Path): Map2DVersion2 {
    file.useLines { lines ->
        var weight: Int? = null
        var height: Int = 0

        val walls: MutableSet<Coordinates> = mutableSetOf()
        var end: Coordinates? = null
        var reinder: MutableCoordinates? = null

        lines.forEachIndexed { y, line ->
            if (weight == null) weight = line.length
            height = y
            line.forEachIndexed { x, char ->
                val c = Coordinates(x = x, y = y)
                when (char) {
                    Parser.REINDER.char -> if (reinder == null) reinder = c.toMutable() else TODO("Render 2 times: $reinder, new $c")
                    Parser.END.char -> if (end == null) end = c else TODO("Ending 2 times: $end, new $c")
                    Parser.WALL.char -> walls.add(c)
                    Parser.EMPTY.char -> Unit
                    else -> TODO("unexpected char on parsing: '$char'")
                }
            }
        }
        return Map2DVersion2(
            weight = weight!!, height = ++height,
            obstacles = listOf(
                Map2DObstacleCollection(id = Parser.WALL.id, char = Parser.WALL.char, collection = walls),
                Map2DObstacleElement(id = Parser.END.id, char = Parser.END.char, coordinates = end!!),
                Map2ObstacleReindeer(id = Parser.REINDER.id, coordinates = reinder!!)
            )
        )
    }
}
