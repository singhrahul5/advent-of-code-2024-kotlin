import kotlin.math.abs

fun main() {
    fun part1(input: List<String>): Int {
        val group1List = mutableListOf<Int>()
        val group2List = mutableListOf<Int>()

        input.forEach { t ->
            t.split("\\s+".toRegex()).also{
                group1List.add(it[0].toInt())
                group2List.add(it[1].toInt())
            }
        }

        group1List.sort()
        group2List.sort()

        var ans = 0
        for ((g1, g2) in group1List zip group2List) {
            ans += abs(g1 - g2)
        }


        return ans
    }

    fun part2(input: List<String>): Int {
        val group1List = mutableListOf<Int>()
        val group2Map = mutableMapOf<Int, Int>()

        input.forEach { t ->
            t.split("\\s+".toRegex()).also{
                group1List.add(it[0].toInt())
                group2Map[it[1].toInt()] = (group2Map[it[1].toInt()] ?: 0) + 1
            }
        }

        var ans = 0
        for (g1 in group1List) {
            ans += g1 * (group2Map[g1] ?: 0)
        }


        return ans
    }
    var input = """
        3   4
        4   3
        2   5
        1   3
        3   9
        3   3
    """.trimIndent().lines()
    check(part1(input) == 11)
    check(part2(input) == 31)

    part1(readInput("Day01")).println()
    part2(readInput("Day01")).println()
}