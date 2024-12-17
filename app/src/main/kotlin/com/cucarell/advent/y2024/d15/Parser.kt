package com.cucarell.advent.y2024.d15

import com.cucarell.advent.utils.Coordinates
import com.cucarell.advent.utils.CoordinatesMove
import com.cucarell.advent.utils.EnumChar
import com.cucarell.advent.utils.Map2DVersion1
import java.nio.file.Path
import kotlin.io.path.useLines

enum class Parser(override val char: Char): EnumChar {
    ROBOT('@'),
    BOX('O'),
    WALL('#'),
    EMPTY('.');
}

fun parser(file: Path): Pair<Map2DVersion1, List<CoordinatesMove>> {
    file.useLines { lines ->
        val iterable = lines.iterator()
        val map2d: Map2DVersion1 = parserMap2D(iterable)
        val orders: List<CoordinatesMove> = parserMovements(iterable.asSequence())
        return Pair(map2d, orders)
    }
}

private fun parserMap2D(iterable: Iterator<String>): Map2DVersion1 {
    var y = 0
    val walls: MutableSet<Coordinates> = mutableSetOf()
    val boxes: MutableSet<Coordinates> = mutableSetOf()
    var robot: Coordinates? = null
    var width: Int? = null

    while (iterable.hasNext()) {
        val line = iterable.next()
        if (line.isBlank()) break
        if (width == null) width = line.length

        line.forEachIndexed { x, char ->
            val c = Coordinates(x, y)
            when (char) {
                Parser.EMPTY.char -> Unit
                Parser.WALL.char -> walls.add(c)
                Parser.BOX.char -> boxes.add(c)
                Parser.ROBOT.char -> if (robot == null) robot = c else TODO("Robot: $robot, new $c")
                else -> TODO("Unexpected char: '$char'")
            }
        }

        y++
    }

    return Map2DVersion1(
        weight = width!!, height = y,
        collections = listOf(
            Pair(Parser.WALL, walls),
            Pair(Parser.BOX, boxes.map { b -> b.toMutable() })
        ),
        elements = listOf(Pair(Parser.ROBOT, robot!!.toMutable()))
    )
}

fun parserMovements(sequence: Sequence<String>): List<CoordinatesMove> {
    val movementsMap = CoordinatesMove.entries.associateBy { m -> m.char }
    return sequence.map { line ->
        line.map { char ->
            movementsMap[char] ?: TODO("Unexpected char: '$char'")
        }
    }.flatten().toList()
}
