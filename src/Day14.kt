import java.lang.Math.floorMod

fun main() {
    fun part1(input: List<String>): Int {
        val width = 101
        val height = 103

        var quad1 = 0
        var quad2 = 0
        var quad3 = 0
        var quad4 = 0

        for (robot in input) {
            val temp = robot.split(" ")
                .map {
                    it.substring(2)
                        .split(",")
                        .map { it.toInt() }
                }
//            temp.println()
            var (px, py) = temp[0]
            val (vx, vy) = temp[1]

//            repeat(100) {
//                px = floorMod(px + vx, wide)
//                py = floorMod(py + vy, tall)
//            }

            px = floorMod(px + vx * 100, width)
            py = floorMod(py + vy * 100, height)


//            println("$px $py")

            if (px < width / 2 && py < height / 2) {
                quad1 ++
            } else if (px < width / 2 && py > height / 2) {
                quad2 ++
            } else if (px > width / 2 && py < height / 2) {
                quad3 ++
            } else if (px > width / 2 && py > height / 2) {
                quad4 ++
            }
        }
        return quad1 * quad2 * quad3 * quad4
    }


    fun part2(input: List<String>): Int {
        val width = 101
        val height = 103
        val positions = mutableListOf<MutableList<Int>>()
        val velocity = mutableListOf<List<Int>>()

        for (robot in input) {
            val temp = robot.split(" ")
                .map {
                    it.substring(2)
                        .split(",")
                        .map { it.toInt() }
                }
            positions.add( temp[0].toMutableList())
            velocity.add(temp[1])
        }



        var safetyFactor = Int.MAX_VALUE
        var minSeconds = 0
        for (i in 1..(width * height) ) {
            var quad1 = 0
            var quad2 = 0
            var quad3 = 0
            var quad4 = 0
            for (index in 0..<positions.size) {
                val px = floorMod(positions[index][0] + velocity[index][0], width)
                val py = floorMod(positions[index][1] + velocity[index][1], height)
                positions[index][0] = px
                positions[index][1] = py
                if (px < width / 2 && py < height / 2) {
                    quad1 ++
                } else if (px < width / 2 && py > height / 2) {
                    quad2 ++
                } else if (px > width / 2 && py < height / 2) {
                    quad3 ++
                } else if (px > width / 2 && py > height / 2) {
                    quad4 ++
                }
            }

            if (safetyFactor > quad1 * quad2 * quad3 * quad4) {
                safetyFactor = quad1 * quad2 * quad3 * quad4
                minSeconds = i
            }

        }
        return minSeconds
    }

//    var input = """
//        p=0,4 v=3,-3
//        p=6,3 v=-1,-3
//        p=10,3 v=-1,2
//        p=2,0 v=2,-1
//        p=0,0 v=1,3
//        p=3,0 v=-2,-2
//        p=7,6 v=-1,-3
//        p=3,0 v=-1,-2
//        p=9,3 v=2,3
//        p=7,3 v=-1,2
//        p=2,4 v=2,-3
//        p=9,5 v=-3,-3
//    """.trimIndent().lines()

//    check(part1(input) == 12)

    part1(readInput("Day14")).println()
    part2(readInput("Day14")).println()
}