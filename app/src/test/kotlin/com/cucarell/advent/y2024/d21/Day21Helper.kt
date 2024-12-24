package com.cucarell.advent.y2024.d21

import com.cucarell.advent.utils.MutableCoordinates

data class Robot(
    val map: List<String>,
    val where: MutableCoordinates = getCoordinates('A', map).toMutable(),
) {
    constructor(map: List<String>) : this(map, where = getCoordinates('A', map).toMutable())

    fun execute(input: Sequence<Char>): Sequence<Char> = sequence {
        for (c: Char in input) {
            when (c) {
                '<' -> where.x -= 1
                '>' -> where.x += 1
                '^' -> where.y -= 1
                'v' -> where.y += 1
                'A' -> yield(getChar())
                else -> error("Unexpected error KR execute with '$c'")
            }
        }
    }

    fun getChar(): Char = getValue(where, map)
    override fun toString(): String = "$where " + getChar()
}

fun getNumericRobot() = Robot(numericKeypad)
fun getDirectionalRobot() = Robot(directionalKeypad)

fun fullRobot(input: String): Sequence<Char> = input.toList().asSequence()
    .let { wii -> getDirectionalRobot().execute(wii) }
    .let { wii -> getDirectionalRobot().execute(wii) }
    .let { wii -> getNumericRobot().execute(wii) }
