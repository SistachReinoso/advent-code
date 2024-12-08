package com.cucarell.advent.y2024

import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.useLines

fun main() {
    val file: Path = object {}.javaClass.getResource(
        //"/2024/06/demo"
        "/2024/06/input"
    )!!.path.let(::Path)

    part1(file) // 41
    part2(file) // 6
}

private fun part1(file: Path) = parse(file)
    .also(Game::walkToEnd)
    //.also(::println)
    .let(Game::countCross)
    .let(::println)

/**
 * If there is something directly in front of you, turn right 90 degrees.
 * Otherwise, take a step forward.
 */
private enum class Direction(val value: Char) {
    UP('^'),
    RIGHT('>'),
    DOWN('v'),
    LEFT('<'),
    ;
}

private enum class Terrain(val char: Char) {
    OBSTRUCTIONS('#'),
    ADD_OBSTRUCTION('O'),
    FREE('.'),
    ;
}

data class Coordinates(val x: Int, val y: Int) {
    operator fun minus(o: Coordinates): Coordinates = Coordinates(x - o.x, y - o.y)
    operator fun plus(o: Coordinates): Coordinates = Coordinates(x + o.x, y + o.y)
    override fun toString(): String = "($x, $y)"
    operator fun times(i: Int): Coordinates = Coordinates(i * x, i * y)
}

private data class Guard(var direction: Direction, var coordinates: Coordinates) {
    fun nextStepCoordinates(): Coordinates {
        val (x, y) = coordinates
        return when (direction) {
            Direction.UP -> coordinates.copy(y = y - 1)
            Direction.RIGHT -> coordinates.copy(x = x + 1)
            Direction.DOWN -> coordinates.copy(y = y + 1)
            Direction.LEFT -> coordinates.copy(x = x - 1)
        }
    }

    fun move(coor: Coordinates) {
        coordinates = coor
    }

    fun turn() {
        direction = when (direction) {
            Direction.UP -> Direction.RIGHT
            Direction.RIGHT -> Direction.DOWN
            Direction.DOWN -> Direction.LEFT
            Direction.LEFT -> Direction.UP
        }
    }

    override fun toString(): String = direction.value.toString()
}

private data class TerrainTable(val table: List<MutableList<Terrain>>) {
    fun horitzontalSize() = table.first().size
    fun verticalSize() = table.size
    fun get(c: Coordinates): Terrain = table[c.y][c.x]
    fun addObstruction(c: Coordinates): TerrainTable {
        val new = table.map { mutable -> mutable.toMutableList() }
        new[c.y][c.x] = Terrain.ADD_OBSTRUCTION
        return TerrainTable(new)
    }
}

private data class Game(val guard: Guard, val table: TerrainTable, val track: MutableSet<Coordinates>) {
    fun isNotEnded() = !isEnded()
    fun isEnded(): Boolean {
        val (x, y) = guard.coordinates
        val direction = guard.direction

        return (x == 0 && direction == Direction.LEFT) ||
                (y == 0 && direction == Direction.UP) ||
                (x == table.horitzontalSize() - 1 && direction == Direction.RIGHT) ||
                (y == table.verticalSize() - 1 && direction == Direction.DOWN)
    }

    fun walkToEnd() {
        while (isNotEnded()) {
            walk()
        }
    }

    private fun walk() {
        val newCoordinates = guard.nextStepCoordinates()
        if (isNotObstruction(newCoordinates)) {
            track.add(newCoordinates)
            guard.move(newCoordinates)
        } else guard.turn()
    }

    private fun isNotObstruction(coordinates: Coordinates): Boolean = when (table.get(coordinates)) {
        Terrain.OBSTRUCTIONS -> false
        Terrain.ADD_OBSTRUCTION -> false
        Terrain.FREE -> true
    }

    fun countCross() = track.size

