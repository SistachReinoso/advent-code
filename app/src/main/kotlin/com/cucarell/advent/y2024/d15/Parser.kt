package com.cucarell.advent.y2024.d15

import com.cucarell.advent.utils.Coordinates
import com.cucarell.advent.utils.Map2D
import java.nio.file.Path
import kotlin.io.path.useLines

enum class Parser(val char: Char) {
    ROBOT('@'),
    BOX('O'),
    WALL('#'),
    EMPTY('.');
}

enum class Moves(val char: Char) {
    UP('^'),
    DOWN('v'),
    LEFT('<'),
    RIGHT('>');
}

fun parser(file: Path): Pair<Map2D, List<Moves>> {
    file.useLines { lines ->
        val iterable = lines.iterator()
        val map2d: Map2D = parserMap2D(iterable)
        val orders: List<Moves> = parserMovements(iterable.asSequence())
        return Pair(map2d, orders)
    }
}

private fun parserMap2D(iterable: Iterator<String>): Map2D {
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

    return Map2D(
        weight = width!!, height = y,
        collections = listOf(
            Pair(Parser.WALL.char, walls),
            Pair(Parser.BOX.char, boxes.map { b -> b.toMutable() })
        ),
        elements = listOf(Pair(Parser.ROBOT.char, robot!!.toMutable()))
    )
}

private fun parserMovements(sequence: Sequence<String>): List<Moves> {
    val movementsMap = Moves.entries.associateBy { m -> m.char }
    return sequence.map { line ->
        line.map { char ->
            movementsMap[char] ?: TODO("Unexpected char: '$char'")
        }
    }.flatten().toList()
}
