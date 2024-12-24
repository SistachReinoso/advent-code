package com.cucarell.advent.y2024.d24

enum class Gate {
    AND,
    OR,
    XOR,
    ;

    companion object {
        fun of(input: String): Gate = entries.first { gate: Gate -> gate.toString() == input }
    }
}

typealias WireKey = String
