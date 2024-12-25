package com.cucarell.advent.y2024.d25

sealed class Schematic(val schematic: List<Int>) {
    class Key(schematic: List<Int>) : Schematic(schematic) {
        override fun toString(): String = "key : ${super.toString()}"
    }

    class Lock(schematic: List<Int>) : Schematic(schematic) {
        override fun toString(): String = "Lock: ${super.toString()}"
    }

    override fun toString(): String = schematic.joinToString(prefix = "(", postfix = ")")
    override fun equals(o: Any?): Boolean = when (o) {
        is List<*> -> schematic == o
        is Schematic -> schematic == o.schematic
        else -> super.equals(o)
    }

    override fun hashCode(): Int = schematic.hashCode()
    fun withoutOverlap(o: Schematic): Boolean = schematic.zip(o.schematic)
        .all { pair: Pair<Int, Int> -> pair.first + pair.second < 6 }
}
