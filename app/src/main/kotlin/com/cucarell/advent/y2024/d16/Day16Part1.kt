package com.cucarell.advent.y2024.d16

import com.cucarell.advent.utils.*
import java.nio.file.Path
import kotlin.io.path.Path

fun main() {
    val file: Path = object {}.javaClass.getResource(
        //"/2024/16/demo" // 7036
        //"/2024/16/demo2" // 11048
        "/2024/16/input"
    )!!.path.let(::Path)

    part1(file)
        .let(::println)
}

class Map2ObstacleReindeer(
    override val id: String,
    var position: Position,
) : Map2DObstacleInterface {
    constructor(id: String, coordinates: MutableCoordinates) : this(
        id = id,
        position = Position(coordinates, CoordinatesMove.RIGHT)
    )

    override fun getCoordinatesPointer(c: CoordinatesInterface): List<CoordinatesInterface> =
        listOf(position.getCoordinates())

    @Deprecated("Perquè tots ho han de tenir? Una altra interface de moviment ho podria tenir sense problemes.")
    override fun getMovementCoordinates(c: CoordinatesInterface, move: CoordinatesMove): List<CoordinatesInterface> =
        TODO("Not yet implemented")

    override fun contains(o: CoordinatesInterface): Boolean = position.getCoordinates() == o
    override fun getChar(c: CoordinatesInterface): Char = position.direction.char

    @Deprecated("Dona més confusió que altra cosa!")
    fun getMovements(): List<Pair<Position, Long>> {
        val c = position.getCoordinates()
        val move: CoordinatesMove = position.direction
        return CoordinatesMove
            .entries
            .map { m2: CoordinatesMove ->
                Pair(Position(c.getMovement(m2), m2), if (move == m2) 1L else 1001L)
            }
    }
}


fun part1(file: Path): Long {
    val map2d: Map2DVersion2 = parser(file)
    val end: CoordinatesInterface = map2d.getObstacle(Parser.END.id).getCoordinatesPointer(Coordinates(0, 0)).first()
    val reindeer: Map2ObstacleReindeer = map2d.getObstacle(Parser.REINDER.id) as Map2ObstacleReindeer

    //println(map2d) // TODO

    val track: MutableMap<Position, Long> = mutableMapOf(reindeer.position to 0)
    val alive: MutableList<Pair<Position, Long>> = mutableListOf(reindeer.position to 0)

    while (alive.isNotEmpty()) {
        val explore = alive.minBy { (_, v) -> v }
        if (Coordinates(explore.first.getCoordinates()) == end) return explore.second
        alive.remove(explore)

        reindeer.position = explore.first
        //println("$map2d and $explore") // TODO

        val nextSteps: List<Pair<Position, Long>> = reindeer
            .getMovements()
            .filter { (p: Position, _) -> map2d.getObstacleOrNull(p)?.id != Parser.WALL.id }
            .filter { (position: Position, points: Long) ->
                val coordinates = position.getCoordinates()
                val options: List<Position> = CoordinatesMove
                    .entries
                    .map { m -> Position(coordinates, m) }
                    .filter { p: Position -> track[p] != null }

                if (options.isEmpty()) return@filter true
                //.map { p: Position -> Pair(p, track[p]!!) }

                options
                    .any { oldPosition: Position ->
                        val oldPoints = track[oldPosition]!!

                        if (position == oldPosition) points < oldPoints
                        else points + 1000L < oldPoints
                    }
            }.map { (p, l) -> Pair(p, l + explore.second) }

        nextSteps.forEach { pair: Pair<Position, Long> ->
            val oldTrack = track[pair.first]

            if (oldTrack == null) {
                track.put(pair.first, pair.second)
                alive.add(pair)
            } else if (oldTrack > pair.second) {
                track.put(pair.first, pair.second)
                alive.add(pair)
            }
        }
    }

    TODO("No more options \\O.o//")
}
