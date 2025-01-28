fun main() {
    fun part1(input: List<String>): Int {
        val lockSchematics = mutableListOf<IntArray>()
        val keySchematics = mutableListOf<IntArray>()
        for (schematic in input.indices step 8) {
            val pinHeights = IntArray(input[schematic].length){-1}

            for (row in 0..6) {
                for (col in input[schematic + row].indices) {
                    if (input[schematic + row][col] == '#')
                        pinHeights[col] ++
                }
            }

            if (input[schematic][0] == '#') {
                lockSchematics.add(pinHeights)
            } else {
                keySchematics.add(pinHeights)
            }
        }

        var validKeyLockPairCount = 0
        for (lock in lockSchematics) {
            middle@ for (key in keySchematics) {
                for (i in lock.indices) {
                    if (lock[i] + key[i] > 5) {
//                        println("${key.contentToString()} & ${lock.contentToString()}")
                        continue@middle
                    }
                }
                validKeyLockPairCount ++
            }
        }

        return validKeyLockPairCount
    }

    val input = """
        #####
        .####
        .####
        .####
        .#.#.
        .#...
        .....

        #####
        ##.##
        .#.##
        ...##
        ...#.
        ...#.
        .....

        .....
        #....
        #....
        #...#
        #.#.#
        #.###
        #####

        .....
        .....
        #.#..
        ###..
        ###.#
        ###.#
        #####

        .....
        .....
        .....
        #....
        #.#..
        #.#.#
        #####
    """.trimIndent().lines()

    check(part1(input) == 3)
    part1(readInput("Day25")).println()
}