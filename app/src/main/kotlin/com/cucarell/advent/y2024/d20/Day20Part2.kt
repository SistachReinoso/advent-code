package com.cucarell.advent.y2024.d20

import com.cucarell.advent.utils.*
import java.nio.file.Path
import kotlin.io.path.Path

fun main() {
    val file: Path = object {}.javaClass.getResource("/2024/20/input")!!.path.let(::Path)
    val a = part2(file, save = 100L)

    a.asSequence()
        .filter { map -> map.key >= 100 }
        .map { map -> map.value }
        .sum()
        .let { wii -> println(wii) }
}

fun part2(file: Path, save: Long, cheat: Int = 20): Map<Long, Int> {
    val map2d: Map2DVersion2 = parse20Raze(file)
    val start: CoordinatesInterface = (map2d.getObstacle(Map20Elements.START.id) as Map2DObstacleElement).coordinates
    val end: CoordinatesInterface = (map2d.getObstacle(Map20Elements.END.id) as Map2DObstacleElement).coordinates
    val walls: Set<CoordinatesInterface> =
        (map2d.getObstacle(Map20Elements.WALL.id) as Map2DObstacleCollection).collection as Set<CoordinatesInterface>

    val lhm: LinkedHashMap<Coordinates, Long> = runFullRace(start = start, end = end, walls = walls)
    val lastValue = lhm.lastEntry().value
    println("Last Value: $lastValue")
    return lhm//.asSequence()
        .filter { map: Map.Entry<Coordinates, Long> -> map.value + save <= lastValue } // TODO takeWhile (?)
        .flatMap { map: Map.Entry<Coordinates, Long> -> generateFromDistance(map.key, map.value, maxDistance = cheat) }
        .filter { cr: CheatResult -> cr.coordinates in lhm }
        .map { cr: CheatResult -> lhm.getValue(cr.coordinates) - cr.expend }
        .filter { result: Long -> result >= save }
        .groupingBy { it }.eachCount()
}

fun runFullRace(
    start: CoordinatesInterface,
    end: CoordinatesInterface,
    walls: Set<CoordinatesInterface>
): LinkedHashMap<Coordinates, Long> {
    var explorer: Coordinates = Coordinates(start)
    val track: LinkedHashMap<Coordinates, Long> = LinkedHashMap()
    var expend: Long = 0L

    while (true) {
        track.put(explorer, expend++)
        if (explorer == end) break
        explorer = nextStep(explorer = explorer, walls = walls, track = track)
    }
    return track
}

data class CheatResult(val coordinates: Coordinates, val expend: Long)

fun generateFromDistance(start: Coordinates, expend: Long, maxDistance: Int): Sequence<CheatResult> = sequence {
    (2..maxDistance)
        .forEach { distance: Int ->
            val xI = sequenceLoopNumbers(start.x, min = start.x - distance, max = start.x + distance).iterator()
            val yI = sequenceLoopNumbers(start.y - distance, min = start.y - distance, max = start.y + distance)
                .iterator()
            repeat(4 * distance) { yield(CheatResult(Coordinates(xI.next(), yI.next()), expend + distance)) }
        }
}

fun sequenceLoopNumbers(init: Int, min: Int, max: Int): Sequence<Int> = sequence {
    var value = init
    var increment = true

    while (true) {
        yield(value)
        if (value == max) increment = false
        else if (value == min) increment = true
        value += if (increment) 1 else -1
    }
}
