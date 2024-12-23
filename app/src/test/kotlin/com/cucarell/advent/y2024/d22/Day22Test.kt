package com.cucarell.advent.y2024.d22

import org.junit.jupiter.api.Test
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.test.assertEquals

class Day22Test {

    @Test
    fun part1Test() {
        val file: Path = object {}.javaClass.getResource("/2024/22/demo")!!.path.let(::Path)
        val solution: Long = part1(file)
        assertEquals(37327623L, solution)
    }

    @Test
    fun part2Demo123() {
        generateChanges(123, 10)
            .forEach { println(it) }
    }

    @Test
    fun part2Demo2() {
        val file: Path = object {}.javaClass.getResource("/2024/22/demo2")!!.path.let(::Path)
        val solution: Long = part2(file)
        // -2,1,-1,3
        assertEquals(23L, solution)
    }
}

// Only add if not exists
class MapTest {
    @Test
    fun toMap() {
        val map = listOf(0 to 0, 0 to 1)
            .toMap()
        assertEquals(0, map[0]) // But is 1...
    }

    @Test
    fun mutableMap() {
        val map = mutableMapOf(0 to '0')
        map.putIfAbsent(0, '1')
        assertEquals('0', map[0])
    }

    @Test
    fun linkedHashMap() {
        val map = listOf(0 to 0, 0 to 1)
            .toMap(LinkedHashMap())

        assertEquals(0, map[0]) // But is 1...
    }
}
