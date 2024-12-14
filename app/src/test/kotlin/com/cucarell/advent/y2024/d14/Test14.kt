package com.cucarell.advent.y2024.d14

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Test14 {
    @Nested
    inner class Robot {
        @Test
        fun example() {
            val p = Coordinates(2, 4)
            val v = Coordinates(2, -3)
            val size = Coordinates(11, 7)
            val robot = Robot(p, v, size)

            assertEquals(Coordinates(4, 1), robot.getPositionAfter(1))
            assertEquals(Coordinates(6, 5), robot.getPositionAfter(2))
            assertEquals(Coordinates(8, 2), robot.getPositionAfter(3))
            assertEquals(Coordinates(10, 6), robot.getPositionAfter(4))
            assertEquals(Coordinates(1, 3), robot.getPositionAfter(5))
        }
    }
}
