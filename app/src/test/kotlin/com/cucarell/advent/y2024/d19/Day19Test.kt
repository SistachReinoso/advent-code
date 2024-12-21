package com.cucarell.advent.y2024.d19

import org.junit.jupiter.api.Test
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.test.assertEquals

class Day19Test {
    val file: Path = object {}.javaClass.getResource("/2024/19/demo")!!.path.let(::Path)

    @Test
    fun part1Test() {
        val output: Int = part1(file = file)
        assertEquals(6, output)
    }

    @Test
    fun part2Test() {
        val output: Long = part2(file = file)
        assertEquals(16L, output)
    }

    @Test
    fun part2gbbr() {
        val towels = TowelsData.of(setOf("g", "gb", "b", "br", "r",))
        val output: Long = numberCombinations("gbbr", towels)
        assertEquals(4L, output)
    }
}
