package com.cucarell.advent.y2024.d24

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.test.assertEquals

class Day24Test {
    @Nested
    inner class Part1Test {
        @Test
        fun simpleTest() {
            val file: Path = object {}.javaClass.getResource("/2024/24/simply")!!.path.let(::Path)
            assertEquals(4L, part1(file))
        }

        @Test
        fun complexTest() {
            val file: Path = object {}.javaClass.getResource("/2024/24/complex")!!.path.let(::Path)
            assertEquals(2024L, part1(file))
        }
    }
}
