package com.cucarell.advent.y2024.d20

import com.cucarell.advent.utils.*
import java.nio.file.Path
import kotlin.io.path.Path

fun main() {
    val file: Path = object {}.javaClass.getResource("/2024/20/input")!!.path.let(::Path)
    val a = part1(file)
    println(a)
}

fun part1(file: Path): Long {
    val map2d: Map2DVersion2 = parse20Raze(file)
    val start: CoordinatesInterface = (map2d.getObstacle(Map20Elements.START.id) as Map2DObstacleElement).coordinates
    val end: CoordinatesInterface = (map2d.getObstacle(Map20Elements.END.id) as Map2DObstacleElement).coordinates
    val walls: Set<CoordinatesInterface> =
        (map2d.getObstacle(Map20Elements.WALL.id) as Map2DObstacleCollection).collection as Set<CoordinatesInterface>

    val a = resolveRace(start = start, end = end, walls = walls, map2d = map2d)
    return a
}

fun resolveRace(
    start: CoordinatesInterface,
    end: CoordinatesInterface,
    walls: Set<CoordinatesInterface>,
    map2d: Map2DVersion2
): Long {
    var explorer: Coordinates = Coordinates(start)
    val track: MutableMap<Coordinates, Long> = mutableMapOf()
    val cheatTrack: MutableMap<Coordinates, List<Long>> = mutableMapOf()
    var solution: Long = 0L
    var expend: Long = 0L

    while (true) {
        track.put(explorer, expend++)
        if (explorer in cheatTrack)
            solution += generateSolution(explorer = explorer, cheatTrack = cheatTrack, track = track)
                .count { diff: Long -> diff >= 100L }
        if (explorer == end) break
        nextCheat(explorer = explorer, cheatTrack = cheatTrack, walls = walls, map2d = map2d, track = track)
        explorer = nextStep(explorer = explorer, walls = walls, track = track)
    }
    return solution
}

private fun nextStep(
    explorer: Coordinates,
    walls: Set<CoordinatesInterface>,
    track: Map<Coordinates, Long>
): Coordinates =
    CoordinatesMove.entries
        .map { cm: CoordinatesMove -> explorer.getMovement(cm) }
        .filter { coordinates: Coordinates -> track[coordinates] == null }
        .filter { coordinates: Coordinates -> coordinates !in walls }
        .also { c -> if (c.size != 1) error("unexpected, != 1: $c") }
        .first()

private fun generateSolution(
    explorer: Coordinates,
    cheatTrack: MutableMap<Coordinates, List<Long>>,
    track: MutableMap<Coordinates, Long>
): List<Long> {
    val normalExpend: Long = track.getValue(explorer)
    val diff: List<Long> = cheatTrack.getValue(explorer)
        .map { cheatExpend: Long -> normalExpend - cheatExpend }
    return diff
}

private fun nextCheat(
    explorer: Coordinates,
    cheatTrack: MutableMap<Coordinates, List<Long>>,
    walls: Set<CoordinatesInterface>,
    map2d: Map2DVersion2,
    track: Map<Coordinates, Long>,
) {
    CoordinatesMove.entries.asSequence()
        .map { cm: CoordinatesMove -> Pair(explorer.getMovement(cm), cm) }
        .filter { (coordinates, _) -> coordinates in walls }
        .map { (coordinates, cm) -> coordinates.getMovement(cm) }
        .filter { coordinates: Coordinates -> track[coordinates] == null }
        .filter { coordinates: Coordinates -> coordinates !in walls }
        .filter { coordinates: Coordinates -> coordinates in map2d }
        .forEach { coordinates: Coordinates ->
            val expend: Long = track.getValue(explorer) + 2 // Two movements
            cheatTrack.put(coordinates, (cheatTrack[coordinates] ?: emptyList()) + listOf(expend))
        }
}
