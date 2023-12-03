package day02

import println
import readInput
import kotlin.math.max

fun main() {
    fun part1(input: List<String>): Int {
        val RED = 12
        val GREEN = 13
        val BLUE = 14

        var sum = 0

        input.forEach { line ->
            val gameId = Regex("Game (\\d+):").find(line)!!.groupValues[1].toInt()

            val sets = line.removePrefix("Game $gameId:").split(";", ",")
            var wasOver = false
            sets.forEach { set ->
                val count = Regex("\\d+").find(set)!!.value.toInt()
                when {
                    set.contains("red") -> {
                        if (count > RED) wasOver = true
                    }

                    set.contains("green") -> {
                        if (count > GREEN) wasOver = true
                    }

                    set.contains("blue") -> {
                        if (count > BLUE) wasOver = true
                    }
                }
            }
            if (!wasOver) {
                sum += gameId
            }
        }

        return sum
    }

    fun part2(input: List<String>): Int {
        var sum = 0

        input.forEach { line ->
            val gameId = Regex("Game (\\d+):").find(line)!!.groupValues[1].toInt()

            val sets = line.removePrefix("Game $gameId:").split(";", ",")
            var red = 0
            var blue = 0
            var green = 0
            sets.forEach { set ->
                val count = Regex("\\d+").find(set)!!.value.toInt()
                when {
                    set.contains("red") -> {
                        red = max(count, red)
                    }

                    set.contains("green") -> {
                        green = max(count, green)
                    }

                    set.contains("blue") -> {
                        blue = max(count, blue)
                    }
                }
            }
            sum += red * green * blue
        }

        return sum
    }

    val input = readInput("day02")
    part1(input).println()
    part2(input).println()
}
