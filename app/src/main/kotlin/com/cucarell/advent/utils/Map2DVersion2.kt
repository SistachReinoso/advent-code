package com.cucarell.advent.utils

interface Map2DObstacleInterface {
    operator fun contains(o: CoordinatesInterface): Boolean
    fun getChar(c: CoordinatesInterface): Char
}

class Map2DObstacleCollection(val char: Char, val collection: Collection<CoordinatesInterface>) :
    Map2DObstacleInterface {
    override fun contains(o: CoordinatesInterface): Boolean = o in collection
    override fun getChar(c: CoordinatesInterface) = char
}

class Map2DObstacleElement(val char: Char, val coordinates: CoordinatesInterface) : Map2DObstacleInterface {
    override fun contains(o: CoordinatesInterface): Boolean = o == coordinates
    override fun getChar(c: CoordinatesInterface) = char
}

class Map2DVersion2(
    private val weight: Int,
    private val height: Int,
    private val obstacles: List<Map2DObstacleInterface>,
    private val emptyChar: Char = '.',
    private val multipleChar: Char = '8'
) {

    fun searchObstacle(c: CoordinatesInterface): List<Map2DObstacleInterface> = obstacles
        .filter { obstacle -> c in obstacle }

    fun toString(emptyChar: Char, multipleChar: Char): String =
        (0..<height).joinToString("\n") { y ->
            (0..<weight).fold("") { acc, x ->
                val coordinates = Coordinates(x, y)
                val r = searchObstacle(coordinates)

                acc + when (r.size) {
                    0 -> emptyChar
                    1 -> r.first().getChar(coordinates)
                    else -> multipleChar
                }
            }
        }

    override fun toString(): String =
        toString(emptyChar = emptyChar, multipleChar = multipleChar)
}
