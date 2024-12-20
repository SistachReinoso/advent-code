package com.cucarell.advent.y2024.d17

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ChronospatialComputerTest {
    @Test
    fun division() {
        val cc = ChronospatialComputer(a = 5L)
        assertEquals(1L, cc.division(2))
    }

    @Nested
    inner class SomeExamples {
        @Test
        fun one() {
            val cc = ChronospatialComputer(c = 9)
            val program = listOf(2, 6)
            cc.executeCommands(program)
            assertEquals(1L, cc.b)
        }

        @Test
        fun two() {
            val cc = ChronospatialComputer(a = 10)
            val program = listOf(5, 0, 5, 1, 5, 4)
            cc.executeCommands(program)
            assertEquals(listOf(0, 1, 2), cc.output)
        }

        @Test
        fun tree() {
            val cc = ChronospatialComputer(a = 2024)
            val program = listOf(0, 1, 5, 4, 3, 0)
            cc.executeCommands(program)
            assertEquals(listOf(4, 2, 5, 6, 7, 7, 7, 7, 3, 1, 0), cc.output)
            assertEquals(0L, cc.a)
        }

        @Test
        fun four() {
            val cc = ChronospatialComputer(b = 29)
            val program = listOf(1, 7)
            cc.executeCommands(program)
            assertEquals(26L, cc.b)
        }

        @Test
        fun five() {
            val cc = ChronospatialComputer(b = 2024, c = 43690)
            val program = listOf(4, 0)
            cc.executeCommands(program)
            assertEquals(44354L, cc.b)
        }
    }

    @Test
    fun examplePart1() {
        val cc = ChronospatialComputer(a = 729)
        val program = listOf(0, 1, 5, 4, 3, 0)
        cc.executeCommands(program)
        assertEquals(listOf(4, 6, 3, 5, 6, 3, 5, 2, 1, 0), cc.output)
    }
}
