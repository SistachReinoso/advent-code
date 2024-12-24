package com.cucarell.advent.y2024.d20

import com.cucarell.advent.utils.Coordinates
import org.junit.jupiter.api.Test
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.test.assertEquals

class Day20Test {
    val file: Path = object {}.javaClass.getResource("/2024/20/demo")!!.path.let(::Path)

    @Test
    fun part1Test() {
        val a =
            //part1(file)
            part2(file, save = 1L, cheat = 2)
        println(a)
        //assertEquals(84, a[0])
        assertEquals(14, a[2])
        assertEquals(14, a[4])
        assertEquals(2, a[6])
        assertEquals(4, a[8])
        assertEquals(2, a[10])
        assertEquals(3, a[12])
        assertEquals(1, a[20])
        assertEquals(1, a[36])
        assertEquals(1, a[38])
        assertEquals(1, a[38])
        assertEquals(1, a[40])
        assertEquals(1, a[64])
    }

    @Test
    fun part2Test() {
        val a = part2(file, save = 50L)
        println(a)
        assertEquals(32, a[50])
        assertEquals(31, a[52])
        assertEquals(29, a[54])
        assertEquals(39, a[56])
        assertEquals(25, a[58])
        assertEquals(23, a[60])
        assertEquals(20, a[62])
        assertEquals(19, a[64])
        assertEquals(12, a[66])
        assertEquals(14, a[68])
        assertEquals(12, a[70])
        assertEquals(22, a[72])
        assertEquals(4, a[74])
        assertEquals(3, a[76])
    }

    @Test
    fun notUtilsBecauseToSpecific() {
        val iterator: Iterator<CheatResult> = generateFromDistance(Coordinates(10, 10), 10L, maxDistance = 3).iterator()

        assertEquals(CheatResult(Coordinates(10, 8), 12), iterator.next())
        assertEquals(CheatResult(Coordinates(11, 9), 12), iterator.next())
        assertEquals(CheatResult(Coordinates(12, 10), 12), iterator.next())
        assertEquals(CheatResult(Coordinates(11, 11), 12), iterator.next())
        assertEquals(CheatResult(Coordinates(10, 12), 12), iterator.next())
        assertEquals(CheatResult(Coordinates(9, 11), 12), iterator.next())
        assertEquals(CheatResult(Coordinates(8, 10), 12), iterator.next())
        assertEquals(CheatResult(Coordinates(9, 9), 12), iterator.next())

        assertEquals(CheatResult(Coordinates(10, 7), 13), iterator.next())
        assertEquals(CheatResult(Coordinates(11, 8), 13), iterator.next())
        assertEquals(CheatResult(Coordinates(12, 9), 13), iterator.next())
        assertEquals(CheatResult(Coordinates(13, 10), 13), iterator.next())
        assertEquals(CheatResult(Coordinates(12, 11), 13), iterator.next())
        assertEquals(CheatResult(Coordinates(11, 12), 13), iterator.next())
        assertEquals(CheatResult(Coordinates(10, 13), 13), iterator.next())
        assertEquals(CheatResult(Coordinates(9, 12), 13), iterator.next())
        assertEquals(CheatResult(Coordinates(8, 11), 13), iterator.next())
        assertEquals(CheatResult(Coordinates(7, 10), 13), iterator.next())
        assertEquals(CheatResult(Coordinates(8, 9), 13), iterator.next())
        assertEquals(CheatResult(Coordinates(9, 8), 13), iterator.next())
        assertEquals(false, iterator.hasNext())
    }
}
