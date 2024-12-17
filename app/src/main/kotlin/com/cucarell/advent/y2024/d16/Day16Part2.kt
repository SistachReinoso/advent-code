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
    var track: Set<CoordinatesInterface>
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

    override fun equals(o: Any?): Boolean = when (o) {
        is Alive -> position == o.position && points == o.points
        else -> super.equals(o)
    }

    override fun hashCode(): Int = when (position.direction) {
        CoordinatesMove.UP -> 11
        CoordinatesMove.DOWN -> 13
        CoordinatesMove.LEFT -> 17
        CoordinatesMove.RIGHT -> 19
    } * (position.x + position.y * points.toInt())

    override fun toString(): String = "$position <$points, $startPoints> ${track.size}"
}

fun part2(file: Path): Long {
    val map2d: Map2DVersion2 = parser(file)
    val end: CoordinatesInterface = map2d.getObstacle(Parser.END.id).getCoordinatesPointer(Coordinates(0, 0)).first()
    val reindeer: Map2ObstacleReindeer = map2d.getObstacle(Parser.REINDER.id) as Map2ObstacleReindeer

    val track: MutableMap<Position, Long> = mutableMapOf(reindeer.position to 0)
    val alive: MutableSet<Alive> = mutableSetOf(
        Alive(
            position = reindeer.position,
            startPoints = aStart(reindeer.position, end),
            points = 0L,
            track = setOf(reindeer.position)
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
           // println(map2d.toString(other = Pair('O', maximumAlive!!.track))); println(explorer)
            val endTrack = maximumAlive!!.track.map { c -> Coordinates(c.x, c.y) }.toSet()
            return endTrack.size.toLong()
        } else if (explorer.position == end) {
            maximumAlive = explorer
            maximum = explorer.points
            alive.add(explorer)
            println(maximum)
            continue
        }
        // TODO println(map2d.toString(other = Pair('0', explorer.track))); println(explorer)

        val nextSteps: List<Pair<Position, Long>> = nextSteps(explorer = explorer, map2d = map2d, track = track)

        val newAlives: List<Alive> = nextSteps.mapNotNull { (position: Position, points: Long) ->
            updateAliveOrGetAlive(position = position, points = points, explorer = explorer, alive = alive, end = end)
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
    position: Position,
    points: Long,
    explorer: Alive,
    alive: Set<Alive>,
    end: CoordinatesInterface
): Alive? {
    val list = alive.filter { a: Alive -> position in a.track }
    if (list.isEmpty()) return newAlive(
        position = position,
        points = points,
        track = explorer.track,
        end = end
    )

    list.forEach { a: Alive -> a.track = a.track + explorer.track }
    return null
}

fun newAlive(position: Position, points: Long, end: CoordinatesInterface, track: Set<CoordinatesInterface>): Alive =
    Alive(
        position = position,
        points = points,
        startPoints = points + aStart(position = position, end = end),
        track = track + setOf(position)
    )

fun nextSteps(explorer: Alive, map2d: Map2DVersion2, track: Map<Position, Long>): List<Pair<Position, Long>> = explorer
    .getMovements()
    .filter { (p: Position, _) -> map2d.getObstacleOrNull(p)?.id != Parser.WALL.id }
    .filter { (position: Position, points: Long) ->
        val oldPoints = track[position]
        if (oldPoints == null) CoordinatesMove
            .entries
            .map { m -> position.copy(direction = m) }
            .mapNotNull { p: Position -> track[p] }
            .also { list -> if (list.isEmpty()) return@filter true }
            .any { rotatedPoints: Long -> points <= rotatedPoints + 1000L }
        else points <= oldPoints
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

fun nextExplorer(alive: MutableSet<Alive>, ma: Alive?): Alive {
    val minAStar = alive.filter { a -> a != ma }.minBy { a: Alive -> a.startPoints }.startPoints
    val explorer =
        alive.filter { a -> a != ma }.filter { a: Alive -> a.startPoints == minAStar }.maxBy { a: Alive -> a.points }
    if (alive.remove(explorer)) return explorer
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
