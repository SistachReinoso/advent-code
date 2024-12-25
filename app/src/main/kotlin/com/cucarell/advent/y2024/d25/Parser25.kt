package com.cucarell.advent.y2024.d25

import com.cucarell.advent.y2024.d25.CharValues.entries
import java.nio.file.Path
import kotlin.io.path.useLines

enum class CharValues(val char: Char) {
    FILLED('#'),
    EMPTY('.');

    companion object {
        fun of(char: Char): CharValues = entries.first { e: CharValues -> e.char == char }
    }
}

fun parser25(file: Path): List<Schematic> {
    file.useLines { lines: Sequence<String> ->
        return lines
            .map { line: String -> line.toList().map { char: Char -> CharValues.of(char) } }
            .chunked(8)
            .map { listOfLists: List<List<CharValues>> -> parser25ToSchematic(listOfLists.take(7)) }
            .toList()
    }
}

fun parser25ToSchematic(listOfLists: List<List<CharValues>>): Schematic {
    val newList: List<String> = turn90ListOfLists(listOfLists)
    val whatIsIt = listOfLists[0][0]
    val output = newList.map { line: String -> line.indexOfLast { char: Char -> char == whatIsIt.char } }
    return when (whatIsIt) {
        CharValues.FILLED -> Schematic.Lock(output)
        CharValues.EMPTY -> Schematic.Key(output.map { v: Int -> 5 - v })
    }
}

// TODO utils
fun turn90ListOfLists(listOfLists: List<List<CharValues>>): List<String> =
    (0..<5) // Five-Pin Tumbler
        .map { x: Int -> listOfLists .joinToString("") { list: List<CharValues> -> list[x].char.toString() } }
