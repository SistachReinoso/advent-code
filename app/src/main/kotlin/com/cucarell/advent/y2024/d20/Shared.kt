package com.cucarell.advent.y2024.d20

enum class Map20Elements(val char: Char, val id: String) {
    Track('.', "Track"),
    START('S', "Start"), // Track too
    END('E', "End"),  // Track too
    WALL('#', "Wall"),
    ;

    companion object {
        fun of(char: Char): Map20Elements = entries.firstOrNull { me: Map20Elements -> me.char == char }
            ?: error("Unexpected char: '$char'")
    }
}
