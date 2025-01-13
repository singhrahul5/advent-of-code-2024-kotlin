fun main() {

    class TrieNode(
        val children: Array<TrieNode?> = arrayOfNulls(26),
        var terminal: Boolean = false
    )

    fun part1(input: List<String>): Int {

        // available towel patterns
        val availableTowelsPatterns = input[0].split(", ")
        // list of designs by Official Onsen Branding Expert
        val designs = input.subList(2, input.size)

        // trie for available towel patterns
        val rootNode = TrieNode()

        // adding every stripe into trie
        availableTowelsPatterns.forEach { stripes ->
            var head = rootNode

            for (i in stripes.indices) {
                val index = stripes[i] - 'a'
                if (head.children[index] == null)
                    head.children[index] = TrieNode()
                head = head.children[index]!!
                if (i == stripes.lastIndex)
                    head.terminal = true

            }
        }

        fun isPossibleDesign(index: Int, design: String, dp: Array<Boolean?>): Boolean {
            if (index == design.length)
                return true

            if (dp[index] != null)
                return dp[index]!!

            var head = rootNode

            var result = false
            for (i in index..<design.length) {
                val stripeIndex = design[i] - 'a'
                if (head.children[stripeIndex] == null)
                    break
                head = head.children[stripeIndex]!!

                if (head.terminal && isPossibleDesign(i+1, design, dp)) {
                    result = true
                    break
                }
            }
            dp[index] = result
            return result
        }

        var possibleDesignCount = 0
        for (design in designs) {
            if (isPossibleDesign(0, design, arrayOfNulls(design.length)))
                possibleDesignCount ++
        }
        return possibleDesignCount
    }

    fun part2(input: List<String>): Long {

        // available towel patterns
        val availableTowelsPatterns = input[0].split(", ")
        // list of designs by Official Onsen Branding Expert
        val designs = input.subList(2, input.size)

        // trie for available towel patterns
        val rootNode = TrieNode()

        // adding every stripe into trie
        availableTowelsPatterns.forEach { stripes ->
            var head = rootNode

            for (i in stripes.indices) {
                val index = stripes[i] - 'a'
                if (head.children[index] == null)
                    head.children[index] = TrieNode()
                head = head.children[index]!!
                if (i == stripes.lastIndex)
                    head.terminal = true

            }
        }

        fun getPossibleDesignCount(index: Int, design: String, dp: Array<Long?>): Long {
            if (index == design.length)
                return 1

            if (dp[index] != null)
                return dp[index]!!

            var head = rootNode

            var result = 0L
            for (i in index..<design.length) {
                val stripeIndex = design[i] - 'a'
                if (head.children[stripeIndex] == null)
                    break
                head = head.children[stripeIndex]!!

                if (head.terminal)
                    result += getPossibleDesignCount(i+1, design, dp)

            }
            dp[index] = result
            return result
        }

        var possibleDesignCount = 0L
        for (design in designs) {
            possibleDesignCount += getPossibleDesignCount(0, design, arrayOfNulls(design.length))
        }
        return possibleDesignCount
    }

    val input = """
        r, wr, b, g, bwu, rb, gb, br

        brwrr
        bggr
        gbbr
        rrbgbr
        ubwu
        bwurrg
        brgr
        bbrgwb
    """.trimIndent().lines()

    check(part1(input) == 6)
    check(part2(input) == 16L)

    part1(readInput("Day19")).println()
    part2(readInput("Day19")).println()
}