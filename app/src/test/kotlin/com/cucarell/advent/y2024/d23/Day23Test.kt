package com.cucarell.advent.y2024.d23

import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.test.Test
import kotlin.test.assertEquals

class Day23Test {
    val file: Path = object {}.javaClass.getResource("/2024/23/demo")!!.path.let(::Path)

    @Test
    fun part1Test() {
        val output: Long = part1(file)
        assertEquals(7, output)
    }

    @Test
    fun part2Test() {
        val output: String = part2(file)
        assertEquals("co,de,ka,ta", output)
    }
}
