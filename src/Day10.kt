fun main() {
    val moves = arrayOf(
        intArrayOf(-1, 0), // up
        intArrayOf(1, 0), // down
        intArrayOf(0, -1), // left
        intArrayOf(0, 1) // right
    )

    fun part1(input: List<String>): Int {
        val n: Int = input.size
        val m: Int = input[0].length

        fun hike(row: Int, col: Int, visited: MutableSet<Int>): Int {
            visited.add(row * m + col)
//            println("$row $col ${input[row][col]} $visited")
            if (input[row][col] == '9') {
                return 1
            }

            var ans = 0
            for ((moveRow, moveCol) in moves) {
                val nextRow = row + moveRow
                val nextCol = col + moveCol
                if (nextRow >= 0 && nextRow < n && nextCol >= 0 && nextCol < m
                    && (nextRow * m + nextCol) !in visited
                    && input[row][col].digitToInt() + 1 == input[nextRow][nextCol].digitToInt()
                ) {
//                    println("next move $nextRow $nextCol ${input[nextRow][nextCol].digitToInt()}")
                    ans += hike(nextRow, nextCol, visited)
                }
            }
            return ans
        }

//        hike(0, 2, mutableSetOf()).println()
        var trailheads = 0

        for (row in 0..<n) {
            for (col in 0..<m) {
                if (input[row][col] == '0') {
                    trailheads += hike(row, col, mutableSetOf())
//                    println("$row $col trailheads = $trailheads")
                }
            }
        }
        return trailheads
    }

    fun part2(input: List<String>): Int {
        val n: Int = input.size
        val m: Int = input[0].length

        fun hike(row: Int, col: Int): Int {

            if (input[row][col] == '9') {
                return 1
            }

            var ans = 0
            for ((moveRow, moveCol) in moves) {
                val nextRow = row + moveRow
                val nextCol = col + moveCol
                if (nextRow >= 0 && nextRow < n && nextCol >= 0 && nextCol < m
                    && input[row][col].digitToInt() + 1 == input[nextRow][nextCol].digitToInt()
                ) {
                    ans += hike(nextRow, nextCol)
                }
            }
            return ans
        }

        var trailheads = 0

        for (row in 0..<n) {
            for (col in 0..<m) {
                if (input[row][col] == '0') {
                    trailheads += hike(row, col)

                }
            }
        }
        return trailheads
    }

    var input = """89010123
        78121874
        87430965
        96549874
        45678903
        32019012
        01329801
        10456732""".lines().map { it.trim() }


    check(part1(input) == 36)
    check(part2(input) == 81)

    // aoc input day 10
    input = readInput("Day10")
    part1(input).println()
    part2(input).println()
}