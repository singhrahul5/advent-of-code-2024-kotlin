import kotlin.math.max

fun main() {
    fun nextSecretNum(secretNum: Int): Int {
        // step 1
        var result: Long = secretNum.toLong() * 64
        var nextSecretNum = secretNum.toLong() xor result // mix
        nextSecretNum %= 16777216 // prune
        //step 2
        result = nextSecretNum / 32
        nextSecretNum = nextSecretNum xor result // mix
        nextSecretNum %= 16777216 // prune
        //step 3
        result = nextSecretNum * 2048
        nextSecretNum = nextSecretNum xor result // mix
        nextSecretNum %= 16777216 // prune

        return nextSecretNum.toInt()
    }

//    check(nextSecretNum(123) == 15887950)

    fun part1(input: List<String>): Long {
        var ans = 0L
        for (secretNumStr in input) {
            var secretNum = secretNumStr.toInt()
            var times = 2000
            while (times-- > 0)
                secretNum = nextSecretNum(secretNum)

            ans += secretNum
        }
        return ans
    }

    fun part2(input: List<String>): Int {
        data class Changes(
            val firstChange: Int,
            val secondChange: Int,
            val thirdChange: Int,
            val fourthChange: Int
        )

        val changeSequencesProfitMap = mutableMapOf<Changes, Int>()
        for (secretNumStr in input) {
            val changeSequenceSet = mutableSetOf<Changes>()
            var sn = secretNumStr.toInt()
            var firstChange = -1
            var secondChange = -1
            var thirdChange = -1

            var times = 0
            while (times++ < 2000) {

                val nextSn = nextSecretNum(sn)
                val fourthChange = nextSn % 10 - sn % 10
                sn = nextSn

                val changeSeq = Changes(
                    firstChange,
                    secondChange,
                    thirdChange,
                    fourthChange
                )
                if (times >= 4 && changeSeq !in changeSequenceSet) {
                    changeSequenceSet.add(changeSeq)

                    changeSequencesProfitMap[changeSeq] =
                        (changeSequencesProfitMap[changeSeq] ?: 0) + (sn % 10)
                }

                firstChange = secondChange
                secondChange = thirdChange
                thirdChange = fourthChange
            }
        }
//        changeSequencesProfitMap.println()
        var maxProfit = 0
        for (profit in changeSequencesProfitMap.values) {
            maxProfit = max(maxProfit, profit)
        }
        return maxProfit
    }


    var input = """
        1
        10
        100
        2024
    """.trimIndent().lines()
    check(part1(input) == 37327623L)
    part1(readInput("Day22")).println()

    input = """
        1
        2
        3
        2024
    """.trimIndent().lines()

    check(part2(input) == 23)
    part2(readInput("Day22")).println()
}