fun main() {
    fun Char.getMove(): Pair<Int, Int> =
        if (this == '^')
            -1 to 0
        else if (this == 'v')
            1 to 0
        else if (this == '<')
            0 to -1
        else
            0 to 1


    fun part1(input: List<String>): Int {
        val warehouse = input.subList(0, input.indexOf(""))
            .map { it.toCharArray() }

        val moves =
            input.subList(input.indexOf("") + 1, input.size).joinToString("")

        // find robot position
        var robotRow = -1
        var robotCol = -1
        outer@ for (row in 0..<warehouse.size) {
            for (col in 0..<warehouse[row].size) {
                if (warehouse[row][col] == '@') {
                    robotRow = row
                    robotCol = col
                    break@outer
                }
            }
        }

//        println("$robotRow $robotCol")

        for (move in moves) {
//            move.println()
            val (rowMove, colMove) = move.getMove()

            var tmpRow = robotRow + rowMove
            var tmpCol = robotCol + colMove
            // find first wall or empty space
            while (warehouse[tmpRow][tmpCol] != '#' && warehouse[tmpRow][tmpCol] != '.') {
                tmpRow += rowMove
                tmpCol += colMove
            }

//            println("$tmpRow $tmpCol")


            if (warehouse[tmpRow][tmpCol] == '.') { // free space found
                val revRowMove = rowMove * -1
                val revColMove = colMove * -1
//                println("moving box")
                while (tmpRow != robotRow || tmpCol != robotCol) {
                    val nextRow = tmpRow + revRowMove
                    val nextCol = tmpCol + revColMove
//                    println("swap ($nextRow $nextCol) to ($tmpRow $tmpCol)")

                    // move boxes in corresponding direction
                    warehouse[tmpRow][tmpCol] = warehouse[nextRow][nextCol]
//                    warehouse[nextRow][nextCol] = '.'

                    tmpRow = nextRow
                    tmpCol = nextCol
                }

                warehouse[robotRow][robotCol] = '.'

                // robot new position
                robotRow += rowMove
                robotCol += colMove
            }

//            warehouse.forEach { String(it).println() }
        }

//        warehouse.forEach { String(it).println() }
        // to find gps coordinate sum
        var gpsCoordinateSum = 0
        for (row in 0..<warehouse.size) {
            for (col in 0..<warehouse[row].size) {
                if (warehouse[row][col] == 'O') {
                    gpsCoordinateSum += row * 100 + col
                }
            }
        }

        return gpsCoordinateSum
    }

    fun part2(input: List<String>): Int {
        val warehouse = input.subList(0, input.indexOf(""))
            .map {
                val row = CharArray(it.length * 2)
                for (i in 0..<it.length) {
                    if (it[i] == 'O') {
                        row[i * 2] = '['
                        row[i * 2 + 1] = ']'
                    } else if (it[i] == '@') {
                        row[i * 2] = '@'
                        row[i * 2 + 1] = '.'
                    } else {
                        row[i * 2] = it[i]
                        row[i * 2 + 1] = it[i]
                    }
                }
                row
            }



        val moves = input.subList(input.indexOf("") + 1, input.size).joinToString("")

        // find robot position
        var robotRow = -1
        var robotCol = -1
        outer@for (row in 0..<warehouse.size) {
            for (col in 0..<warehouse[row].size) {
                if (warehouse[row][col] == '@') {
                    robotRow = row
                    robotCol = col
                    break@outer
                }
            }
        }

//        println("$robotRow $robotCol")

        for (move in moves) {
//            move.println()
            val (rowMove, colMove) = move.getMove()


            if(move in charArrayOf('<', '>')) { // Horizontal movement
                val boxIndices = mutableListOf<Pair<Int, Int>>()
                var tmpRow = robotRow
                var tmpCol = robotCol

                while(warehouse[tmpRow][tmpCol] != '#' && warehouse[tmpRow][tmpCol] != '.') {
                    boxIndices.add(tmpRow to tmpCol)
                    tmpRow += rowMove
                    tmpCol += colMove
                }

                if (warehouse[tmpRow][tmpCol] == '#')
                    continue

                boxIndices.reverse()
                for ((row, col) in boxIndices)
                    warehouse[row + rowMove][col + colMove] = warehouse[row][col]

                warehouse[robotRow][robotCol] = '.'

                robotRow += rowMove
                robotCol += colMove

            } else { // Vertical movement
                val boxIndices = mutableListOf<Pair<Int, Int>>()
                var tmpRow = robotRow + rowMove
                var tmpCol = robotCol + colMove

                if (warehouse[tmpRow][tmpCol] == '#')
                    continue
                else if (warehouse[tmpRow][tmpCol] == '.') {
                    warehouse[tmpRow][tmpCol] = warehouse[robotRow][robotCol]
                    warehouse[robotRow][robotCol] = '.'
                    robotRow = tmpRow
                    robotCol = tmpCol
                    continue
                }

                // handle push box case
                if (warehouse[tmpRow][tmpCol] == ']') {
                    boxIndices.add(tmpRow to (tmpCol-1))
                } else {
                    boxIndices.add(tmpRow to tmpCol)
                }

                var movementSucceed = true

                var index = 0
                while(index < boxIndices.size) {
                    var (currBoxRow, currBoxCol) = boxIndices[index]
                    tmpRow = currBoxRow + rowMove
                    tmpCol = currBoxCol + colMove

                    if (warehouse[tmpRow][tmpCol] == '#') {
                        movementSucceed = false
                        break
                    } else if (warehouse[tmpRow][tmpCol] == '[') {
                        boxIndices.add(tmpRow to tmpCol)
                    } else if (warehouse[tmpRow][tmpCol] == ']' && boxIndices.last() != (tmpRow to (tmpCol-1))) {
                        boxIndices.add(tmpRow to (tmpCol-1))
                    }

                    tmpCol += 1

                    if (warehouse[tmpRow][tmpCol] == '#') {
                        movementSucceed = false
                        break
                    } else if (warehouse[tmpRow][tmpCol] == '[') {
                        boxIndices.add(tmpRow to tmpCol)
                    } else if (warehouse[tmpRow][tmpCol] == ']' && boxIndices.last() != (tmpRow to (tmpCol-1))) {
                        boxIndices.add(tmpRow to (tmpCol-1))
                    }

                    index ++
                }

                if (!movementSucceed)
                    continue

                boxIndices.reverse()
                for ((row, col) in boxIndices) {
                    warehouse[row + rowMove][col + colMove] = warehouse[row][col]
                    warehouse[row + rowMove][col + colMove+1] = warehouse[row][col+1]
                    warehouse[row][col] = '.'
                    warehouse[row][col+1] = '.'
                }
                warehouse[robotRow+rowMove][robotCol+colMove] = warehouse[robotRow][robotCol]
                warehouse[robotRow][robotCol] = '.'

                robotRow += rowMove
                robotCol += colMove
            }


//            warehouse.forEach { String(it).println() }
        }

//        warehouse.forEach { String(it).println() }
        // to find gps coordinate sum
        var gpsCoordinateSum = 0
        for (row in 0..<warehouse.size) {
            for (col in 0..<warehouse[row].size) {
                if (warehouse[row][col] == '[') {
                    gpsCoordinateSum += row * 100 + col
                }
            }
        }

        return gpsCoordinateSum
    }

    var input = """
        ########
        #..O.O.#
        ##@.O..#
        #...O..#
        #.#.O..#
        #...O..#
        #......#
        ########

        <^^>>>vv<v>>v<<
    """.trimIndent().lines()

    check(part1(input) == 2028)

    input = """
        ##########
        #..O..O.O#
        #......O.#
        #.OO..O.O#
        #..O@..O.#
        #O#..O...#
        #O..O..O.#
        #.OO.O.OO#
        #....O...#
        ##########

        <vv>^<v^>v>^vv^v>v<>v^v<v<^vv<<<^><<><>>v<vvv<>^v^>^<<<><<v<<<v^vv^v>^
        vvv<<^>^v^^><<>>><>^<<><^vv^^<>vvv<>><^^v>^>vv<>v<<<<v<^v>^<^^>>>^<v<v
        ><>vv>v^v^<>><>>>><^^>vv>v<^^^>>v^v^<^^>v^^>v^<^v>v<>>v^v^<v>v^^<^^vv<
        <<v<^>>^^^^>>>v^<>vvv^><v<<<>^^^vv^<vvv>^>v<^^^^v<>^>vvvv><>>v^<<^^^^^
        ^><^><>>><>^^<<^^v>>><^<v>^<vv>>v>>>^v><>^v><<<<v>>v<v<v>vvv>^<><<>^><
        ^>><>^v<><^vvv<^^<><v<<<<<><^v<<<><<<^^<v<^^^><^>>^<v^><<<^>>^v<v^v<v^
        >^>>^v>vv>^<<^v<>><<><<v<<v><>v<^vv<<<>^^v^>^^>>><<^v>>v^v><^^>>^<>vv^
        <><^^>^^^<><vvvvv^v<v<<>^v<v>v<<^><<><<><<<^^<<<^<<>><<><^^^>^^<>^>v<>
        ^^>vv<^v^v<vv>^<><v<^v>^^^>>>^^vvv^>vvv<>>>^<^>>>>>^<<^v>^vvv<>^<><<v>
        v^^>>><<^^<>>^v^<v^vv<>v^<<>^<^v^v><^<<<><<^<v><v<>vv>>v><v^<vv<>v^<<^
    """.trimIndent().lines()

    check(part1(input) == 10092)
    check(part2(input) == 9021)

    part1(readInput("Day15")).println()
    part2(readInput("Day15")).println()
}