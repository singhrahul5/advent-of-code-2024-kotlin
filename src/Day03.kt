fun main() {
    fun part1(input: List<String>): Int {

        val regex = "mul\\((\\d{1,3}),(\\d{1,3})\\)".toRegex()

        val instructions = regex.findAll(input.joinToString(""))
//        instructions.toList().println()
        var ans = 0
        for (instruction in instructions) {
//            instruction.groupValues.println()
            val (_, num1, num2) = instruction.groupValues

            ans += num1.toInt() * num2.toInt()
        }
        return ans
    }

    fun part2(input: List<String>): Int {

        val regex = "mul\\((\\d{1,3}),(\\d{1,3})\\)|do\\(\\)|don't\\(\\)".toRegex()

        val instructions = regex.findAll(input.joinToString(""))
//        instructions.toList().println()
        var ans = 0
        var enable = true
        for (instruction in instructions) {
            if (instruction.value == "do()"){
                enable = true
            } else if(instruction.value == "don't()") {
                enable = false
            } else if (enable) {
                val (_, num1, num2) = instruction.groupValues

                ans += num1.toInt() * num2.toInt()
            }
        }
        return ans
    }

    var input =
        listOf("xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))")

    check(part1(input) == 161)

    input = listOf("xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))")
    check(part2(input) == 48)

    part1(readInput("Day03")).println()
    part2(readInput("Day03")).println()
}