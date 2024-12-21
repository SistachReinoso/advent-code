package com.cucarell.advent.y2024.d19

import org.junit.jupiter.api.Test
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.test.assertEquals

class Day19Test {
    @Test
    fun part1Test() {
        val file: Path = object {}.javaClass.getResource("/2024/19/demo")!!.path.let(::Path)
        val output: Int = part1(file = file)
        assertEquals(6, output)
    }
}
