package com.cucarell.advent.y2024.d21

val numericKeypad: List<String> = listOf(
    "789",
    "456",
    "123",
    " 0A",
)

val directionalKeypad: List<String> = listOf(
    " ^A",
    "<v>"
)

enum class DirectionsKeypad(val char: Char) {
    UP('^'),
    LEFT('<'),
    DOWN('v'),
    RIGHT('>'),
    A('A');
}
