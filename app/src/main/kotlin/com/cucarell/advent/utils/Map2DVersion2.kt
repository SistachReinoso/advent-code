package com.cucarell.advent.utils

interface Map2DObstacleInterface {
    val id: String
    fun getCoordinatesPointer(c: CoordinatesInterface): List<CoordinatesInterface>

    @Deprecated("Perquè tots ho han de tenir? Una altra interface de moviment ho podria tenir sense problemes.")
    fun getMovementCoordinates(c: CoordinatesInterface, move: CoordinatesMove): List<CoordinatesInterface>
    operator fun contains(o: CoordinatesInterface): Boolean
    fun getChar(c: CoordinatesInterface): Char
}

class Map2DObstacleCollection(
    val char: Char,
    val collection: Collection<CoordinatesInterface>,
    override val id: String
) :
    Map2DObstacleInterface {
    override fun getCoordinatesPointer(c: CoordinatesInterface): List<CoordinatesInterface> =
        collection.filter { e -> e == c }

    @Deprecated("Perquè tots ho han de tenir? Una altra interface de moviment ho podria tenir sense problemes.")
    override fun getMovementCoordinates(c: CoordinatesInterface, move: CoordinatesMove): List<CoordinatesInterface> =
        collection
            .filter { e -> e == c }
            .map { e -> e.getMovement(move) }

    override fun contains(oi: CoordinatesInterface): Boolean {
        val o = Coordinates(oi)
        return collection.any { e -> o == e }
    }

    override fun getChar(c: CoordinatesInterface) = char
}

class Map2DObstacleElement(
    val char: Char,
    val coordinates: CoordinatesInterface,
    override val id: String
) : Map2DObstacleInterface {
    override fun getCoordinatesPointer(c: CoordinatesInterface): List<CoordinatesInterface> = getCoordinatesPointer()
    fun getCoordinatesPointer() = listOf(coordinates)

    @Deprecated("Perquè tots ho han de tenir? Una altra interface de moviment ho podria tenir sense problemes.")
    override fun getMovementCoordinates(c: CoordinatesInterface, move: CoordinatesMove): List<CoordinatesInterface> =
        getMovementCoordinates(move)

    fun getMovementCoordinates(move: CoordinatesMove): List<CoordinatesInterface> =
        listOf(coordinates.getMovement(move))


    override fun contains(oi: CoordinatesInterface): Boolean {
        val o = Coordinates(oi)
        return o == coordinates
    }

    override fun getChar(c: CoordinatesInterface) = char
}

class Map2DVersion2(
    private val weight: Int,
    private val height: Int,
    private val obstacles: List<Map2DObstacleInterface>,
    private val emptyChar: Char = '.',
    private val multipleChar: Char = '8'
) {
    fun getObstacle(id: String): Map2DObstacleInterface = obstacles
        .first { o -> o.id == id }

    fun getObstacleOrNull(c: CoordinatesInterface): Map2DObstacleInterface? = obstacles
        .firstOrNull { o -> c in o }

    fun searchObstacle(c: Coordinates): List<Map2DObstacleInterface> = obstacles
        .filter { obstacle -> c in obstacle }

    operator fun contains(o: CoordinatesInterface): Boolean = o.x in 0..<weight && o.y in 0..<height

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

    fun toString(
        other: Pair<Char, Collection<CoordinatesInterface>>,
        emptyChar: Char = '.',
        multipleChar: Char = '8'
    ): String =
        (0..<height).joinToString("\n") { y ->
            (0..<weight).fold("") { acc, x ->
                val coordinates = Coordinates(x, y)
                val r = searchObstacle(coordinates).map { map2d -> map2d.getChar(coordinates) } +
                        if (other.second.firstOrNull { c -> coordinates == c } == null) emptyList() else listOf(other.first)

                acc + when (r.size) {
                    0 -> emptyChar
                    1 -> r.first()
                    else -> multipleChar
                }
            }
        }

    override fun toString(): String =
        toString(emptyChar = emptyChar, multipleChar = multipleChar)
}
