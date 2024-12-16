package com.cucarell.advent.utils

class Map2D(
    private val weight: Int,
    private val height: Int,
    private val collections: Collection<Pair<Char, Collection<CoordinatesInterface>>>,
    private val elements: Collection<Pair<Char, CoordinatesInterface>>
) {
    //operator fun contains(c: Coordinates): Boolean = c in walls
    fun toString(emptyChar: Char = ' ', multipleChar: Char = '8'): String =
        (0..<height).joinToString("\n") { y ->
            (0..<weight).fold("") { acc, x ->
                val coordinates = Coordinates(x, y)
                val r = collections.filter { (_, collection) -> coordinates in collection }.map { (char, _) -> char } +
                        elements.filter { (_, c) -> coordinates == c }.map { (char, _) -> char }

                acc + when (r.size) {
                    0 -> emptyChar
                    1 -> r.first()
                    else -> multipleChar
                }
            }
        }
}
