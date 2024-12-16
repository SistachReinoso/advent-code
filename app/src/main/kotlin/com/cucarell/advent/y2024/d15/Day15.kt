package com.cucarell.advent.y2024.d15

import java.nio.file.Path
import kotlin.io.path.Path

/* Solution demo2:
########
#....OO#
##.....#
#.....O#
#.#O@..#
#...O..#
#...O..#
########

Solution demo:
##########
#.O.O.OOO#
#........#
#OO......#
#OO@.....#
#O#.....O#
#O.....OO#
#O.....OO#
#OO....OO#
##########
 */

fun main() {
    val file: Path = object {}.javaClass.getResource(
        //"/2024/15/demo" // 10092
        "/2024/15/demo2"  //  2028
        //"/2024/15/input"
    )!!.path.let(::Path)

    part1(file)
        .let(::println)
}

fun part1(file: Path): Long {
    val (map2d, moves) = parser(file)
    println("Parser!")
    println(map2d.toString(emptyChar = '.'))
    println(moves)
    return 42L
}

