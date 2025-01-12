import java.util.*

fun main() {
    val moves = arrayOf(-1 to 0, 1 to 0, 0 to -1, 0 to 1)

    fun part1(input: List<String>): Int {

        val corruptedMemoryAddressList = input.map { addressStr ->
            val (x, y) = addressStr.split(",").map { it.toInt() }
            x to y
        }

        val corruptedMemoryAddressSet =
            corruptedMemoryAddressList.subList(0, 1024).toSet()

        var minSteps = 0

        val queue = LinkedList<Pair<Int, Int>>()
        queue.add(0 to 0)

        val visited = mutableSetOf(0 to 0)
        while (queue.isNotEmpty()) {
            var levelSize = queue.size
            while (levelSize-- > 0) {
                val (x, y) = queue.poll()

                if (x == 70 && y == 70) return minSteps

                for ((rd, cd) in moves) {
                    val nbr = (x + rd) to (y + cd)
                    if (nbr.first !in 0..70 || nbr.second !in 0..70
                        || nbr in visited || nbr in corruptedMemoryAddressSet
                    )
                        continue

                    visited.add(nbr)
                    queue.add(nbr)
                }
            }

            minSteps++
        }

        throw IllegalStateException("There is no way to reach exit.")
    }

    fun part2(input: List<String>): String {

        val corruptedMemoryAddressList = input.map { addressStr ->
            val (x, y) = addressStr.split(",").map { it.toInt() }
            x to y
        }

        val corruptedMemoryAddressSet = mutableSetOf<Pair<Int, Int>>()

        fun isExitPathBlocked(): Boolean {
            val queue = LinkedList<Pair<Int, Int>>()
            queue.add(0 to 0)

            val visited = mutableSetOf(0 to 0)
            while (queue.isNotEmpty()) {

                val (x, y) = queue.poll()

                if (x == 70 && y == 70) return false

                for ((rd, cd) in moves) {
                    val nbr = (x + rd) to (y + cd)
                    if (nbr.first !in 0..70 || nbr.second !in 0..70
                        || nbr in visited || nbr in corruptedMemoryAddressSet
                    )
                        continue

                    visited.add(nbr)
                    queue.add(nbr)
                }

            }

            return true
        }


        for (corruptedMemory in corruptedMemoryAddressList) {
            corruptedMemoryAddressSet.add(corruptedMemory)

            if (isExitPathBlocked())
                return "${corruptedMemory.first},${corruptedMemory.second}"
        }

        throw IllegalStateException("No corrupted memory can block exit path")
    }


    part1(readInput("Day18")).println()
    part2(readInput("Day18")).println()
}