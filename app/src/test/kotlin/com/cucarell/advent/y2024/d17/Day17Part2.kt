package com.cucarell.advent.y2024.d17

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day17Part2 {
    @Test
    fun checkResult() {
        val cc = ChronospatialComputer(a = 117440L)
        val program = listOf(0, 3, 5, 4, 3, 0)
        cc.executeCommands(program)
        assertEquals(program, cc.output)
    }
}
