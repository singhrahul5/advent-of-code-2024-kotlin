import kotlin.math.min

fun main() {

    fun String.parseInput(prefixSize: Int) =
        substring(prefixSize).split(",").map { it.trim().substring(2).toInt() }

    fun part1(input: List<String>): Int {

        fun solve(
            ax: Int,
            ay: Int,
            bx: Int,
            by: Int,
            prizeX: Int,
            prizeY: Int
        ): Int {

            var buttonBPushes = min(prizeX / bx, prizeY / by)
//            buttonBPushes.println()
            while (buttonBPushes >= 0) {
                val remainingXMoves = prizeX - buttonBPushes * bx
                val remainingYMoves = prizeY - buttonBPushes * by

//                println("$buttonBPushes ")
//                println("$remainingXMoves $remainingYMoves ")
//                println("${remainingXMoves % ax == 0} ${remainingYMoves % ay == 0} ${remainingXMoves / ax == remainingYMoves / ay}")
                if (
                    remainingXMoves >= 0 && remainingYMoves >= 0
                    && remainingXMoves % ax == 0 && remainingYMoves % ay == 0
                    && remainingXMoves / ax == remainingYMoves / ay
                ) {
                    val buttonAPushes = remainingXMoves / ax
                    return buttonAPushes * 3 + buttonBPushes
                }

                buttonBPushes--
            }

            return 0
        }

//        solve(94, 34, 22, 67, 8400, 5400).println()


        var ans = 0
        for (i in 0..<input.size step 4) {
            val (ax, ay) = input[i].parseInput(9)
            val (bx, by) = input[i + 1].parseInput(9)
            val (prizeX, prizeY) = input[i + 2].parseInput(6)

//            println("$ax $ay $bx, $by, $prizeX $prizeY")
            ans += solve(ax, ay, bx, by, prizeX, prizeY)
        }
        return ans
    }

    fun part2(input: List<String>): Long {
        val measurementError = 10_000_000_000_000L

        var ans = 0L
        for (i in 0..<input.size step 4) {
            val (ax, ay) = input[i].parseInput(9)
            val (bx, by) = input[i + 1].parseInput(9)
            val (prizeX, prizeY) = input[i + 2].parseInput(6).map { it + measurementError}

            // ax * A_button_pushes + bx * B_button_pushes = prizeX --- (1)
            // ay * A_button_pushes + by * B_button_pushes = prizeY --- (2)
            // Cramer’s Rule of solving two equation

            val d = ax * by - ay * bx

            val dA = prizeX * by - prizeY * bx
            val dB = ax * prizeY - ay * prizeX

            if (dA % d == 0L && dB % d == 0L)
                ans += dA / d * 3 + dB / d
        }
        return ans
    }

    var input = """
        Button A: X+94, Y+34
        Button B: X+22, Y+67
        Prize: X=8400, Y=5400

        Button A: X+26, Y+66
        Button B: X+67, Y+21
        Prize: X=12748, Y=12176

        Button A: X+17, Y+86
        Button B: X+84, Y+37
        Prize: X=7870, Y=6450

        Button A: X+69, Y+23
        Button B: X+27, Y+71
        Prize: X=18641, Y=10279
    """.trimIndent().lines()
    check(part1(input) == 480)
//    part2(input).println()

    part1(readInput("Day13")).println()
    part2(readInput("Day13")).println()
}