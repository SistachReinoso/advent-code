package com.cucarell.advent.y2024.d16

import com.cucarell.advent.utils.*
import java.nio.file.Path
import kotlin.io.path.Path

fun main() {
    val file: Path = object {}.javaClass.getResource(
        //"/2024/16/demo" // 45, 7036
        //"/2024/16/demo2" // 64, 11048
        "/2024/16/input"
    )!!.path.let(::Path)

    part2(file)
        .let(::println)
}

data class Alive(
    val position: Position,
    val points: Long,
    val startPoints: Long,
    var track: List<Pair<Position, Long>>
) {
    fun getMovements(): List<Pair<Position, Long>> {
        val move: CoordinatesMove = position.direction
        return CoordinatesMove
            .entries
            .map { m: CoordinatesMove ->
                Pair(
                    Position(position.getMovement(m), m),
                    points + if (move == m) 1L else 1001L
                )
            }
    }

    /* TODO error from Kotlin? Wii not match?
    override fun equals(o: Any?): Boolean = when (o) {
        is Alive -> position == o.position && points == o.points
        else -> super.equals(o)
    }

    override fun hashCode(): Int = when (position.direction) {
        CoordinatesMove.UP -> 191
        CoordinatesMove.DOWN -> 197
        CoordinatesMove.LEFT -> 157
        CoordinatesMove.RIGHT -> 181
    } * (position.x + position.y * points.toInt())
    */

    override fun toString(): String = "$position <$points, $startPoints> ${track.size}"
}

data class NextStep(val position: Position, val points: Long, val equals: Boolean)

fun part2(file: Path): Long {
    val map2d: Map2DVersion2 = parser(file)
    val end = Coordinates(map2d.getObstacle(Parser.END.id).getCoordinatesPointer(Coordinates(0, 0)).first())
    val reindeer: Map2ObstacleReindeer = map2d.getObstacle(Parser.REINDER.id) as Map2ObstacleReindeer

    val track: MutableMap<Position, Long> = mutableMapOf(reindeer.position to 0)
    val alive: MutableList<Alive> = mutableListOf(
        newAlive(
            position = reindeer.position,
            points = 0L,
            end = end,
            track = emptyList()
        )
    )

    // TODO println("track: $track\nalive: $alive")
    var maximum: Long? = null
    var maximumAlive: Alive? = null

    while (alive.isNotEmpty()) {
        val explorer: Alive = nextExplorer(alive, maximumAlive)
        reindeer.position = explorer.position // show map!
        if (maximum != null && maximum < explorer.startPoints) {
            // println(map2d.toString(other = Pair('0', explorer.track))); println(explorer)
            // println("maximum < explorer.startPoints: $maximum < ${explorer.startPoints}: ${maximum < explorer.startPoints}")
            println(map2d.toString(other = Pair('O', maximumAlive!!.track.map { it.first }))); println(explorer)
            return maximumAlive!!.track.size.toLong()
        } else if (explorer.position == end) {
            maximumAlive = explorer
            maximum = explorer.points
            alive.add(explorer)
            println(maximum)
            continue
        }
        // TODO println(map2d.toString(other = Pair('0', explorer.track))); println(explorer)

        val nextSteps: List<NextStep> = nextSteps(explorer = explorer, map2d = map2d, track = track)

        val newAlives: List<Alive> = nextSteps.mapNotNull { nextStep: NextStep ->
            updateAliveOrGetAlive(nextStep = nextStep, explorer = explorer, alive = alive, end = end)
        }

        alive.addAll(newAlives)
        newAlives.forEach { a: Alive ->
            val oldTrack = track[a.position]
            if (oldTrack != null && a.points > oldTrack) TODO("Que ha passat? $a, $alive, \n$map2d\n$track")
            track[a.position] = a.points
        }
    }
    TODO("No more options \\O.o//")
}

