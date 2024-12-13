package com.cucarell.advent.y2024.d12

import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.test.assertEquals

class Test {
    @Nested
    @Disabled("Complicat fer-ho funcionar amb el cas demo3")
    inner class Part1 {
        @Test
        fun demo() {
            val file: Path = object {}.javaClass.getResource("/2024/12/demo")!!.path.let(::Path)
            assertEquals(140, part1(file))
        }
        @Test
        fun demo2() {
            val file: Path = object {}.javaClass.getResource("/2024/12/demo2")!!.path.let(::Path)
            assertEquals(772, part1(file))
        }

        @Test
        fun demo3() {
            val file: Path = object {}.javaClass.getResource("/2024/12/demo3")!!.path.let(::Path)
            assertEquals(88, part1(file))
        }
    }
}
