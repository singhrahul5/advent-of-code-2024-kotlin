import kotlin.math.max

fun main() {

    fun part1(input: List<String>): Long {
        val arr: MutableList<Int> = input[0].split("")
            .filter { it.isNotEmpty() }
            .map(Integer::valueOf).toMutableList()

        val diskMap: MutableList<Int> = mutableListOf<Int>()

        var id = 0

        for (i in 0..<arr.size) {

            repeat(arr[i]) {
                diskMap.add(if (i % 2 == 0) id else -1)
            }

            if (i % 2 == 0 && arr[i] != 0)
                id++
        }

        var left = 0
        var right = diskMap.lastIndex

        while (left < right) {
            if (diskMap[left] != -1) {
                left++
                continue
            }

            while (right > left && diskMap[right] == -1)
                right--

            if (right > left) {
                diskMap[left] = diskMap[right]
                diskMap[right] = -1
            }
        }
//        diskMap.println()
        var ans: Long = 0L
        for (i in 0..<diskMap.size) {
            ans += i * max(0, diskMap[i])
        }
        return ans
    }

    fun part2(input: List<String>): Long {
        val arr: MutableList<Int> = input[0].split("")
            .filter { it.isNotEmpty() }
            .map(Integer::valueOf).toMutableList()

        val diskMap: MutableList<Int> = mutableListOf<Int>()

        var id = 0

        val freeSpaces: MutableList<Pair<Int, Int>> = mutableListOf()
        val files: MutableList<Pair<Int, Int>> = mutableListOf()

        for (i in 0..<arr.size) {
            if (i % 2 == 0) {
                files.add(diskMap.size to arr[i])
                repeat(arr[i]) {
                    diskMap.add(id)
                }

                id++
            } else {
                freeSpaces.add(diskMap.size to arr[i])
                repeat(arr[i]) {
                    diskMap.add(-1)
                }
            }

        }

        files.reverse()

//        diskMap.println()
//        files.println()
//        freeSpaces.println()
        for (file in files) {
            var (fileStartIndex, fileBlocks) = file
            val fileID = diskMap[fileStartIndex]
            // find first greater or equal size free block
            var freeBlockIndex = 0
            while (freeBlockIndex < freeSpaces.size && freeSpaces[freeBlockIndex].second < fileBlocks) {
                freeBlockIndex++
            }

            if (freeBlockIndex < freeSpaces.size && freeSpaces[freeBlockIndex].first < fileStartIndex) {
                var (freeSpaceStartIndex, freeSpaceBlocks) = freeSpaces[freeBlockIndex]

                repeat(fileBlocks) {
                    diskMap[freeSpaceStartIndex++] = fileID
                    diskMap[fileStartIndex++] = -1
                }

                freeSpaces[freeBlockIndex] =
                    freeSpaceStartIndex to (freeSpaceBlocks - fileBlocks)
            }
        }


//        diskMap.println()
        var ans: Long = 0L
        for (i in 0..<diskMap.size) {
            ans += i * max(0, diskMap[i])
        }
        return ans
    }

    check(part1(listOf("12345")) == 60L)


    var input = listOf("2333133121414131402")
    check(part1(input) == 1928L)
    check(part2(input) == 2858L)


    input = readInput("Day09")
    part1(input).println()
    part2(input).println()

}

