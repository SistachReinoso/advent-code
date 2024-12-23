package com.cucarell.advent.y2024.d20

import org.junit.jupiter.api.Test
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.test.assertEquals

class Day20Test {
    val file: Path = object {}.javaClass.getResource("/2024/20/demo")!!.path.let(::Path)

    @Test
    fun part1Test() {
        val a = part1(file)
        /*
        assertEquals(84L, a[0])
        assertEquals(14L, a[2])
        assertEquals(14L, a[4])
        assertEquals(2L, a[6])
        assertEquals(4L, a[8])
        assertEquals(2L, a[10])
        assertEquals(3L, a[12])
        assertEquals(1L, a[20])
        assertEquals(1L, a[36])
        assertEquals(1L, a[38])
        assertEquals(1L, a[38])
        assertEquals(1L, a[40])
        assertEquals(1L, a[64])
         */
    }
}
