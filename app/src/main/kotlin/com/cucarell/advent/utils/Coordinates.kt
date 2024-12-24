package com.cucarell.advent.utils

import kotlin.math.abs

interface CoordinatesInterface {
    val x: Int
    val y: Int

    fun getMovement(move: CoordinatesMove): Coordinates = when (move) {
        CoordinatesMove.UP -> Coordinates(x, y - 1)
        CoordinatesMove.DOWN -> Coordinates(x, y + 1)
        CoordinatesMove.LEFT -> Coordinates(x - 1, y)
        CoordinatesMove.RIGHT -> Coordinates(x + 1, y)
    }

    fun up(): Coordinates = getMovement(CoordinatesMove.UP)
    fun down(): Coordinates = getMovement(CoordinatesMove.DOWN)
    fun left(): Coordinates = getMovement(CoordinatesMove.LEFT)
    fun right(): Coordinates = getMovement(CoordinatesMove.RIGHT)

    fun distance(o: CoordinatesInterface): Int = abs(x - o.x) + abs(y - o.y)

    operator fun plus(o: CoordinatesInterface): CoordinatesInterface = Coordinates(x + o.x, y + o.y)
}

enum class CoordinatesMove(val char: Char) {
    UP('^'),
    DOWN('v'),
    LEFT('<'),
    RIGHT('>');
}

data class Coordinates(override val x: Int, override val y: Int) : CoordinatesInterface {
    constructor(c: CoordinatesInterface) : this(c.x, c.y)

    fun toMutable(): MutableCoordinates = MutableCoordinates(x, y)
    override operator fun equals(o: Any?): Boolean =
        if (o is CoordinatesInterface) x == o.x && y == o.y else super.equals(o)

    override fun hashCode(): Int = 10000 * x + y
    override fun toString(): String = "($x, $y)"
}

data class MutableCoordinates(override var x: Int, override var y: Int) : CoordinatesInterface {
    fun toCoordinates(): Coordinates = Coordinates(x, y)
    fun move(move: CoordinatesMove) {
        val c = getMovement(move)
        x = c.x
        y = c.y
    }

    override operator fun equals(o: Any?): Boolean =
        if (o is CoordinatesInterface) x == o.x && y == o.y else super.equals(o)

    override fun hashCode(): Int = 10000 * x + y
    override fun toString(): String = "($x, $y)"
}

data class Position(override val x: Int, override val y: Int, val direction: CoordinatesMove) : CoordinatesInterface {
    constructor(c: CoordinatesInterface, m: CoordinatesMove) : this(x = c.x, y = c.y, direction = m)

    fun getCoordinates(): Coordinates = Coordinates(x = x, y = y)
    override operator fun equals(o: Any?): Boolean = when (o) {
        is Position -> x == o.x && y == o.y && direction == o.direction
        is CoordinatesInterface -> x == o.x && y == o.y
        else -> super.equals(o)
    }

    override fun hashCode(): Int = 10000 * x + y
    override fun toString(): String = "($x, $y, $direction)"
}
