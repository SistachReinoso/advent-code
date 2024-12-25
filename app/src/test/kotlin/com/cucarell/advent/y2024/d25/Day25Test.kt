package com.cucarell.advent.y2024.d25

import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class Day25Test {
    @Disabled("No asserts, only visual test")
    @Nested
    inner class Shared25Test {
        @Test
        fun listOfSchematics() {
            val list: List<Schematic> = listOf(
                Schematic.Key(listOf(1, 2, 3)),
                Schematic.Lock(listOf(2, 3, 4))
            ).onEach { println(it) }

            val (keySchematic, lockSchematic) = list.partition { schematic: Schematic ->
                when (schematic) {
                    is Schematic.Key -> true
                    is Schematic.Lock -> false
                }
            }

            keySchematic.first().also { s -> println("Key :: $s -> ${s.schematic}") }
            lockSchematic.first().also { s -> println("Lock:: $s -> ${s.schematic}") }
        }
    }

    @Nested
    inner class Parser25Test {
        @Test
        fun parser25Test() {
            val file: Path = object {}.javaClass.getResource("/2024/25/parse")!!.path.let(::Path)
            val output: List<Schematic> = parser25(file)
            val (keys: List<Schematic>, locks: List<Schematic>) = output.partition { s: Schematic -> s is Schematic.Key }
            assertEquals(1, keys.size)
            assertEquals(1, locks.size)
            assertEquals(listOf(5, 0, 2, 1, 3), keys.first().schematic)
            assertEquals(listOf(0, 5, 3, 4, 3), locks.first().schematic)
        }

        @Test
        fun parserDemo() {
            val file: Path = object {}.javaClass.getResource("/2024/25/demo")!!.path.let(::Path)
            val (keys: List<Schematic>, locks: List<Schematic>) = parser25(file).partition { s: Schematic -> s is Schematic.Key }
            val keysSet: Set<Schematic> = keys.toSet()
            assertEquals(2, locks.size)
            assertEquals(3, keys.size)
            assertTrue { Schematic.Lock(listOf(0, 5, 3, 4, 3)) in locks.toSet() } // mmm...
            assertTrue { Schematic.Lock(listOf(1,2,0,5,3)) in locks.toSet() } // mmm...

            assertTrue { Schematic.Lock(listOf(5, 0, 2, 1, 3)) in keysSet } // mmm...
            assertTrue { Schematic.Key(listOf(5, 0, 2, 1, 3)) in keysSet }
            assertTrue { Schematic.Key(listOf(4, 3, 4, 0, 2)) in keysSet }
            assertTrue { Schematic.Key(listOf(3, 0, 2, 0, 1)) in keysSet }
            // TODO assertTrue { listOf(5, 0, 2, 1, 3) in keys.toSet() }
        }
    }

    @Nested
    inner class Day25Part1Test {
        @Test
        fun demoTest() {
            val file: Path = object {}.javaClass.getResource("/2024/25/demo")!!.path.let(::Path)
            val output: Long = part1(file)
        }
    }
}
