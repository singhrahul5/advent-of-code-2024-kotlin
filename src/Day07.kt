fun main() {
    fun part1(input: List<String>): Long {

        fun findValidEquation(result: Long, operandIndex: Int, finalResult: Long, operandList: List<Long> ): Boolean {
            if (operandIndex == operandList.size)
                return result == finalResult

            return findValidEquation(result * operandList[operandIndex], operandIndex + 1, finalResult, operandList)
                    || findValidEquation(result + operandList[operandIndex], operandIndex + 1, finalResult, operandList)
        }
        return input.map { equation ->
            val result = equation.substring(0, equation.indexOf(":")).toLong()
            val operandList = equation.substring(equation.indexOf(":") + 2)
                .split(" ").map { it.toLong() }

            return@map  if (findValidEquation(operandList[0], 1, result, operandList))
                result
            else 0L
        }.sum()
    }

    fun part2(input: List<String>): Long {

        fun findValidEquation(result: Long, operandIndex: Int, finalResult: Long, operandList: List<Long> ): Boolean {
            if (operandIndex == operandList.size)
                return result == finalResult

            // calculate concat result if concatenation is required
            var tempOperand = operandList[operandIndex]
            var concatResult = result
            while (tempOperand > 0) {
                tempOperand /= 10
                concatResult *= 10
            }
            concatResult += operandList[operandIndex]

            return findValidEquation(result * operandList[operandIndex], operandIndex + 1, finalResult, operandList) // using * operator
                    || findValidEquation(result + operandList[operandIndex], operandIndex + 1, finalResult, operandList) // using + operator
                    || findValidEquation(concatResult, operandIndex + 1, finalResult, operandList) // using || concat operator
        }
        return input.map { equation ->
            val result = equation.substring(0, equation.indexOf(":")).toLong()
            val operandList = equation.substring(equation.indexOf(":") + 2)
                .split(" ").map { it.toLong() }

            return@map  if (findValidEquation(operandList[0], 1, result, operandList))
                result
            else 0L
        }.sum()
    }

    val input = """
        190: 10 19
        3267: 81 40 27
        83: 17 5
        156: 15 6
        7290: 6 8 6 15
        161011: 16 10 13
        192: 17 8 14
        21037: 9 7 18 13
        292: 11 6 16 20
    """.trimIndent().lines()

    check(part1(input) == 3749L)
    check(part2(input) == 11387L)

    part1(readInput("Day07")).println()
    part2(readInput("Day07")).println()
}