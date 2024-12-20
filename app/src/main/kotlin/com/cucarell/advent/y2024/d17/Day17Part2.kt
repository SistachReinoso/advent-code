package com.cucarell.advent.y2024.d17

fun main() {
    val program = listOf(2, 4, 1, 1, 7, 5, 1, 5, 4, 3, 0, 3, 5, 5, 3, 0)

    part2(program).let(::println)
}

fun part2(program: List<Int>): Long {
    var potentialSolution = listOf(0L)

    program.reversed()
        .forEach { wanted: Int ->
            potentialSolution = potentialSolution
                .map { ps: Long -> nextElements(ps, wanted, program) }
                .flatten()
                .onEach { ps: Long -> if (program == executeProgram(ps, program)) return ps }
                .map { ps: Long -> ps shl 3 }
            potentialSolution.forEach { ps -> println("${ps.toString(8)}: ${executeProgram(ps, program)}") }
        }

    return 42L
}

private fun nextElements(potentialSolution: Long, wanted: Int, program: List<Int>): List<Long> = (0..7)
    .map { i: Int -> potentialSolution + i }
    .filter { ps: Long -> wanted == executeProgram(ps, program).first() }

private fun part2Fail(program: List<Int>): Long {
    /* TODO manually
        for (i in 0L..7) {
            println("ex$i: ${executeProgram((6L * 8) * 8 * 8 + 5 * 8 + i, program)}")
        } // 6 0 5 4 ....
     */
    var a = 0L
    var solution = mutableListOf<Int>()
    for (e in program.reversed()) {
        val r = nextElement(a, e, program)
        solution.add(r)
        a = a + r shl 3 // (!) + te preferencia sobre el << (!)
        println("$solution: $a")
    }

    return a
}

private fun nextElement(a: Long, value0: Int, program: List<Int>): Int {
    for (i in 0..7) {
        val l = executeProgram(a + i, program)
        println("db: $i $l")
        if (value0 == l.first())
            return i
    }
    error("upis, searching $value0")
}

private fun executeProgram(a: Long, program: List<Int>): List<Int> {
    val cc = ChronospatialComputer(a = a)
    cc.executeCommands(program)
    return cc.output
}
