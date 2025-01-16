import java.util.*

fun main() {
    val moves = arrayOf(0 to 1, 0 to -1, 1 to 0, -1 to 0)

    fun part1(input: List<String>): Int {
        val racetrackMap = input.map { it.toCharArray() }

        var startRow = -1
        var startCol = -1

        outer@ for (row in racetrackMap.indices) {
            for (col in racetrackMap[0].indices) {
                if (racetrackMap[row][col] == 'S') {
                    startRow = row
                    startCol = col
                    break@outer
                }
            }
        }

        fun race(): Int {
            val queue = LinkedList<Pair<Int, Int>>()
            val visited = mutableSetOf(startRow to startCol)
            queue.add(startRow to startCol)
            var picoseconds = 0
            while (queue.isNotEmpty()) {
                var levelSize = queue.size
                while (levelSize-- > 0) {
                    val (currRow, currCol) = queue.poll()!!

                    if (racetrackMap[currRow][currCol] == 'E')
                        return picoseconds

                    for ((rd, cd) in moves) {
                        val nbrRow = currRow + rd
                        val nbrCol = currCol + cd

                        if (nbrRow !in racetrackMap.indices
                            || nbrCol !in racetrackMap[0].indices
                            || racetrackMap[nbrRow][nbrCol] == '#'
                            || nbrRow to nbrCol in visited
                        )
                            continue

                        queue.add(nbrRow to nbrCol)
                        visited.add(nbrRow to nbrCol)
                    }
                }

                picoseconds++
            }

            throw IllegalStateException("No valid path found for race")
        }

        val picosecondsTakenWithoutCheat = race()

        var bestCheats = 0
        for (row in racetrackMap.indices) {
            for (col in racetrackMap[0].indices) {
                if (racetrackMap[row][col] != '#') continue

                racetrackMap[row][col] = '.'
                val picosecondsTakenWithCheat = race()
                racetrackMap[row][col] = '#'

                if (picosecondsTakenWithoutCheat - picosecondsTakenWithCheat >= 100)
                    bestCheats++

            }
        }

        return bestCheats
    }

    fun part2(input: List<String>): Int {

        val rowRange = input.indices
        val colRange = input[0].indices

        var startRow = -1
        var startCol = -1

        var endRow = -1
        var endCol = -1

        for (row in rowRange) {
            for (col in colRange) {
                if (input[row][col] == 'S') {
                    startRow = row
                    startCol = col
                } else if (input[row][col] == 'E') {
                    endRow = row
                    endCol = col
                }
            }
        }

        // find the lowest picoseconds to reach end
        val queue = LinkedList<Pair<Int, Int>>()
        val visited = mutableSetOf(endRow to endCol)
        queue.add(endRow to endCol)
        var picoseconds = 0

        val lowestPicosecondsMemo =
            Array(input.size) { IntArray(input[0].length) { input.size * input[0].length + 1 } }

        // find the lowest picoseconds taken from any position to end
        while (queue.isNotEmpty()) {
            var levelSize = queue.size
            while (levelSize-- > 0) {
                val (currRow, currCol) = queue.poll()!!

                lowestPicosecondsMemo[currRow][currCol] = picoseconds

                for ((rd, cd) in moves) {
                    val nbrRow = currRow + rd
                    val nbrCol = currCol + cd

                    if (nbrRow !in rowRange
                        || nbrCol !in colRange
                        || input[nbrRow][nbrCol] == '#'
                        || nbrRow to nbrCol in visited
                    )
                        continue

                    queue.add(nbrRow to nbrCol)
                    visited.add(nbrRow to nbrCol)
                }
            }

            picoseconds++
        }

        val picosecondsTakenWithoutCheat =
            lowestPicosecondsMemo[startRow][startCol]

        // find the best cheat that saves at least 100 picoseconds
        var bestCheats = 0

        val beforeCheatQueue = LinkedList<Pair<Int, Int>>()
        beforeCheatQueue.add(startRow to startCol)
        val beforeCheatVisited = mutableSetOf(startRow to startCol)
        var beforeCheatPicoseconds = 0

        /*
                fun cheat(cheatStartRow: Int, cheatStartCol: Int) {
                    // wall found and start cheating
        //            println("$cheatStartRow, $cheatStartCol")
                    val cheatQueue = LinkedList<Pair<Int, Int>>()
                    cheatQueue.add(cheatStartRow to cheatStartCol)
                    val cheatVisited = mutableSetOf(cheatStartRow to cheatStartCol)

                    var cheatPicoseconds = 0
                    while (cheatPicoseconds <= 20 && cheatQueue.isNotEmpty()) {
                        var clSize = cheatQueue.size
                        // cheat picoseconds level
                        while (clSize-- > 0) {
                            val (cheatRow, cheatCol) = cheatQueue.poll()!!

                            if (cheatPicoseconds != 0 && input[cheatRow][cheatCol] != '#') {
                                // out of wall
                                // stop the cheating
                                // calculate the picoseconds to reach end of race
                                val totalPicoseconds =
                                    beforeCheatPicoseconds + cheatPicoseconds + lowestPicosecondsMemo[cheatRow][cheatCol]

                                if (picosecondsTakenWithoutCheat - totalPicoseconds >= 100) {
        //                            print("$cheatRow, $cheatCol | ")
                                    bestCheats++ // increment best cheat count if this cheat saves 100 picoseconds
                                }
                            }

                            // add next steps
                            for ((crd, ccd) in moves) {
                                val cheatNbrRow = cheatRow + crd
                                val cheatNbrCol = cheatCol + ccd

                                if (cheatNbrRow !in rowRange
                                    || cheatNbrCol !in colRange
                                    || cheatNbrRow to cheatNbrCol in cheatVisited
                                )
                                    continue

                                cheatQueue.add(cheatNbrRow to cheatNbrCol)
                                cheatVisited.add(cheatNbrRow to cheatNbrCol)
                            }
                        }
                        // increment cheat picoseconds
                        cheatPicoseconds++
                    }
        //            println()
                }

         */

        fun cheat(cheatStartRow: Int, cheatStartCol: Int) {
            // wall found and start cheating

            for (cheatPicoseconds in 1..20) {
                for (rd in 0..cheatPicoseconds) {
                    val cd = cheatPicoseconds - rd
                    // quadrants
                    //    |
                    //  3 | 2
                    // ---0---
                    //  4 | 1
                    //    |

                    // first quadrant
                    var cheatRow = cheatStartRow + rd
                    var cheatCol = cheatStartCol + cd
                    if (cheatRow in rowRange && cheatCol in colRange && input[cheatRow][cheatCol] != '#') {
                        val totalPicoseconds =
                            beforeCheatPicoseconds + cheatPicoseconds + lowestPicosecondsMemo[cheatRow][cheatCol]

                        if (picosecondsTakenWithoutCheat - totalPicoseconds >= 100)
                            bestCheats++ // increment best cheat count if this cheat saves 100 picoseconds

                    }

                    // second quadrant
                    if (rd != 0) {
                        cheatRow = cheatStartRow - rd
                        cheatCol = cheatStartCol + cd
                        if (cheatRow in rowRange && cheatCol in colRange && input[cheatRow][cheatCol] != '#') {
                            val totalPicoseconds =
                                beforeCheatPicoseconds + cheatPicoseconds + lowestPicosecondsMemo[cheatRow][cheatCol]

                            if (picosecondsTakenWithoutCheat - totalPicoseconds >= 100)
                                bestCheats++ // increment best cheat count if this cheat saves 100 picoseconds

                        }
                    }
                    // third quadrant
                    if (rd != 0 && cd != 0) {
                        cheatRow = cheatStartRow - rd
                        cheatCol = cheatStartCol - cd
                        if (cheatRow in rowRange && cheatCol in colRange && input[cheatRow][cheatCol] != '#') {
                            val totalPicoseconds =
                                beforeCheatPicoseconds + cheatPicoseconds + lowestPicosecondsMemo[cheatRow][cheatCol]

                            if (picosecondsTakenWithoutCheat - totalPicoseconds >= 100)
                                bestCheats++ // increment best cheat count if this cheat saves 100 picoseconds

                        }
                    }
                    // forth quadrant
                    if (cd != 0) {
                        cheatRow = cheatStartRow + rd
                        cheatCol = cheatStartCol - cd
                        if (cheatRow in rowRange && cheatCol in colRange && input[cheatRow][cheatCol] != '#') {
                            val totalPicoseconds =
                                beforeCheatPicoseconds + cheatPicoseconds + lowestPicosecondsMemo[cheatRow][cheatCol]

                            if (picosecondsTakenWithoutCheat - totalPicoseconds >= 100)
                                bestCheats++ // increment best cheat count if this cheat saves 100 picoseconds

                        }
                    }
                }
            }
        }

        outer@ while (beforeCheatQueue.isNotEmpty()) {
            var bclSize = beforeCheatQueue.size
            // before cheat levels
            while (bclSize-- > 0) {
                val (currRow, currCol) = beforeCheatQueue.poll()!!

                if (input[currRow][currCol] == 'E')
                    break@outer

                // start cheating
                cheat(currRow, currCol)

                // add next steps
                for ((rd, cd) in moves) {
                    val nbrRow = currRow + rd
                    val nbrCol = currCol + cd

                    if (nbrRow !in rowRange
                        || nbrCol !in colRange
                        || input[nbrRow][nbrCol] == '#'
                        || nbrRow to nbrCol in beforeCheatVisited
                    )
                        continue

                    beforeCheatQueue.add(nbrRow to nbrCol)
                    beforeCheatVisited.add(nbrRow to nbrCol)
                }
            }

            // increment before cheat picoseconds
            beforeCheatPicoseconds++
        }

        return bestCheats
    }

    part1(readInput("Day20")).println() // this solution takes almost 20 seconds

//    val input = """
//        ###############
//        #...#...#.....#
//        #.#.#.#.#.###.#
//        #S#...#.#.#...#
//        #######.#.#.###
//        #######.#.#...#
//        #######.#.###.#
//        ###..E#...#...#
//        ###.#######.###
//        #...###...#...#
//        #.#####.#.###.#
//        #.#...#.#.#...#
//        #.#.#.#.#.#.###
//        #...#...#...###
//        ###############
//    """.trimIndent().lines()
//
//    part2(input).println()

    part2(readInput("Day20")).println()
}