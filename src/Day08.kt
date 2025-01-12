fun main() {
    fun part1(input: List<String>): Int {
        val frequencyMap = mutableMapOf<Char, MutableList<Pair<Int, Int>>>()


        for (row in input.indices) {
            for (col in input[row].indices) {
                if (input[row][col] == '.') continue

                frequencyMap.putIfAbsent(input[row][col], mutableListOf())

                frequencyMap[input[row][col]]?.add(row to col)
            }
        }

        val antinodes = mutableSetOf<Pair<Int, Int>>()

        for ((_, sameFreqList) in frequencyMap) {

            for (firstFreq in sameFreqList) {
                for (secondFreq in sameFreqList) {
                    if (firstFreq == secondFreq) continue

                    val antinodeRow = secondFreq.first * 2 - firstFreq.first
                    val antinodeCol = secondFreq.second * 2 - firstFreq.second

                    if (antinodeRow in input.indices && antinodeCol in input[0].indices)
                        antinodes.add(antinodeRow to antinodeCol)

                }
            }
        }

        return antinodes.size
    }


    fun part2(input: List<String>): Int {
        val frequencyMap = mutableMapOf<Char, MutableList<Pair<Int, Int>>>()

        for (row in input.indices) {
            for (col in input[row].indices) {
                if (input[row][col] == '.') continue

                frequencyMap.putIfAbsent(input[row][col], mutableListOf())
                frequencyMap[input[row][col]]?.add(row to col)
            }
        }

        val antinodes = mutableSetOf<Pair<Int, Int>>()

        for ((_, sameFreqList) in frequencyMap) {

            for (firstFreq in sameFreqList) {
                for (secondFreq in sameFreqList) {
                    if (firstFreq == secondFreq) continue

                    val rowDisplacement = secondFreq.first - firstFreq.first
                    val colDisplacement = secondFreq.second - firstFreq.second

                    var antinodeRow = secondFreq.first
                    var antinodeCol = secondFreq.second
                    while (antinodeRow in input.indices && antinodeCol in input[0].indices) {
                        antinodes.add(antinodeRow to antinodeCol)
                        antinodeRow += rowDisplacement
                        antinodeCol += colDisplacement
                    }
                }
            }
        }

        return antinodes.size
    }

    val input = """
        ............
        ........0...
        .....0......
        .......0....
        ....0.......
        ......A.....
        ............
        ............
        ........A...
        .........A..
        ............
        ............
    """.trimIndent().lines()

    check(part1(input) == 14)
    check(part2(input) == 34)

    part1(readInput("Day08")).println()
    part2(readInput("Day08")).println()
}