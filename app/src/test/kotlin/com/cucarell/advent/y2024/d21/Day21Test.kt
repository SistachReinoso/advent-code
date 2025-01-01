package com.cucarell.advent.y2024.d21

import org.junit.jupiter.api.Nested
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class Day21Test {
    @Nested
    inner class Part1 {
        @Test
        fun part1Test() {
            val file: Path = object {}.javaClass.getResource("/2024/21/demo")!!.path.let(::Path)
            val output: Long = part1(file)
            assertEquals(126384L, output)
        }

        @Test
        fun part2Test1() {
            val file: Path = object {}.javaClass.getResource("/2024/21/demo")!!.path.let(::Path)
            val output: Long = part2(file, 2)
            assertEquals(126384L, output)
        }

        @Test
        fun code029A() {
            val output = calculateOutput("029A")
            assertEquals(68L * 29L, output)
        }

        @Test
        fun code980A() {
            val output: Long = calculateOutput("980A")
            assertEquals(60L * 980L, output)
        }

        @Test
        fun code179A() {
            val output: Long = calculateOutput("179A")
            assertEquals(68L * 179L, output)
        }

        @Test
        fun code456A() {
            val output: Long = calculateOutput("456A")
            assertEquals(64L * 456L, output)
        }

        @Test
        fun code379A() {
            val output: Long = calculateOutput("379A")
            assertEquals(64L * 379L, output)
        }

        @Test
        fun bestRoutesTest() {
            val bestRoutesValue = bestRoutes('A', '7', numericKeypad)
                .map { orders -> orders.joinToString("") { dk -> dk.char.toString() } }
                .toList()
            // panic!! assertTrue { "<<^^^A" in bestRoutesValue }
            assertTrue { "<^<^^A" in bestRoutesValue }
            assertTrue { "<^^<^A" in bestRoutesValue }
            assertTrue { "<^^^<A" in bestRoutesValue }
            assertTrue { "^<<^^A" in bestRoutesValue }
            assertTrue { "^<^<^A" in bestRoutesValue }
            assertTrue { "^<^^<A" in bestRoutesValue }
            assertTrue { "^^<<^A" in bestRoutesValue }
            assertTrue { "^^<^<A" in bestRoutesValue }
            assertTrue { "^^^<<A" in bestRoutesValue }
            assertEquals(9, bestRoutesValue.size)
        }
    }

    @Nested
    inner class Part2 {
        @Test
        fun part2Test() {
            val file: Path = object {}.javaClass.getResource("/2024/21/demo")!!.path.let(::Path)
            val output: Long = part2(file)
            // 82050061710, 72242026390, 81251039228, 80786362258, 77985628636
            assertEquals(154_115_708_116_294L, output)
        }
    }

    @Nested
    inner class Helper {
        @Test
        fun example029Lvl1() {
            val expected = "029A"
            val nr = getNumericRobot()
            assertEquals(expected, nr.execute("<A^A>^^AvvvA".toList().asSequence()).joinToString(""))
            assertEquals(expected, nr.execute("<A^A^>^AvvvA".toList().asSequence()).joinToString(""))
            assertEquals(expected, nr.execute("<A^A^^>AvvvA".toList().asSequence()).joinToString(""))
        }

        @Test
        fun example029Lvl2() {
            val expected = "<A^A>^^AvvvA"
            val dr = getDirectionalRobot()
            assertEquals(expected, dr.execute("v<<A>>^A<A>AvA<^AA>A<vAAA>^A".toList().asSequence()).joinToString(""))
        }

        @Test
        fun example029ALvl3() {
            val expected = "v<<A>>^A<A>AvA<^AA>A<vAAA>^A"
            val dr = getDirectionalRobot()
            assertEquals(
                expected,
                dr.execute("<vA<AA>>^AvAA<^A>A<v<A>>^AvA^A<vA>^A<v<A>^A>AAvA^A<v<A>A>^AAAvA<^A>A".toList().asSequence())
                    .joinToString("")
            )
        }

        @Test
        fun example029A() {
            val expected = "029A"
            val input = "<vA<AA>>^AvAA<^A>A<v<A>>^AvA^A<vA>^A<v<A>^A>AAvA^A<v<A>A>^AAAvA<^A>A"
            checkWay(expected, input)
        }

        @Test
        fun example980A() {
            val expected = "980A"
            val input = "<v<A>>^AAAvA^A<vA<AA>>^AvAA<^A>A<v<A>A>^AAAvA<^A>A<vA>^A<A>A"
            checkWay(expected, input)
        }

        @Test
        fun example179A() {
            val expected = "179A"
            val i = "<<vAA>A>^AAvA<^A>AvA^A<<vA>>^AAvA^A<vA>^AA<A>A<<vA>A>^AAAvA<^A>A"
            val e = "<v<A>>^A<vA<A>>^AAvAA<^A>A<v<A>>^AAvA^A<vA>^AA<A>A<v<A>A>^AAAvA<^A>A"
            checkWay(expected, i)
            checkWay(expected, e)
        }

        @Test
        fun example456A() {
            val expected = "456A"
            val m = "<<vAA>A>^AAvA<^A>AAvA^A<vA>^A<A>A<vA>^A<A>A<<vA>A>^AAvA<^A>A"
            val e = "<v<A>>^AA<vA<A>>^AAvAA<^A>A<vA>^A<A>A<vA>^A<A>A<v<A>A>^AAvA<^A>A"
            checkWay(expected, m)
            checkWay(expected, e)
        }
    }
}

fun checkWay(expected: String, input: String) {
    assertEquals(expected, fullRobot(input).joinToString(""))
}
