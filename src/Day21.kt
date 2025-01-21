import kotlin.math.abs
import kotlin.math.min

fun main() {
    val numericalKeypad = listOf("789", "456", "123", "\u00000A")
    val directionalKeypad = listOf("\u0000^A", "<v>")
    fun findKeySeq(
        src: Char,
        dest: Char,
        keypad: List<String>
    ) : List<String> {
        var srcRow = -1
        var srcCol = -1
        var destRow = -1
        var destCol = -1

        for (i in keypad.indices) {
            for (j in keypad[0].indices) {
                if (keypad[i][j] == src){
                    srcRow = i
                    srcCol = j
                }
                if (keypad[i][j] == dest) {
                    destRow = i
                    destCol = j
                }
            }
        }

        val rowDiff = destRow - srcRow
        val colDiff = destCol - srcCol

        val path1 = (if (rowDiff >= 0) "v" else "^").repeat(abs(rowDiff)) +
                (if (colDiff >= 0) ">" else "<").repeat(abs(colDiff))

        val path2 = (if (colDiff >= 0) ">" else "<").repeat(abs(colDiff)) +
                (if (rowDiff >= 0) "v" else "^").repeat(abs(rowDiff))

        // check validity of path
        val paths = mutableListOf<String>()
        outer@for (num in 1..2) {
            val path = if (num == 1) path1 else path2
            var row = srcRow
            var col = srcCol
            for (direction in path) {
                val (rd, cd) = when (direction) {
                    '^' -> -1 to 0
                    'v' -> 1 to 0
                    '>' -> 0 to 1
                    else -> 0 to -1
                }
                row += rd
                col += cd
                if (keypad[row][col] == '\u0000')
                    continue@outer
            }
            paths.add(path + 'A')
        }
        return paths
    }

    fun part1(input: List<String>): Int {
        fun findSeqLength(codeKeys: String, times: Int): Int {
            var lastKey = 'A'
            var seqLength = 0
            for (key in codeKeys) {
                val nextRobotInputSeq = findKeySeq(
                    lastKey,
                    key,
                    directionalKeypad
                )

                lastKey = key

                var minLen = Int.MAX_VALUE
                for (nextSeq in nextRobotInputSeq) {
                    minLen = min(
                        minLen,
                        if (times == 1)
                            nextSeq.length
                        else
                            findSeqLength(nextSeq, times - 1)
                    )
                }
                seqLength += minLen
            }
            return seqLength
        }

        fun findSeqLengthFinal(codeKeys: String): Int {
            var lastKey = 'A'
            var seqLength = 0
            for (key in codeKeys) {
                val nextRobotInputSeq = findKeySeq(lastKey, key, numericalKeypad)
                lastKey = key

                var minLen = Int.MAX_VALUE
                for (nextSeq in nextRobotInputSeq) {
                    minLen = min(minLen, findSeqLength(nextSeq, 2))
                }
                seqLength += minLen
            }
            return seqLength
        }


        var result = 0
        for(code in input) {
            val seqLen = findSeqLengthFinal(code)
            val num = code.substring(0, code.length-1).toInt()
            result += seqLen * num
        }
        return result
    }

    fun part2(input: List<String>): Long {
        val dp = mutableMapOf<String, Array<Long?>>()

        fun findSeqLength(codeKeys: String, times: Int): Long {
            if (dp[codeKeys]?.get(times) != null)
                return dp[codeKeys]!![times]!!

            var lastKey = 'A'
            var seqLength: Long = 0L
            for (key in codeKeys) {
                val nextRobotInputSeq = findKeySeq(
                    lastKey,
                    key,
                    directionalKeypad
                )

                lastKey = key

                var minLen: Long = Long.MAX_VALUE
                for (nextSeq in nextRobotInputSeq) {
                    minLen = min(
                        minLen,
                        if (times == 1)
                            nextSeq.length.toLong()
                        else
                            findSeqLength(nextSeq, times - 1)
                    )
                }
                seqLength += minLen
            }
            if (dp[codeKeys] == null)
                dp[codeKeys] = arrayOfNulls(26)
            dp[codeKeys]!![times] = seqLength
            return seqLength
        }

        fun findSeqLengthFinal(codeKeys: String): Long {
            var lastKey = 'A'
            var seqLength: Long = 0L

            for (key in codeKeys) {
                val nextRobotInputSeq = findKeySeq(lastKey, key, numericalKeypad)
                lastKey = key

                var minLen: Long = Long.MAX_VALUE

                for (nextSeq in nextRobotInputSeq) {
                    minLen = min(minLen, findSeqLength(nextSeq, 25))
                }
                seqLength += minLen
            }
            return seqLength
        }


        var result: Long = 0L
        for(code in input) {
            val seqLen = findSeqLengthFinal(code)
            val num = code.substring(0, code.length-1).toInt()
            result += seqLen * num
        }
        return result
    }

    val input = """
        029A
        980A
        179A
        456A
        379A
    """.trimIndent().lines()
    check(part1(input) == 126384)
    part1(readInput("Day21")).println()
    part2(readInput("Day21")).println()
}