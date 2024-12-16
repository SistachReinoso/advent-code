package com.cucarell.advent.utils

interface CoordinatesInterface {
    val x: Int
    val y: Int
}

data class Coordinates(override val x: Int, override val y: Int) : CoordinatesInterface {
    fun toMutable(): MutableCoordinates = MutableCoordinates(x, y)
    override fun equals(o: Any?): Boolean = if (o is CoordinatesInterface) x == o.x && y == o.y else super.equals(o)
    override fun hashCode(): Int = 10000 * x + y
    override fun toString(): String = "($x, $y)"
}

data class MutableCoordinates(override var x: Int, override var y: Int) : CoordinatesInterface {
    fun toCoordinates(): Coordinates = Coordinates(x, y)
    override fun equals(o: Any?): Boolean = if (o is CoordinatesInterface) x == o.x && y == o.y else super.equals(o)
    override fun hashCode(): Int = 10000 * x + y
    override fun toString(): String = "($x, $y)"
}