// If filter as not null, this mean is == points, you update alive.
// if not, you get a new alive
fun updateAliveOrGetAlive(
    nextStep: NextStep,
    explorer: Alive,
    alive: List<Alive>,
    end: Coordinates,
): Alive? =
    if (nextStep.equals) {
        val check = alive.filter { a: Alive -> Pair(nextStep.position, nextStep.points) in a.track }
        // TODO (?) if (check.isEmpty()) TODO("$nextStep,\n ${alive.joinToString("\n")}")
        check.forEach { a: Alive ->
            a.track = (a.track + explorer.track).distinctBy { t -> t.first.getCoordinates() }
        }
        null
    } else newAlive(
        position = nextStep.position,
        points = nextStep.points,
        track = explorer.track,
        end = end
    )

fun newAlive(position: Position, points: Long, end: Coordinates, track: List<Pair<Position, Long>>): Alive =
    Alive(
        position = position,
        points = points,
        startPoints = points + aStart(position = position, end = end),
        track = track + setOf(Pair(position, points))
    )

fun nextSteps(explorer: Alive, map2d: Map2DVersion2, track: Map<Position, Long>): List<NextStep> = explorer
    .getMovements()
    .filter { (p: Position, _) -> map2d.getObstacleOrNull(p)?.id != Parser.WALL.id }
    .mapNotNull { (position: Position, points: Long) ->
        val oldPoints = track[position]
        if (oldPoints == null) {
            val whatDo = CoordinatesMove
                .entries
                .map { m -> position.copy(direction = m) }
                .mapNotNull { p: Position -> track[p] }
                .none { rotatedPoints: Long -> points > rotatedPoints + 1000L }

            if (whatDo) NextStep(position = position, points = points, equals = false) else null
        } else if (points > oldPoints) null else NextStep(
            position = position,
            points = points,
            equals = points == oldPoints
        )
    }
/* TODO deletme (?)
.map { (position: Position, p: Long) ->
    val points = p + explorer.points
    Alive(
        position = position,
        points = points + explorer.points,
        startPoints = points + aStart(position = position, end = end)
        )
}
 */

fun nextExplorer(alive: MutableList<Alive>, ma: Alive?): Alive {
    val minAStar = alive.filter { a -> a != ma }.minBy { a: Alive -> a.startPoints }.startPoints
    val explorer = alive.withIndex()
        .filter { (_, a: Alive) -> a != ma }
        .filter { (_, a: Alive) -> a.startPoints == minAStar }
        .maxBy { (_, a: Alive) -> a.points }
    return alive.removeAt(explorer.index)
    println("explrer: $explorer")
    alive.forEach { a: Alive ->
        println("a: $a: ${a == explorer}")
        println("a == explorer")
        println("a: $a: ${a == explorer}")
    }
    TODO("que xungo: $explorer: -> ${alive.joinToString("\n") { a -> "$a ${a == explorer} && ${explorer == a}" }}")
}

/* I supose: S bottom left, E up right
UP RIGHT, is easy, I think is correct
Left x ==
<#G
123
    1 -> 1001
    2 -> 2002
    3 -> 2003
    G -> 3004
Where 2 are the distance between < and G

Left y ==, the same case
3G
2#
1<
    1 ->    1
    2 -> 1002
    3 -> 1003
    G -> 2004

Left !=
2G
1#
<.
#.
    1 -> 1001
    2 -> 1002
    G -> 2003
    distancia = 3
 */
fun aStart(position: Position, end: CoordinatesInterface): Long =
    when (position.direction) {
        CoordinatesMove.UP -> if (position.x == end.x) end.y - position.y else 1000 + position.distance(end)
        CoordinatesMove.RIGHT -> if (position.y == end.y) end.x - position.y else 1000 + position.distance(end)
        CoordinatesMove.DOWN -> if (position.y == end.y) 3002 + end.x - position.x else 2000 + position.distance(end)
        CoordinatesMove.LEFT -> if (position.x == end.x) 3002 + end.y - position.y else 2000 + position.distance(end)
    }.toLong()
