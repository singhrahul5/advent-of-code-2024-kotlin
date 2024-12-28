import kotlin.math.abs

fun main() {
    fun part1(input: List<String>): Int {
        var safe = 0
        outer@ for (t in input) {
            val levels = t.split(" ").map { it.toInt() }

            if (levels.size == 2 && (abs(levels[0] - levels[1]) == 0 || abs(
                    levels[0] - levels[1]
                ) > 3)
            )
                continue

            for (i in 2..<levels.size) {
                val gap1 = levels[i] - levels[i - 1]
                val gap2 = levels[i - 1] - levels[i - 2]

                if (abs(gap1) == 0 || abs(gap1) > 3
                    || abs(gap2) == 0 || abs(gap2) > 3
                    || !(gap1 < 0 && gap2 < 0 || gap1 > 0 && gap2 > 0)
                )
                    continue@outer

            }

            safe++

        }

        return safe
    }

    fun safeAfterRemovingOneEle(levels: List<Int>): Boolean {

        outer@ for (i in 0..<levels.size) {
            var first = -1
            var second = -1

            for (j in 0..<levels.size) {
                if (j == i) continue

                if (first != -1 && second != -1) {
                    val gap1 = first - second
                    val gap2 = second - levels[j]

                    if (abs(gap1) == 0 || abs(gap1) > 3
                        || abs(gap2) == 0 || abs(gap2) > 3
                        || !(gap1 < 0 && gap2 < 0 || gap1 > 0 && gap2 > 0)
                    )
                        continue@outer
                }

                first = second
                second = levels[j]
            }

            return true
        }
        return false
    }

    fun part2(input: List<String>): Int {
        var safe = 0
        for (t in input) {
            val levels = t.split(" ").map { it.toInt() }

            safe += if (safeAfterRemovingOneEle(levels)) 1 else 0
        }
        return safe
    }

    var input = """
        7 6 4 2 1
        1 2 7 8 9
        9 7 6 2 1
        1 3 2 4 5
        8 6 4 4 1
        1 3 6 7 9
    """.trimIndent().lines()

    check(part1(input) == 2)
    check(part2(input) == 4)

    part1(readInput("Day02")).println()
    part2(readInput("Day02")).println()
}