import java.math.BigInteger


/**
 * brute force technique
 */
class StoneList(input: List<String>) {
    private class Node(var number: String) {
        var next: Node? = null
    }

    private var head: Node = Node("")
    var stoneCount: Long = 0L
        private set

    init {
        var temp = head
        for (number in input) {
            temp.next = Node(number)
            temp = temp.next ?: throw IllegalStateException()
            stoneCount++
        }
    }


    private fun String.removeLeadingZeros(): String {
        var i = 0
        while (i < length) {
            if (this[i] != '0')
                break

            i++
        }
        return if (i == length) "0" else substring(i)
    }

    private fun splitStone(prev: Node): Pair<Node, Node?> {
        val curr = prev.next ?: throw IllegalStateException()
        val number = curr.number
        val firstNumber =
            number.substring(0, number.length / 2).removeLeadingZeros()
        val secondNumber =
            number.substring(number.length / 2).removeLeadingZeros()

        val firstNode = Node(firstNumber)
        val secondNode = Node(secondNumber)

        prev.next = firstNode
        firstNode.next = secondNode
        secondNode.next = curr.next

        stoneCount++ // one stone increases

        return secondNode to secondNode.next
    }

    fun blinks(time: Int) {
        repeat(time) {
            blink()

//            var temp: Node? = head.next
//            val st = mutableListOf<String>()
//            while (temp != null) {
//                st.add(temp.number)
//                temp = temp.next
//            }
//
//            println("stones ${st.size} and unique stones ${HashSet(st).size}")

        }
    }

    private fun blink() {
        var prev: Node = head
        var curr: Node? = head.next

        while (curr != null) {
            if (curr.number == "0") {
                curr.number = "1"
                prev = curr
                curr = curr.next
            } else if (curr.number.length % 2 == 0) {
                splitStone(prev).also {
                    prev = it.first
                    curr = it.second
                }
            } else {
                val newNumber = BigInteger(curr.number) * BigInteger("2024")
                curr.number = newNumber.toString()
                prev = curr
                curr = curr.next
            }
        }
    }
}



fun main() {
    fun String.removeLeadingZeros(): String {
        var i = 0
        while (i < length) {
            if (this[i] != '0')
                break

            i++
        }
        return if (i == length) "0" else substring(i)
    }

    /**
     * solved using dp memorization technique
     */
    fun blink(
        stone: String,
        times: Int,
        memo: MutableMap<Pair<String, Int>, Long>
    ): Long {
        if (times == 0)
            return 1L

        if ((stone to times) !in memo) {
            memo[(stone to times)] = if (stone == "0") {
                blink("1", times - 1, memo)
            } else if (stone.length % 2 == 0) {
                val firstStone =
                    stone.substring(0, stone.length / 2).removeLeadingZeros()
                val secondStone =
                    stone.substring(stone.length / 2).removeLeadingZeros()
                blink(firstStone, times - 1, memo) +
                        blink(secondStone, times - 1, memo)
            } else {
                val newNumber = BigInteger(stone) * BigInteger("2024")
                blink(newNumber.toString(), times - 1, memo)
            }
        }

        return memo[(stone to times)]!!
    }

    fun part1(input: List<String>): Long {
        var ans = 0L
        val stones = input[0].split(" ")

        val memo: MutableMap<Pair<String, Int>, Long> = mutableMapOf()
        for (stone in stones) {
            ans += blink(stone, 25, memo)
        }
        return ans
    }

    fun part2(input: List<String>): Long {
        var ans = 0L
        val stones = input[0].split(" ")

        val memo: MutableMap<Pair<String, Int>, Long> = mutableMapOf()
        for (stone in stones) {
            ans += blink(stone, 75, memo)
        }
        return ans
    }

    var input: List<String> = "125 17".lines()
    check(part1(input) == 55312L)

    with (StoneList(input[0].split(" "))) {
        blinks(25)
        check(stoneCount == 55312L)
    }

//    input = "0".lines()
//    part1(input).println()
//    part2(input).println()

    part1(readInput("Day11")).println()
    part2(readInput("Day11")).println()
}