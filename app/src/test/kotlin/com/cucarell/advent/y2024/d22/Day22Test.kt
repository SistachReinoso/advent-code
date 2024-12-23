package com.cucarell.advent.y2024.d22

import org.junit.jupiter.api.Test
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.test.assertEquals

class Day22Test {
    val file: Path = object {}.javaClass.getResource("/2024/22/demo")!!.path.let(::Path)

    @Test
    fun part1Test() {
        val solution: Long = part1(file)
        assertEquals(37327623L, solution)
    }
}
