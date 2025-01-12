fun main() {
    fun part1(input: List<String>): Int {
        val n = input.size
        val m = input[0].length
        // find guard
        var guardRow: Int = -1
        var guardCol: Int = -1
        outer@ for (row in input.indices) {
            for (col in input[row].indices) {
                if (input[row][col] == '^') {
                    guardRow = row
                    guardCol = col
                    break@outer
                }
            }
        }

        var rd = -1
        var cd = 0

        val guardVisitedArea = mutableSetOf<Pair<Int, Int>>()


        while (guardRow in 0..<n && guardCol in 0..<m) {
            guardVisitedArea.add(guardRow to guardCol)

            if (
                (guardRow + rd) in 0..<n
                && (guardCol + cd) in 0..<m
                && input[guardRow + rd][guardCol + cd] == '#'
            ) {

                val trd = rd
                rd = cd
                cd = -trd

            }
            guardRow += rd
            guardCol += cd
        }

        return guardVisitedArea.size
    }

    fun part2(input: List<String>): Int {
        data class VisCell(
            val row: Int,
            val col: Int,
            val rd: Int,
            val cd: Int
        )

        val n = input.size
        val m = input[0].length

        val lab = input.map(String::toCharArray)

        // find guard position
        var guardRow: Int = -1
        var guardCol: Int = -1
        outer@ for (row in lab.indices) {
            for (col in lab[row].indices) {
                if (lab[row][col] == '^') {
                    guardRow = row
                    guardCol = col
                    break@outer
                }
            }
        }

        fun loops(): Boolean {
            var row = guardRow
            var col = guardCol
            var rd = -1
            var cd = 0

            val guardVisited = mutableSetOf<VisCell>()
            while(true) {
                guardVisited.add(VisCell(row, col , rd, cd))
                if ((row + rd) !in 0..<n || (col + cd) !in 0..<m)  return false

                if (lab[row+rd][col + cd] == '#') {
                    val tempRd = rd
                    rd = cd
                    cd = -tempRd
                } else {
                    row += rd
                    col += cd
                }

                if (VisCell(row, col, rd, cd) in guardVisited) return true

            }
        }

        var obstructions = 0
        for (row in 0..<n) {
            for (col in 0..<m) {
                if (lab[row][col] != '.') continue

                lab[row][col] = '#'
                if (loops()) obstructions++
                lab[row][col] = '.'
            }
        }
        return obstructions
    }



    /*
    // Approach: traversing the lab and putting the obstacle in next cell
    // and after putting obstacle it checks the next steps either it create a loop or go out of the lab
    // Not working: because there is situation that the obstacle this algo put in next cell
    // it can prevent the path to choose current path in past steps
    // for example
    //  .##...
    //  ....#.
    //  ......
    //  ...#..
    //  .^....
    // if we put the obstacle [2,1] after reaching the [2,2] cell
    // it prevents the past steps that was taken to reach the current cell

    fun part2(input: List<String>): Int {

        data class VisCell(
            val row: Int,
            val col: Int,
            val rd: Int,
            val cd: Int
        )

        val n = input.size
        val m = input[0].length

        val lab = input.map(String::toCharArray)

        // find guard position
        var guardRow: Int = -1
        var guardCol: Int = -1
        outer@ for (row in lab.indices) {
            for (col in lab[row].indices) {
                if (lab[row][col] == '^') {
                    guardRow = row
                    guardCol = col
                    break@outer
                }
            }
        }

        var rd = -1
        var cd = 0

        val guardVisitedArea = mutableSetOf<VisCell>()

        val obstructionsSet = mutableSetOf<Pair<Int, Int>>()

        while (true) {
            guardVisitedArea.add(VisCell(guardRow, guardCol, rd, cd))

            if ((guardRow + rd) !in 0..<n || (guardCol + cd) !in 0..<m) break

            if (lab[guardRow + rd][guardCol + cd] == '#') { // obstacle found turn 90 degree
                val trd = rd
                rd = cd
                cd = -trd
                continue
            }

            if (lab[guardRow + rd][guardCol + cd] != '^') {
                val tempGuardVisArea = mutableSetOf<VisCell>()
                // put obstacle in next cell
                val tempCellContent = lab[guardRow + rd][guardCol + cd]
                lab[guardRow + rd][guardCol + cd] = '#'

                var tempGuardRow = guardRow
                var tempGuardCol = guardCol
                // turn 90 degree because of obstacle in the next cell
                var tempRd = cd
                var tempCd = -rd

                while (true) {
                    val tempCell =
                        VisCell(tempGuardRow, tempGuardCol, tempRd, tempCd)
                    if (tempCell in guardVisitedArea || tempCell in tempGuardVisArea) { // loop found
                        obstructionsSet.add((guardRow + rd) to (guardCol + cd))
                        break
                    }
                    tempGuardVisArea.add(tempCell)

                    if ((tempGuardRow + tempRd) !in 0..<n || (tempGuardCol + tempCd) !in 0..<m) // no loop found
                        break

                    if (lab[tempGuardRow + tempRd][tempGuardCol + tempCd] == '#') {
                        // obstacle found turn 90 degree
                        val trd = tempRd
                        tempRd = tempCd
                        tempCd = -trd

                        continue
                    }

                    tempGuardRow += tempRd
                    tempGuardCol += tempCd
                }

                // remove obstacle from next cell
                lab[guardRow + rd][guardCol + cd] = tempCellContent

                check(input == lab.map { it.concatToString() })
            }
            guardRow += rd
            guardCol += cd
        }
//        obstructionsSet.println()
        return obstructionsSet.size
    }

     */


    val input = """
        ....#.....
        .........#
        ..........
        ..#.......
        .......#..
        ..........
        .#..^.....
        ........#.
        #.........
        ......#...
    """.trimIndent().lines()

    check(part1(input) == 41)
    check(part2(input) == 6)

    part1(readInput("Day06")).println()
    part2(readInput("Day06")).println()
}