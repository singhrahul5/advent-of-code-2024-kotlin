fun main() {

    fun createAdjList(edges: List<String>): Map<Int, List<Int>> {
        val adj: MutableMap<Int, MutableList<Int>> = HashMap()

        for ((u, v) in edges.map { it.split("|").map { it.toInt() } }) {
            adj.computeIfAbsent(u) { mutableListOf() }.add(v)
        }
        return adj
    }

    fun isValidPageOrder(pages: List<Int>, adj: Map<Int, List<Int>>): Boolean {

        val visitedPage = mutableSetOf<Int>()
        for (page in pages) {
            for (pageAfterThis in adj[page] ?: emptyList()) {
                if (pageAfterThis in visitedPage)
                    return false
            }
            visitedPage.add(page)
        }

        return true
    }

    fun findTopologicalSort(pages: List<Int>, adj: Map<Int, List<Int>>): List<Int> {
        val pageSet = pages.toSet()
        val stack = mutableListOf<Int>()
        val visited = mutableSetOf<Int>()
        fun topo(currPage: Int) {
            visited.add(currPage)

            for (nextPage in adj[currPage] ?: emptyList()) {
                if (nextPage in pageSet && nextPage !in visited)
                    topo(nextPage)

            }
            stack.add(currPage)
        }

        for (page in pages) {
            if (page !in visited)
                topo(page)
        }

        stack.reverse()
        return stack
    }

    fun part1(input: List<String>): Int {
        input.subList(0, input.indexOf(""))
        val adj = createAdjList(input.subList(0, input.indexOf("")))

        return input.subList(input.indexOf("") + 1, input.size)
            .map { it.split(",").map { it.toInt() } }
            .filter { isValidPageOrder(it, adj) }
            .sumOf { it[it.size / 2] }
    }

    fun part2(input: List<String>): Int {
        input.subList(0, input.indexOf(""))
        val adj = createAdjList(input.subList(0, input.indexOf("")))

        return input.subList(input.indexOf("") + 1, input.size)
            .map { it.split(",").map { it.toInt() } }
            .filter { !isValidPageOrder(it, adj) }
            .map{findTopologicalSort(it, adj)}
            .sumOf { it[it.size / 2] }
    }

    var input = """
        47|53
        97|13
        97|61
        97|47
        75|29
        61|13
        75|53
        29|13
        97|29
        53|29
        61|53
        97|53
        61|29
        47|13
        75|47
        97|75
        47|61
        75|61
        47|29
        75|13
        53|13

        75,47,61,53,29
        97,61,53,29,13
        75,29,13
        75,97,47,61,53
        61,13,29
        97,13,75,29,47
    """.trimIndent().lines()
    check(part1(input) == 143)
//    part2(input).println()
    check(part2(input) == 123)

    part1(readInput("Day05")).println()
    part2(readInput("Day05")).println()
}