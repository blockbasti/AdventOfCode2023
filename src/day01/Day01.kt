package day01

import println
import readInput

fun main() {
    fun part1(input: List<String>): Int {
        var sum = 0

        input.forEach {
            val numbers = it.filter { c -> c.isDigit() }
            val value = "${numbers.first()}${numbers.last()}".toInt()
            sum += value
        }

        return sum
    }

    fun part2(input: List<String>): Int {
        var sum = 0

        input.forEach { line ->
            val regex = Regex("(?=(one|two|three|four|five|six|seven|eight|nine|ten|\\d))")
            val matches = regex.findAll(line)
            val numbers = matches
                .map { it.groupValues[1] }
                .map {
                    when (it) {
                        "one" -> 1
                        "two" -> 2
                        "three" -> 3
                        "four" -> 4
                        "five" -> 5
                        "six" -> 6
                        "seven" -> 7
                        "eight" -> 8
                        "nine" -> 9
                        "" -> 0
                        else -> it.toInt()
                    }
                }
                .toList()

            val value = "${numbers.first()}${numbers.last()}".toInt()
            sum += value
        }
        return sum
    }

    val input = readInput("day01")
    part1(input).println()
    part2(input).println()
}
