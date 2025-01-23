fun main() {
    fun part1(input: List<String>): Int {
        val network: MutableMap<String, MutableSet<String>> = mutableMapOf()

        input.forEach { connection ->
            val (first, second) = connection.split("-")

            if (first !in network)
                network[first] = mutableSetOf()
            if (second !in network)
                network[second] = mutableSetOf()

            network[first]?.add(second)
            network[second]?.add(first)
        }

//        network.map { it.value.size }.max().println()

        val visitedChiefHistorianComputers = mutableSetOf<String>()

        var multiplayerGameChiefHistorianPlayingCount = 0
        for (computer in network.keys) {

            if (computer[0] != 't')
                continue

            val connectedComputerList = (network[computer]!!).toList()

            for (fi in connectedComputerList.indices) {
                if (connectedComputerList[fi] in visitedChiefHistorianComputers)
                    continue

                for (si in (fi + 1)..<connectedComputerList.size) {
                    if ( connectedComputerList[si] in visitedChiefHistorianComputers)
                        continue

                    if (connectedComputerList[si] in (network[connectedComputerList[fi]]!!))
                        multiplayerGameChiefHistorianPlayingCount++
                }
            }

            visitedChiefHistorianComputers.add(computer)
        }

        return multiplayerGameChiefHistorianPlayingCount

    }

    fun part2(input: List<String>): String {
        val network: MutableMap<String, MutableSet<String>> = mutableMapOf()

        input.forEach { connection ->
            val (first, second) = connection.split("-")

            if (first !in network)
                network[first] = mutableSetOf()
            if (second !in network)
                network[second] = mutableSetOf()

            network[first]?.add(second)
            network[second]?.add(first)
        }

//        network.map { it.value.size }.max().println()
        var largestConnectedComputers = mutableListOf<String>()

        fun isClique(subnetworkNodes: List<String>): Boolean {
            for (fi in subnetworkNodes.indices) {
                for (si in (fi + 1)..<subnetworkNodes.size) {
                    if (subnetworkNodes[si] !in (network[subnetworkNodes[fi]]!!))
                        return false
                }
            }
            return true
        }

        fun findSubnetworkAndUpdateClique(index: Int, subnetworkNodes: MutableList<String>, networkNodes: List<String>) {
            if (index !in networkNodes.indices) {
                if (subnetworkNodes.size > largestConnectedComputers.size && isClique(subnetworkNodes))
                    largestConnectedComputers = ArrayList(subnetworkNodes)

//                largestConnectedComputers.println()
                return
            }

            // include current node
            subnetworkNodes.add(networkNodes[index])
            findSubnetworkAndUpdateClique(index+1, subnetworkNodes, networkNodes)
            subnetworkNodes.removeLast()

            //exclude current node
            findSubnetworkAndUpdateClique(index +1, subnetworkNodes, networkNodes)
        }

        for ((computer,connectedComps) in network) {

            if (connectedComps.size <= largestConnectedComputers.size)
                continue

            findSubnetworkAndUpdateClique(0, mutableListOf(computer), connectedComps.toList())
        }
        largestConnectedComputers.sort()
        return largestConnectedComputers.joinToString (separator = "," )
    }

    val input = """
        kh-tc
        qp-kh
        de-cg
        ka-co
        yn-aq
        qp-ub
        cg-tb
        vc-aq
        tb-ka
        wh-tc
        yn-cg
        kh-ub
        ta-co
        de-co
        tc-td
        tb-wq
        wh-td
        ta-ka
        td-qp
        aq-cg
        wq-ub
        ub-vc
        de-ta
        wq-aq
        wq-vc
        wh-yn
        ka-de
        kh-ta
        co-tc
        wh-qp
        tb-vc
        td-yn
    """.trimIndent().lines()

    check(part1(input) == 7)
    part1(readInput("Day23")).println()

    check(part2(input) == "co,de,ka,ta")
    part2(readInput("Day23")).println()
}