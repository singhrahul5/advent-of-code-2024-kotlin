enum class Side(val moveRow: Int, val moveCol: Int) {
    LEFT(0, -1), RIGHT(0, 1), TOP(-1, 0), BOTTOM(1, 0)
}

fun main() {

    fun part1(input: List<String>): Int {
        val n = input.size
        val m = input[0].length

        val visited: Array<BooleanArray> = Array(n) { BooleanArray(m) }

        fun findRegionAreaAndPerimeter(
            row: Int,
            col: Int,
            plant: Char,
        ): Pair<Int, Int> {
            if (row < 0 || row == n || col < 0 || col == m || input[row][col] != plant)
                return 0 to 1
            if (visited[row][col]) return 0 to 0

            visited[row][col] = true
            var area = 1
            var perimeter = 0

            //up
            with(findRegionAreaAndPerimeter(row - 1, col, plant)) {
                area += first
                perimeter += second
            }
            //down
            with(findRegionAreaAndPerimeter(row + 1, col, plant)) {
                area += first
                perimeter += second
            }
            //left
            with(findRegionAreaAndPerimeter(row, col + 1, plant)) {
                area += first
                perimeter += second
            }
            //right
            with(findRegionAreaAndPerimeter(row, col - 1, plant)) {
                area += first
                perimeter += second
            }

            return area to perimeter
        }

        var priceOfFence = 0
        for (row in 0..<n) {
            for (col in 0..<m) {
                if (!visited[row][col]) {
                    with(
                        findRegionAreaAndPerimeter(
                            row,
                            col,
                            input[row][col],
                        )
                    ) {
                        priceOfFence += first * second
                    }
                }
            }
        }
        return priceOfFence
    }

    fun part2(input: List<String>): Int {
        val n = input.size
        val m = input[0].length

        val regions: Array<IntArray> = Array(n) { IntArray(m) }

        fun findRegionAreaAndAssignId(
            row: Int,
            col: Int,
            plant: Char,
            id: Int
        ): Int {
            if (row < 0 || row == n || col < 0 || col == m || input[row][col] != plant || regions[row][col] != 0)
                return 0

            regions[row][col] = id
            return 1 + findRegionAreaAndAssignId(row - 1, col, plant, id) + //up
                    findRegionAreaAndAssignId(row + 1, col, plant, id) + //down
                    findRegionAreaAndAssignId(row, col + 1, plant, id) + //left
                    findRegionAreaAndAssignId(row, col - 1, plant, id)  //right

        }

        fun isSide(row: Int, col: Int, side: Side): Boolean {
            val sideBlockRow = row + side.moveRow
            val sideBlockCol = col + side.moveCol

            return sideBlockRow < 0 || sideBlockRow == n
                    || sideBlockCol < 0 || sideBlockCol == m
                    || regions[row][col] != regions[sideBlockRow][sideBlockCol]
        }

        fun findRegionSides(id: Int, side: Side): Int {
            var ans = 0
            when (side) {
                Side.LEFT, Side.RIGHT -> { // to find left or right side of region
                    for (col in 0..<m) {
                        for (row in 0..<n) {
                            if (regions[row][col] == id && isSide(row, col, side)
                                && (row - 1 < 0 || regions[row-1][col] != id  || !isSide(row - 1, col, side))
                            )
                                ans++
                        }
                    }
                }

                Side.TOP, Side.BOTTOM -> { // to find top or bottom side of region
                    for (row in 0..<n) {
                        for (col in 0..<m) {
                            if (regions[row][col] == id && isSide(row, col, side)
                                && (col - 1 < 0 || regions[row][col-1] != id  || !isSide(row , col-1, side))
                            )
                                ans++
                        }
                    }
                }
            }
            return ans
        }

        var priceOfFence = 0
        var regionId = 1
        for (row in 0..<n) {
            for (col in 0..<m) {
                if (regions[row][col] == 0) {
                    val area = findRegionAreaAndAssignId(row, col, input[row][col], regionId)
                    val sides = Side.entries.sumOf {
                        findRegionSides(regionId, it)
                    }
                    priceOfFence += area * sides
                    regionId++
                }
            }
        }
        return priceOfFence
    }

    var input = """
        AAAA
        BBCD
        BBCC
        EEEC
    """.trimIndent().lines()
    check(part1(input) == 140)
    check(part2(input) == 80)

    input = """
        OOOOO
        OXOXO
        OOOOO
        OXOXO
        OOOOO
    """.trimIndent().lines()
    check(part1(input) == 772)
    check(part2(input) == 436)


    input = """
        RRRRIICCFF
        RRRRIICCCF
        VVRRRCCFFF
        VVRCCCJFFF
        VVVVCJJCFE
        VVIVCCJJEE
        VVIIICJJEE
        MIIIIIJJEE
        MIIISIJEEE
        MMMISSJEEE
    """.trimIndent().lines()
    check(part1(input) == 1930)
    check(part2(input) == 1206)

    part1(readInput("Day12")).println()
    part2(readInput("Day12")).println()
}