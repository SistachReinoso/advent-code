package com.cucarell.advent.y2024.d17

import com.cucarell.advent.y2024.d17.OpCode.entries
import com.google.common.math.LongMath.pow

enum class OpCode(val id: Short) {
    ADV(0),
    BXL(1),
    BST(2),
    JNZ(3),
    BXC(4),
    OUT(5),
    BDV(6),
    CDV(7),
    ;

    companion object {
        fun of(v: Int): OpCode = entries
            .associateBy { oc: OpCode -> oc.id.toInt() }[v] ?: TODO("Unexpected OpCode: $v in 0..7")
    }
}

data class ChronospatialComputer(
    var a: Long = 0,
    var b: Long = 0,
    var c: Long = 0,
    var instructionPointer: Int = 0,
    val output: MutableList<Int> = mutableListOf()
) {
    fun executeCommands(commands: List<Int>) {
        while (instructionPointer < commands.size) {
            commands
                .slice(listOf(instructionPointer, instructionPointer + 1))
                .let { (opCode: Int, operant: Int) -> OpCode.of(opCode) to operant.toLong() }
                .let { (opCode: OpCode, operantValue: Long) -> operate(opCode, operantValue) }
            instructionPointer += 2
        }
    }

    fun operate(opCode: OpCode, operantValue: Long) {
        when (opCode) {
            OpCode.ADV -> a = division(combo(operantValue))
            OpCode.BXL -> b = myXor(b, operantValue)
            OpCode.BST -> b = modulo8(combo(operantValue))
            OpCode.JNZ -> if (a != 0L) instructionPointer = operantValue.toInt() - 2
            OpCode.BXC -> b = myXor(b, c)
            OpCode.OUT -> output.add(modulo8(combo(operantValue)).toInt())
            OpCode.BDV -> b = division(combo(operantValue))
            OpCode.CDV -> c = division(combo(operantValue))
        }
    }

    fun division(value: Long): Long = a shr value.toInt()
    fun modulo8(value: Long): Long = value % 8
    fun myXor(a: Long, b: Long): Long = a xor b

    private fun combo(operant: Long): Long = when (operant) {
        in 0L..3L, 7L -> operant.toLong() // TODO 7 not..., only for testing \O.o/
        4L -> a
        5L -> b
        6L -> c
        else -> TODO("Unexpected operant: $operant in 0..6")
    }
}
