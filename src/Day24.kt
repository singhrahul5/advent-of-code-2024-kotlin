import java.util.*

enum class LogicGate {
    AND, OR, XOR
}

data class Expression(
    val inputWire1: String,
    val inputWire2: String,
    val logicGate: LogicGate
)

fun main() {
    fun applyLogicGate(
        expression: Expression,
        wireValueMap: MutableMap<String, Int>
    ): Int {
        return when (expression.logicGate) {
            LogicGate.OR ->
                wireValueMap[expression.inputWire1]!! or wireValueMap[expression.inputWire2]!!

            LogicGate.AND ->
                wireValueMap[expression.inputWire1]!! and wireValueMap[expression.inputWire2]!!

            LogicGate.XOR ->
                wireValueMap[expression.inputWire1]!! xor wireValueMap[expression.inputWire2]!!
        }
    }

    fun part1(input: List<String>): Long {
        val wireValueMap: MutableMap<String, Int> = mutableMapOf()
        val inputToOutputDependencyGraph: MutableMap<String, MutableList<String>> =
            mutableMapOf()
        val wireValueExpression: MutableMap<String, Expression> = mutableMapOf()

        // assign initial wire values
        var idx = 0
        while (input[idx].isNotEmpty()) {
            // input line e.g. "x00: 1"
            val wire = input[idx].substring(0, 3)
            val value = input[idx].substring(5).toInt()
            wireValueMap[wire] = value
            idx++
        }

        val inDegreeMap: MutableMap<String, Int> = mutableMapOf()
        // section 2 (create output wire dependency graph)
        while (++idx < input.size) {
            // input line e.g. frj XOR qhw -> z04
            val (inputWire1, gate, inputWire2, outputWire)
                    = input[idx].split(" -> ", " ")
            val logicGate = LogicGate.valueOf(gate)
            wireValueExpression[outputWire] =
                Expression(inputWire1, inputWire2, logicGate)

            inputToOutputDependencyGraph.putIfAbsent(
                inputWire1,
                mutableListOf()
            )
            inputToOutputDependencyGraph[inputWire1]!!.add(outputWire)

            inputToOutputDependencyGraph.putIfAbsent(
                inputWire2,
                mutableListOf()
            )
            inputToOutputDependencyGraph[inputWire2]!!.add(outputWire)

            //update in degree of output wire
            inDegreeMap.putIfAbsent(inputWire1, 0)
            inDegreeMap.putIfAbsent(inputWire2, 0)

            inDegreeMap[outputWire] =
                inDegreeMap.getOrDefault(outputWire, 0) + 2
        }

        val que: Queue<String> = LinkedList()
        for ((wire, inDegree) in inDegreeMap) {
            if (inDegree == 0)
                que.add(wire)
        }

        // find execution sequence of gate using topological sort
        val executionSequence = mutableListOf<String>()
        while (que.isNotEmpty()) {
            val inputWire = que.poll()!!
            executionSequence.add(inputWire)

            for (outputWire in (inputToOutputDependencyGraph[inputWire]
                ?: emptyList())) {
                inDegreeMap[outputWire] = inDegreeMap[outputWire]!! - 1

                if (inDegreeMap[outputWire]!! == 0)
                    que.add(outputWire)

            }
        }

//        executionSequence.println()
        for (wire in executionSequence) {
            if (wire[0] == 'x' || wire[0] == 'y')
                continue
//            wire.println()
            wireValueMap[wire] =
                applyLogicGate(wireValueExpression[wire]!!, wireValueMap)
        }

//        wireValueMap.println()

        var outputInDecimal = 0L
        val wireOutputs = wireValueMap.keys.filter { it[0] == 'z' }
            .sortedDescending().map { wireValueMap[it]!!.toLong() }
        for (wireOutput in wireOutputs) {
            outputInDecimal = (outputInDecimal shl 1) or wireOutput
        }

        return outputInDecimal
    }

    fun part2(input: List<String>): String {
        val wireToExpressionMap: MutableMap<String, Expression> = mutableMapOf()
        val expressionToWireMap: MutableMap<Expression, String> = mutableMapOf()

        var idx = input.indexOf("")

        // section 2 (create output wire dependency graph)
        var outputWireCount = 0
        while (++idx < input.size) {
            // input line e.g. frj XOR qhw -> z04
            var (inputWire1, gate, inputWire2, outputWire)
                    = input[idx].split(" -> ", " ")
            val logicGate = LogicGate.valueOf(gate)

            if (outputWire[0] == 'z') {
                outputWireCount++
            }
            // store sorted input wire
            if (inputWire1 > inputWire2) {
                val swapTemp = inputWire1
                inputWire1 = inputWire2
                inputWire2 = swapTemp
            }
            with(Expression(inputWire1, inputWire2, logicGate)) {
                wireToExpressionMap[outputWire] = this
                expressionToWireMap[this] = outputWire
            }
        }

//        expressionToWireMap.keys
//            .filter { it.inputWire1[0] == 'x' && it.logicGate == LogicGate.XOR }
//            .sortedBy { it.inputWire1 }.size.println()
        /*
                fun pp(wire: String, depth: Int): String {
                    if (wire[0] in setOf('x', 'y'))
                        return "\t".repeat(depth) + wire

                    val (x, y, op) = wireValueExpression[wire]!!

                    return "\t".repeat(depth) + op + " (" + wire + ")\n" +
                            pp(x, depth + 1) + "\n" + pp(y, depth + 1)
                }

                println(pp("z02", 0))

         */

        val swappedOutputWire = mutableListOf<String>()

        fun swapOutput(output1: String, output2: String) {
            val output1Expression = wireToExpressionMap[output1]!!
            val output2Expression = wireToExpressionMap[output2]!!

            // swap output wire
            wireToExpressionMap[output1] = output2Expression
            expressionToWireMap[output1Expression] = output2

            wireToExpressionMap[output2] = output1Expression
            expressionToWireMap[output2Expression] = output1

            // store swapped output wire for final ans
            swappedOutputWire.add(output1)
            swappedOutputWire.add(output2)
        }

        fun findDirectOutputWire(outputWireNumber: Int): String {
            // find output wire phase
            val actualInputWire1 = "x" + "$outputWireNumber".padStart(2, '0')
            val actualInputWire2 = "y" + "$outputWireNumber".padStart(2, '0')

            val actualExpression =
                Expression(actualInputWire1, actualInputWire2, LogicGate.XOR)
            val actualOutputWire = expressionToWireMap[actualExpression] ?: throw IllegalStateException("$actualExpression") // here I got the error so that's why I used throw

            return actualOutputWire
        }

        fun findCarryWire(outputWireNumber: Int): String {
            fun findDirectCarryWire(outputWireNumber: Int): String {
                // find actual output wire
                val actualInputWire1 =
                    "x" + "$outputWireNumber".padStart(2, '0')
                val actualInputWire2 =
                    "y" + "$outputWireNumber".padStart(2, '0')

                val actualExpression = Expression(
                    actualInputWire1,
                    actualInputWire2,
                    LogicGate.AND
                )
                return expressionToWireMap[actualExpression]!!
            }

            fun findForwardedCarryWire(outputWireNumber: Int): String {
                // find actual output wire
                var actualInputWire1 = findDirectOutputWire(outputWireNumber)
                var actualInputWire2 = findCarryWire(outputWireNumber - 1)

                if (actualInputWire1 > actualInputWire2) {
                    val swapTemp = actualInputWire1
                    actualInputWire1 = actualInputWire2
                    actualInputWire2 = swapTemp
                }

                val actualExpression = Expression(
                    actualInputWire1,
                    actualInputWire2,
                    LogicGate.AND
                )
                return expressionToWireMap[actualExpression]!!
            }

            if (outputWireNumber == 0)
                return findDirectCarryWire(outputWireNumber)

            // find actual output wire
            var actualInputWire1 = findForwardedCarryWire(outputWireNumber)
            var actualInputWire2 = findDirectCarryWire(outputWireNumber)
            // sort input to find output wire
            if (actualInputWire1 > actualInputWire2) {
                val swapTemp = actualInputWire1
                actualInputWire1 = actualInputWire2
                actualInputWire2 = swapTemp
            }

            val actualExpression =
                Expression(actualInputWire1, actualInputWire2, LogicGate.OR)
            return expressionToWireMap[actualExpression]!!
        }

        fun checkOutputWire(outputWireNumber: Int) {

            // current output expression
            val outputWire = "z" + "$outputWireNumber".padStart(2, '0')
            val outputExpression = wireToExpressionMap[outputWire]!!

            // find actual output wire
            // first case
            if (outputWireNumber == 0) {
                val actualOutputWire = findDirectOutputWire(outputWireNumber)
                if (actualOutputWire != outputWire) {
                    swapOutput(actualOutputWire, outputWire)
                }
                return
            }
            // last case where only carry should be calculated
            if (outputWireNumber == outputWireCount - 1) {
                val actualOutputWire = findCarryWire(outputWireNumber-1)
                if (actualOutputWire != outputWire) {
                    swapOutput(actualOutputWire, outputWire)
                }
                return
            }

            var actualInputWire1 = findDirectOutputWire(outputWireNumber)
            var actualInputWire2 = findCarryWire(outputWireNumber - 1)

            if (actualInputWire1 > actualInputWire2) {
                val swapTemp = actualInputWire1
                actualInputWire1 = actualInputWire2
                actualInputWire2 = swapTemp
            }

            val actualExpression = Expression(
                actualInputWire1,
                actualInputWire2,
                LogicGate.XOR
            )

            if (actualExpression in expressionToWireMap) {
                val actualOutputWire = expressionToWireMap[actualExpression]!!

                if (outputWire != actualOutputWire) {
                    swapOutput(outputWire, actualOutputWire)
                }
            } else if (outputExpression.logicGate == LogicGate.XOR) {
                if (outputExpression.inputWire1 == actualInputWire1) {
                    swapOutput(outputExpression.inputWire2, actualInputWire2)
                } else if (outputExpression.inputWire1 == actualInputWire2) {
                    swapOutput(outputExpression.inputWire2, actualInputWire1)
                } else if (outputExpression.inputWire2 == actualInputWire1) {
                    swapOutput(outputExpression.inputWire1, actualInputWire2)
                } else if (outputExpression.inputWire2 == actualInputWire2) {
                    swapOutput(outputExpression.inputWire1, actualInputWire1)
                } else {
                    throw IllegalStateException("Logical error")
                }
            } else {
                throw IllegalStateException("Logical error")
            }
        }


        for (i in 0..<outputWireCount) {
            checkOutputWire(i)
        }

//        check(swappedOutputWire.size == 8)
        swappedOutputWire.sort()
        return swappedOutputWire.joinToString (",")
    }

    val input = """
        x00: 1
        x01: 0
        x02: 1
        x03: 1
        x04: 0
        y00: 1
        y01: 1
        y02: 1
        y03: 1
        y04: 1

        ntg XOR fgs -> mjb
        y02 OR x01 -> tnw
        kwq OR kpj -> z05
        x00 OR x03 -> fst
        tgd XOR rvg -> z01
        vdt OR tnw -> bfw
        bfw AND frj -> z10
        ffh OR nrd -> bqk
        y00 AND y03 -> djm
        y03 OR y00 -> psh
        bqk OR frj -> z08
        tnw OR fst -> frj
        gnj AND tgd -> z11
        bfw XOR mjb -> z00
        x03 OR x00 -> vdt
        gnj AND wpb -> z02
        x04 AND y00 -> kjc
        djm OR pbm -> qhw
        nrd AND vdt -> hwm
        kjc AND fst -> rvg
        y04 OR y02 -> fgs
        y01 AND x02 -> pbm
        ntg OR kjc -> kwq
        psh XOR fgs -> tgd
        qhw XOR tgd -> z09
        pbm OR djm -> kpj
        x03 XOR y03 -> ffh
        x00 XOR y04 -> ntg
        bfw OR bqk -> z06
        nrd XOR fgs -> wpb
        frj XOR qhw -> z04
        bqk OR frj -> z07
        y03 OR x01 -> nrd
        hwm AND bqk -> z03
        tgd XOR rvg -> z12
        tnw OR pbm -> gnj
    """.trimIndent().lines()

    check(part1(input) == 2024L)
    part1(readInput("Day24")).println()

    part2(readInput("Day24")).println()
}
