package com.cucarell.advent.y2024.d18

import com.cucarell.advent.utils.Coordinates
import com.cucarell.advent.utils.CoordinatesMove
import com.cucarell.advent.utils.Map2DVersion2
import java.nio.file.Path
import kotlin.io.path.Path

fun main() {
    val file: Path = object {}.javaClass.getResource("/2024/18/input")!!.path.let(::Path)
    part1(size = 70, fallen = 1024, file = file)
        .let(::println)
}

data class Alive(
    val position: Coordinates,
    val points: Int,
    val startPoints: Long,
    var track: List<Coordinates>
) {
    constructor(position: Coordinates, track: List<Coordinates>, end: Coordinates) : this(
        position = position,
        points = track.size,
        startPoints = aStart(position, end = end) + track.size,
        track = track
    )

    fun getMovements(): List<Coordinates> {
        return CoordinatesMove
            .entries
            .map { m: CoordinatesMove -> position.getMovement(m) }
            .filter { p: Coordinates -> p !in track }
    }

    override fun toString(): String = "$position <$points, $startPoints> ${track.size}"
}

fun part1(size: Int, fallen: Int, file: Path): Int {
    val map2d = parse(file = file, fallen = fallen, size = size)
    val end = Coordinates(size, size)
    val start = Coordinates(0, 0)
    val alive: MutableSet<Alive> = mutableSetOf(Alive(start, listOf(start), end))

    return resolveMaze(map2d = map2d, end = end, alive = alive) - 1 // First position is not a step
}


fun resolveMaze(map2d: Map2DVersion2, end: Coordinates, alive: MutableSet<Alive>): Int {
    val track: MutableSet<Coordinates> = mutableSetOf()//alive.first().track.toMutableSet()

    while (alive.isNotEmpty()) {
        val explorer: Alive = nextExplorer(alive)
        if (explorer.position == end) return explorer.track.size
        if (explorer.position in track) continue
        track.add(explorer.position)
        val nextSteps: List<Coordinates> = nextSteps(explorer = explorer, map2d = map2d, track = track)
        val newAlive: List<Alive> = nextSteps.map { c: Coordinates -> Alive(c, explorer.track + setOf(c), end) }
        // TODO println(); println(map2d.toString(other = Pair('O', explorer.track))); println(explorer) // TODO
        alive.addAll(newAlive)
    }
    error("No more options \\O.o/")
}

fun nextSteps(explorer: Alive, map2d: Map2DVersion2, track: Set<Coordinates>): List<Coordinates> = explorer
    .getMovements()
    .filter { c: Coordinates -> map2d.getObstacleOrNull(c)?.id != ParseDay18.WALL.id }
    .filter { c: Coordinates -> c in map2d }
    .filter { c: Coordinates -> c !in track }

fun nextExplorer(alives: MutableSet<Alive>): Alive {
    val minAStart = alives.minBy { a: Alive -> a.startPoints }.startPoints
    val explorer: Alive = alives
        .filter { a: Alive -> a.startPoints == minAStart }
        .maxBy { a: Alive -> a.points }

    if (alives.remove(explorer)) return explorer

    TODO(
        "que xungo: $explorer: -> ${
            alives.joinToString("\n") { a ->
                "$a ${a == explorer} && ${explorer === a} ex: ${explorer.hashCode()} a: ${a.hashCode()}"
            }
        }"
    )
}

fun aStart(coordinates: Coordinates, end: Coordinates): Long = coordinates.distance(end).toLong()
