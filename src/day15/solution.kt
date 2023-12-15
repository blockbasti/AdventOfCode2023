package day15

import println
import readInput

fun main() {
    fun hash(input: String): Int {
        var sum = 0
        input.forEach { c ->
            sum += c.code
            sum *= 17
            sum %= 256
        }
        return sum
    }

    fun part1(input: List<String>): Int {
        return input.first().split(",")
            .sumOf(::hash)
    }


    fun part2(input: List<String>): Int {
        val map = MutableList<LinkedHashMap<String, Int>>(256) {
            LinkedHashMap()
        }

        input.first().split(",").forEach { instruction ->
            val (label, operation, focal) = instruction.split(Regex("(?<=[-=])|(?=[-=])"))
            when (operation) {
                "-" -> {
                    map[hash(label)].remove(label)
                }

                "=" -> {
                    if (map[hash(label)].contains(label))
                        map[hash(label)].replace(label, focal.toInt())
                    else
                        map[hash(label)].putLast(label, focal.toInt())
                }

                else -> {}
            }
        }
        return map.withIndex().sumOf { box ->
            box.value.entries.withIndex().sumOf { (box.index + 1) * (it.index + 1) * it.value.value }
        }
    }


    val input = readInput("day15")
    part1(input).println()
    part2(input).println()
}
