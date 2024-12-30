fun main() {
    fun part1(input: List<String>): Int {
        val moves = arrayOf(
            intArrayOf(-1, 0), intArrayOf(-1, 1), intArrayOf(0, 1), intArrayOf(1, 1),
            intArrayOf(1, 0), intArrayOf(1, -1), intArrayOf(0, -1), intArrayOf(-1, -1)
        )

        val n = input.size
        val m = input[0].length

        var xmasCount = 0

        for (i in 0..<n) {
            for (j in 0..<m) {

                if (input[i][j] == 'X') {
                    for ((moveRow, moveCol) in moves) {

                        var row = i + moveRow
                        var col = j + moveCol

                        var flag = true
                        for (ch in "MAS") {
                            if (row < 0 || row >= n || col < 0 || col >= m || input[row][col] != ch) {
                                flag = false
                                break
                            }
                            row += moveRow
                            col += moveCol
                        }

                        if (flag) xmasCount ++
                    }
                }
            }
        }

        return xmasCount
    }

    fun part2(input: List<String>): Int {

        val corners = arrayOf(
            0 to 0, 0 to 2, 2 to 2, 2 to 0
        )
        val n = input.size
        val m = input[0].length

        var xmasCount = 0

        for (i in 0..<(n-2)) {
            for (j in 0..<(m-2)) {
                if ( input[i+1][j+1] == 'A') {
                    var m1 = -1
                    var m2 = -1
                    var s1 = -1
                    var s2 = -1

                    for (corner in corners.indices) {
                        val (moveRow, moveCol) = corners[corner]
                        if (input[i+moveRow][j+moveCol] == 'M') {
                            m1 = m2
                            m2 = corner
                        } else if (input[i+moveRow][j+moveCol] == 'S') {
                            s1 = s2
                            s2 = corner
                        }
                    }

                    if (m1 != -1 && m2 != -1 && s1 != -1 && s2 != -1 && m1 %2 != m2%2)
                        xmasCount ++

                }
            }
        }

        return xmasCount
    }

    var input = """
        MMMSXXMASM
        MSAMXMSMSA
        AMXSXMAAMM
        MSAMASMSMX
        XMASAMXAMM
        XXAMMXXAMA
        SMSMSASXSS
        SAXAMASAAA
        MAMMMXMMMM
        MXMXAXMASX
    """.trimIndent().lines()
    check(part1(input) == 18)
    check(part2(input) == 9)

    part1(readInput("Day04")).println()
    part2(readInput("Day04")).println()
}