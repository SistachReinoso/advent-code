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

    @Nested
    inner class Part2Test {
        @Test
        fun readInput() {
            val file: Path = object {}.javaClass.getResource("/2024/24/readInput")!!.path.let(::Path)
            val (values: MutableMap<WireKey, Boolean>, _, allWiresKey: Set<WireKey>) = parser24(file)
            val x: Long = getLongValue('x', values, allWiresKey)
            val y: Long = getLongValue('y', values, allWiresKey)
            val z: Long = getLongValue('z', values, allWiresKey)

            assertEquals(11L, x)
            assertEquals(13L, y)
            assertEquals(24L, z)
        }

        @Test
        fun binarySum() {
            //           result a      +  b      +  carry
            assertEquals(false, false xor false xor false) // 0 0 0
            assertEquals( true, false xor false xor  true) // 0 0 1
            assertEquals( true, false xor  true xor false) // 0 1 0
            assertEquals(false, false xor  true xor  true) // 0 1 1
            assertEquals( true, true  xor false xor false) // 1 0 0
            assertEquals(false, true  xor false xor  true) // 1 0 1
            assertEquals(false, true  xor  true xor false) // 1 1 0
            assertEquals( true, true  xor  true xor  true) // 1 1 1
        }
    }
}