    override fun toString(): String {
        val hr = 0..<table.horitzontalSize()
        val vr = 0..<table.verticalSize()

        return "--- ${countCross()} ---\n" +
                vr.joinToString("\n") { y ->
                    hr.joinToString("") { x ->
                        when (val coordinates = Coordinates(x, y)) {
                            guard.coordinates -> guard.toString()
                            in track -> "X"
                            else -> table.get(coordinates).char.toString()
                        }
                    }
                }
    }
}

private fun parse(file: Path): Game {
    var guard: Guard? = null
    file.useLines { lines ->
        val table = lines.withIndex()
            .map { (y, line) ->
                line.withIndex()
                    .map { (x, char) ->
                        when (char) {
                            '#' -> Terrain.OBSTRUCTIONS
                            '.' -> Terrain.FREE
                            '^' -> {
                                guard = Guard(Direction.UP, Coordinates(x, y))
                                Terrain.FREE
                            }

                            else -> TODO("Strange char: '$char'")
                        }
                    }.toMutableList()
            }.toList() // XXX we need define guard value
        return Game(guard!!, TerrainTable(table), mutableSetOf(guard!!.coordinates))
    }
}

/****************************************************************************************************/
/*                                             Part 2                                               */
/****************************************************************************************************/

private data class Game2(
    val guard: Guard,
    val table: TerrainTable,
    val track: MutableSet<Guard>,
    val init: Guard,
    val obstructionSet: MutableSet<Coordinates>
) {
    fun isNotEnded() = !isEnded()
    fun isEnded(): Boolean {
        val (x, y) = guard.coordinates
        val direction = guard.direction

        return (x == 0 && direction == Direction.LEFT) ||
                (y == 0 && direction == Direction.UP) ||
                (x == table.horitzontalSize() - 1 && direction == Direction.RIGHT) ||
                (y == table.verticalSize() - 1 && direction == Direction.DOWN)
    }

    fun walkToEnd(): Boolean {
        while (isNotEnded()) {
            if (walk()) return true
        }
        return false
    }

    fun walkToEndOrLoop(): Int {
        var count = 0
        while (isNotEnded()) {
            val nextCoordinates = guard.nextStepCoordinates()
            if (table.get(nextCoordinates) == Terrain.FREE && nextCoordinates !in obstructionSet) {
                obstructionSet.add(nextCoordinates)
                val obstructionGame = this.copy(
                    guard = init.copy(),
                    table = table.addObstruction(nextCoordinates),
                    track = mutableSetOf(init.copy())
                )
                if (obstructionGame.walkToEnd()) {
                    // println("\n==================="); println(obstructionGame)
                    count++
                }
            }
            walk()
        }

        return count
    }


    private fun walk(): Boolean {
        val newCoordinates = guard.nextStepCoordinates()
        if (isNotObstruction(newCoordinates)) {
            guard.move(newCoordinates)
            if (guard in track) return true
            track.add(guard.copy())
        } else guard.turn()
        return false
    }

    private fun isNotObstruction(coordinates: Coordinates): Boolean = when (table.get(coordinates)) {
        Terrain.OBSTRUCTIONS -> false
        Terrain.ADD_OBSTRUCTION -> false
        Terrain.FREE -> true
    }

    fun countCross() = track.size

    override fun toString(): String {
        val hr = 0..<table.horitzontalSize()
        val vr = 0..<table.verticalSize()

        return "--- ${countCross()} ---\n" +
                vr.joinToString("\n") { y ->
                    hr.joinToString("") { x ->
                        when (val coordinates = Coordinates(x, y)) {
                            guard.coordinates -> guard.toString()
                            in track.map { it.coordinates } -> "X"
                            else -> table.get(coordinates).char.toString()
                        }
                    }
                }
    }
}

private fun part2(file: Path) {
    val (guard: Guard, table: TerrainTable, _) = parse(file)
    Game2(guard, table, mutableSetOf(guard), guard.copy(), mutableSetOf())
        .let(Game2::walkToEndOrLoop)
        .also(::println)
}
