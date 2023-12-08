package day06

import println
import readInput

fun main() {

    fun part1(input: List<String>): Int {
        val times = input[0].drop(5).split(" ").filter { it.isNotBlank() }.map { it.toInt() }
        val records = input[1].drop(10).split(" ").filter { it.isNotBlank() }.map { it.toInt() }

        return times
            .zip(records)
            .map { entry ->
                (0..entry.first).map timeToSpeed@{ chargeTime ->
                    val raceTime = entry.first - chargeTime
                    return@timeToSpeed raceTime * chargeTime > entry.second
                }
            }.map { entry -> entry.filter { it }.size }
            .reduce { acc, i -> acc * i }
    }

    fun part2(input: List<String>): Int {
        val time = input[0].drop(5).filter { it != ' ' }.toLong()
        val record = input[1].drop(10).filter { it != ' ' }.toLong()

        return (0..<time).map { chargeTime ->
            val raceTime = time - chargeTime
            return@map (raceTime * chargeTime) > record
        }.filter { it }.size
    }

    val input = readInput("day06")
    part1(input).println()
    part2(input).println()
}
