package com.cucarell.advent.utils

interface EnumChar {
    val char: Char
}

class Map2DVersion1(
    private val weight: Int,
    private val height: Int,
    private val collections: Collection<Pair<EnumChar, Collection<CoordinatesInterface>>>,
    private val elements: Collection<Pair<EnumChar, CoordinatesInterface>>,
    private val emptyChar: Char = ' '
) {

    fun getElement(enumChar: EnumChar): CoordinatesInterface = elements
        .first { (ec, _) -> enumChar == ec }
        .let { (_, c) -> c }

    fun getCollection(enumChar: EnumChar): Collection<CoordinatesInterface> = collections
        .first { (ec, _) -> enumChar == ec }
        .let { (_, c) -> c }

    fun getCollectionPointer(enumChar: EnumChar, coordinates: CoordinatesInterface): CoordinatesInterface = collections
        .first { (ec, _) -> enumChar == ec }
        .let { (_, c) -> c }
        .first { c -> c == coordinates }

    fun getEnumCharOrNull(coordinates: CoordinatesInterface): EnumChar? =
        elements.firstOrNull { (_, c) -> c == coordinates }?.first
            ?: collections.firstOrNull { (_, c) -> coordinates in c }?.first

    fun toString(empty: Char = emptyChar, multipleChar: Char = '8'): String =
        (0..<height).joinToString("\n") { y ->
            (0..<weight).fold("") { acc, x ->
                val coordinates = Coordinates(x, y)
                val r =
                    collections.filter { (_, collection) -> coordinates in collection }.map { (enum, _) -> enum.char } +
                            elements.filter { (_, c) -> coordinates == c }.map { (enum, _) -> enum.char }

                acc + when (r.size) {
                    0 -> empty
                    1 -> r.first()
                    else -> multipleChar
                }
            }
        }
}
