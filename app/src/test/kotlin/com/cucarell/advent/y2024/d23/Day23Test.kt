package com.cucarell.advent.y2024.d23

import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.test.Test
import kotlin.test.assertEquals

class Day23Test {
    @Test
    fun part1Test() {
        val file: Path = object {}.javaClass.getResource("/2024/23/demo")!!.path.let(::Path)
        val output: Long = part1(file)
        assertEquals(7, output)
    }
}
