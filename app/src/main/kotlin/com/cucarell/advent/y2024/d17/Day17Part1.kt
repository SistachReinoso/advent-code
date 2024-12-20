package com.cucarell.advent.y2024.d17

fun main() {
    val cc = ChronospatialComputer(a = 46323429)
    val registers = Registers(a = cc.a)
    val program = listOf(2, 4, 1, 1, 7, 5, 1, 5, 4, 3, 0, 3, 5, 5, 3, 0)
    cc.executeCommands(program)
    cc.output
        .joinToString(",")
        .let(::println)

    part1(registers, program)
        .joinToString(",")
        .let(::println)
}

data class Registers(
    var a: Long = 0L,
    var b: Long = 0L,
    var c: Long = 0L,
    var index: Int = 0,
    val output: MutableList<Int> = mutableListOf()
) {
    fun combo(operant: Int): Long = when (operant) {
        in 0..3 -> operant.toLong()
        4 -> a
        5 -> b
        6 -> c
        else -> error("Unsupported operant: $operant !in 0..6")
    }

    fun opcode(opcode: Int, operand: Int): Unit {
        when (opcode) {
            0 -> a = a shr combo(operand).toInt()
            1 -> b = b xor operand.toLong()
            2 -> b = combo(operand) % 8
            3 -> if (a != 0L) index = operand - 2
            4 -> b = b xor c
            5 -> output += (combo(operand) % 8).toInt()
            6 -> b = a shr combo(operand).toInt()
            7 -> c = a shr combo(operand).toInt()
            else -> error("Unsupported opcode: $opcode !in 0..7")
        }
        index += 2
    }

    fun loopPart1(program: List<Int>) {
        val programSize = program.size
        while (index < programSize) {
            opcode(program[index], program[index + 1])
        }
    }
}

fun part1(registers: Registers, program: List<Int>): List<Int> {
    val myCheck = Registers(a = 729)
    myCheck.loopPart1(listOf(0, 1, 5, 4, 3, 0))
    registers.loopPart1(program)
    return registers.output
}
