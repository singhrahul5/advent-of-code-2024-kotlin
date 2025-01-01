import java.util.*

enum class Direction(val moveRow: Int, val moveCol: Int) {
    EAST(0, 1),
    WEST(0, -1),
    NORTH(-1, 0),
    SOUTH(1, 0);

    fun getOpposite(): Direction {
        return when (this) {
            EAST -> WEST
            WEST -> EAST
            NORTH -> SOUTH
            SOUTH -> NORTH
        }
    }
}

fun main() {
    data class Info(
        val row: Int,
        val col: Int,
        val direction: Direction,
        val score: Int
    ) : Comparable<Info> {
        override fun compareTo(other: Info): Int {
            return score - other.score
        }
    }


    fun part1(input: List<String>): Int {
        val n = input.size
        val m = input[0].length


        // find start
        var startRow = -1
        var startCol = -1
        //find end
        var endRow = -1
        var endCol = -1
        for (row in 0..<n) {
            if (input[row].indexOf('S') >= 0) {
                startRow = row
                startCol = input[row].indexOf('S')
            }
            if (input[row].indexOf('E') >= 0) {
                endRow = row
                endCol = input[row].indexOf('E')
            }
        }

//        println("$startRow $startCol")

        /*
        val visited = Array(n) { BooleanArray(m) }
        fun findPathScore(row: Int, col: Int, currDirection: Direction): Int {
            if (input[row][col] == 'E')
                return 0

            visited[row][col] = true

            var ans = (1e8).toInt()

            for (nextDirection in Direction.entries) {
                val nextRow = row + nextDirection.moveRow
                val nextCol = col + nextDirection.moveCol

                if (
                    nextRow in 0..<n && nextCol in 0..<m
                    && input[nextRow][nextCol] != '#' && !visited[nextRow][nextCol]
                ) {
                    val temp =
                        findPathScore(nextRow, nextCol, nextDirection) +
                                when (nextDirection) {
                                    currDirection -> 1
                                    currDirection.getOpposite() -> 2001
                                    else -> 1001
                                }
                    ans = min(ans, temp)
                }
            }

            visited[row][col] = false

            return ans
        }

        return findPathScore(startRow, startCol, Direction.EAST)
        */

        // DIJKSTRA SHORTEST PATH

        val dist = Array(n) { IntArray(m) { Int.MAX_VALUE } }

        val queue: Queue<Info> = PriorityQueue()

        queue.add(Info(startRow, startCol, Direction.EAST, 0))
        dist[startRow][startCol] = 0


        while (queue.isNotEmpty()) {
            val curr = queue.poll()!!

            for (nextDirection in Direction.entries) {
                val nextRow = curr.row + nextDirection.moveRow
                val nextCol = curr.col + nextDirection.moveCol

                val scoreIncrement = when (nextDirection) {
                    curr.direction -> 1
                    curr.direction.getOpposite() -> 2001
                    else -> 1001
                }
                if (
                    nextRow in 0..<n && nextCol in 0..<m
                    && input[nextRow][nextCol] != '#'
                    && dist[nextRow][nextCol] > curr.score + scoreIncrement
                ) {
                    dist[nextRow][nextCol] = curr.score + scoreIncrement
                    queue.add(
                        Info(
                            nextRow,
                            nextCol,
                            nextDirection,
                            dist[nextRow][nextCol]
                        )
                    )
                }
            }
        }

        return dist[endRow][endCol]
    }

    fun part2(input: List<String>): Int {

        data class PathNode(
            val row: Int,
            val col: Int,
            val direction: Direction,
            val score: Int
        ) : Comparable<PathNode> {
            override fun compareTo(other: PathNode): Int = score - other.score
        }

        data class Record(
            val row: Int,
            val col: Int,
            val direction: Direction
        )

        val n = input.size
        val m = input[0].length

        // find start
        var startRow = -1
        var startCol = -1
        //find end
        var endRow = -1
        var endCol = -1
        for (row in 0..<n) {
            for (col in 0..<m) {
                if (input[row][col] == 'S') {
                    startRow = row
                    startCol = col
                } else if (input[row][col] == 'E') {
                    endRow = row
                    endCol = col
                }
            }
        }

//        println("$startRow $startCol")
//        println("$endRow $endCol")

        val lowestScore: MutableMap<Record, Int> = mutableMapOf()
        val backtrackMap: MutableMap<Record, MutableSet<Record>> =
            mutableMapOf()
        val pq = PriorityQueue<PathNode>()

        pq.add(PathNode(startRow, startCol, Direction.EAST, 0))
        // source node
        lowestScore[Record(startRow, startCol, Direction.EAST)] = 0

        var bestScore = Int.MAX_VALUE
        val endStateRecords: MutableSet<Record> = mutableSetOf()

        while (pq.isNotEmpty()) {
            val curr = pq.poll()!!

            if (curr.row == endRow && curr.col == endCol) {
                if (curr.score > bestScore) break
                bestScore = curr.score
                endStateRecords.add(Record(curr.row, curr.col, curr.direction))
            }

            for (nextDirection in Direction.entries) {
                val nextRow = curr.row + nextDirection.moveRow
                val nextCol = curr.col + nextDirection.moveCol

                if (input[nextRow][nextCol] == '#') continue

                val nextScore = curr.score + when (nextDirection) {
                    curr.direction -> 1
                    curr.direction.getOpposite() -> 2001
                    else -> 1001
                }
                val nextRecord = Record(nextRow, nextCol, nextDirection)

                val nextCellLowestScore = lowestScore[nextRecord] ?: Int.MAX_VALUE

                if (nextCellLowestScore < nextScore) continue

                if (nextCellLowestScore > nextScore) {
                    backtrackMap[nextRecord] = mutableSetOf()
                    lowestScore[nextRecord] = nextScore
                }

                backtrackMap[nextRecord]!!.add(
                    Record(
                        curr.row,
                        curr.col,
                        curr.direction
                    )
                )
                pq.add(PathNode(nextRow, nextCol, nextDirection, nextScore))
            }

        }

//        endStateRecords.println()
//        lowestScore.println()
//        backtrackMap.println()

        // backtrack the best paths using bfs
        val queue = ArrayDeque(endStateRecords)
        val seen = mutableSetOf<Record>()
        seen.addAll(endStateRecords)

        while(queue.isNotEmpty()) {
            val curr = queue.poll()!!
//            backtrackMap[curr].println()
            for (last in backtrackMap[curr] ?: setOf()) {
                if (last in seen) continue
                seen.add(last)
                queue.add(last)
            }
        }

        return mutableSetOf<Pair<Int, Int>>().apply{
            seen.forEach { t ->
                add(t.row to t.col)
            }
        }.size
    }

    var input = """
        ###############
        #.......#....E#
        #.#.###.#.###.#
        #.....#.#...#.#
        #.###.#####.#.#
        #.#.#.......#.#
        #.#.#####.###.#
        #...........#.#
        ###.#.#####.#.#
        #...#.....#.#.#
        #.#.#.###.#.#.#
        #.....#...#.#.#
        #.###.#.#.#.#.#
        #S..#.....#...#
        ###############
    """.trimIndent().lines()

    check(part1(input) == 7036)
    check(part2(input) == 45)

    /*
        input = """
            #####
            #..E#
            #.#.#
            #.#.#
            #.#.#
            #S..#
            #####
        """.trimIndent().lines()
        check(part1(input) == 1006)
     */

    input = """
        #################
        #...#...#...#..E#
        #.#.#.#.#.#.#.#.#
        #.#.#.#...#...#.#
        #.#.#.#.###.#.#.#
        #...#.#.#.....#.#
        #.#.#.#.#.#####.#
        #.#...#.#.#.....#
        #.#.#####.#.###.#
        #.#.#.......#...#
        #.#.###.#####.###
        #.#.#...#.....#.#
        #.#.#.#####.###.#
        #.#.#.........#.#
        #.#.#.#########.#
        #S#.............#
        #################
    """.trimIndent().lines()
    check(part1(input) == 11048)
    check(part2(input) == 64)


    part1(readInput("Day16")).println()
    part2(readInput("Day16")).println()

}