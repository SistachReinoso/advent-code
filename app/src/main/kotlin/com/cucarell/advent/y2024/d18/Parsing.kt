package com.cucarell.advent.y2024.d18

import com.cucarell.advent.utils.Coordinates
import com.cucarell.advent.utils.Map2DObstacleCollection
import com.cucarell.advent.utils.Map2DVersion2
import java.nio.file.Path
import kotlin.io.path.useLines

enum class ParseDay18(val char: Char, val id: String) {
    WALL('#', "corrupted")
    ;
}

fun parse(file: Path, fallen: Int, size: Int): Map2DVersion2 {
    file.useLines { lines ->
        val coordinates = lines
            .take(fallen)
            .map { line: String -> line.split(",").map { value -> value.toInt() } }
            .map { (x, y) -> Coordinates(x, y) }
            .toSet()

        return Map2DVersion2(
            weight = size + 1,
            height = size + 1,
            obstacles = listOf(
                Map2DObstacleCollection(
                    char = ParseDay18.WALL.char,
                    collection = coordinates,
                    id = ParseDay18.WALL.id
                )
            )
        )
    }
}

data class Parser18(val map2d: Map2DVersion2, val coordinates: List<Coordinates>)

fun parsePart2(file: Path, size: Int): Parser18 {
    file.useLines { lines ->
        val coordinates = lines
            .map { line: String -> line.split(",").map { value -> value.toInt() } }
            .map { (x, y) -> Coordinates(x, y) }
            .toList()

        val map2d = Map2DVersion2(
            weight = size + 1,
            height = size + 1,
            obstacles = listOf(
                Map2DObstacleCollection(
                    char = ParseDay18.WALL.char,
                    collection = mutableSetOf(),
                    id = ParseDay18.WALL.id
                )
            )
        )

        return Parser18(map2d, coordinates)
    }
}
