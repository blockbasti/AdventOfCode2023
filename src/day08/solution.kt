package day08

import println
import readInput


fun main() {
    fun part1(input: List<String>): Int {

        val instructions: List<Char> = input[0].toList()
        val map = input.drop(2).map { line ->
            val (key, directions) = line.split("=").map { it.trim() }
            val (left, right) = directions.removeSurrounding("(", ")").split(",").map { it.trim() }
            key to Pair(left, right)
        }.toMap()

        var currentLocation = "AAA"
        var steps = 0
        while (currentLocation != "ZZZ") {
            currentLocation =
                if (instructions[steps % instructions.size] == 'L') map[currentLocation]!!.first else map[currentLocation]!!.second
            steps++
        }

        return steps
    }

    fun findLCM(a: Long, b: Long): Long {
        val larger: Long = if (a > b) a else b
        val maxLcm: Long = a * b
        var lcm: Long = larger
        while (lcm <= maxLcm) {
            if (lcm % a == 0L && lcm % b == 0L) {
                return lcm
            }
            lcm += larger
        }
        return maxLcm
    }

    fun lcm(input: List<Long>): Long {
        var result = input[0]
        for (i in 1 until input.size) result = findLCM(result, input[i])
        return result
    }

    fun part2(input: List<String>): Long {
        val instructions: List<Char> = input[0].toList()
        val map = input.drop(2).map { line ->
            val (key, directions) = line.split("=").map { it.trim() }
            val (left, right) = directions.removeSurrounding("(", ")").split(",").map { it.trim() }
            key to Pair(left, right)
        }.toMap()

        val currentLocations = map.keys.filter { it.endsWith("A") }

        val steps = currentLocations.map { location ->
            var currentLocation = location
            var steps = 0L
            while (!currentLocation.endsWith("Z")) {
                currentLocation =
                    if (instructions[(steps % instructions.size).toInt()] == 'L') map[currentLocation]!!.first else map[currentLocation]!!.second
                steps++
            }
            return@map steps
        }
        return lcm(steps)
    }

    val input = readInput("day08")
    part1(input).println()
    part2(input).println()
}
