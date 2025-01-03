import kotlin.math.pow

fun main() {
    fun part1(input: List<String>): String {

        var a = input[0].substring(12).toInt()
        var b = input[1].substring(12).toInt()
        var c = input[2].substring(12).toInt()

        val program = input[4].substring(9).split(",").map { it.toInt() }

        val output = mutableListOf<Int>()

        var instructionPointer = 0
        while (instructionPointer < program.size) {
            val opcode = program[instructionPointer]
            val literalOperand = program[instructionPointer + 1]
            val comboOperand = when (literalOperand) {
                4 -> a
                5 -> b
                6 -> c
                else -> literalOperand
            }


            when (opcode) {

                0 -> a = a shr comboOperand // a = a / (2.0).pow(comboOperand).toInt()

                1 -> b = b xor literalOperand

                2 -> b = comboOperand and 0b111 // comboOperator % 8

                3 -> {
                    if ( a != 0) {
                        instructionPointer = literalOperand
                        continue
                    }
                }

                4 -> b = b xor c

                5 -> output.add(comboOperand and 0b111)

                6 -> b = a shr comboOperand // b = a / (2.0).pow(comboOperand).toInt()

                7 -> c = a shr comboOperand // c = a / (2.0).pow(comboOperand).toInt()
            }

            instructionPointer += 2
        }

        return output.joinToString(",")
    }

    fun part2(input: List<String>): Long {
        val program = input[4].substring(9).split(",").map { it.toInt() }

        fun find(prog: List<Int>, index: Int, ans: Long): Long {
            if (index < 0) return ans

            var a: Long
            var b: Long
            var c: Long
            for (rem in 0L..<8L) {
                a = ans shl 3 or rem
                b = rem
                b = b xor 1
                c = a shr b.toInt()
                b = b xor 4
//                a = a shr 3 // redundant
                b = b xor c
                if ((b % 8).toInt() == prog[index]) {
                    val sol = find(prog, index-1, ans shl 3 or rem)
                    if (sol == -1L) continue
                    return sol
                }

            }
            return -1L
        }

        return find(program, program.lastIndex, 0)
    }

    var input: List<String> = """
        Register A: 729
        Register B: 0
        Register C: 0

        Program: 0,1,5,4,3,0
    """.trimIndent().lines()

    check(part1(input) == "4,6,3,5,6,3,5,2,1,0")


    part1(readInput("Day17")).println()
    part2(readInput("Day17")).println()
}