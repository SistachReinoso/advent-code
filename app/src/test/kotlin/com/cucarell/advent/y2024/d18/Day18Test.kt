package com.cucarell.advent.y2024.d18

import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.test.Test
import kotlin.test.assertEquals

class Day18Test {
    @Test
    fun part1Test() {
        val file: Path = object {}.javaClass.getResource("/2024/18/demo")!!.path.let(::Path)
        val output: Int = part1(size = 6, fallen = 12, file = file)
        assertEquals(22, output)
    }

    @Test
    fun part2Test() {
        val file: Path = object {}.javaClass.getResource("/2024/18/demo")!!.path.let(::Path)
        val output: String = part2(size = 6, fallen = 12, file = file)
        assertEquals("6,1", output)
    }
}
