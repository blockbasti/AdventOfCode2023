package day09

import println
import readInput

fun main() {
    fun parseInput(input: List<String>) =
        input.map { it.split(" ") }.map { it.map { it2 -> it2.toInt() } }

    fun getDifferencesFromHistory(history: List<Int>): MutableList<MutableList<Int>> {
        val predictions = mutableListOf(history.toMutableList())

        // parse history data
        var currentDiffs: List<Int> = history
        do {
            currentDiffs = currentDiffs.windowed(2).map { window -> window.last() - window.first() }.toMutableList()
            predictions.addLast(currentDiffs)
        } while (!currentDiffs.all { it == 0 })

        predictions.reverse()
        return predictions
    }

    fun part1(input: List<String>): Int {
        val histories = parseInput(input)

        return histories.sumOf { history ->
            val predictions = getDifferencesFromHistory(history)

            // generate predictions
            predictions.drop(1).mapIndexed { index, differences ->
                val nextNumber = predictions.getOrNull(index)?.last() ?: 0
                differences.addLast(differences.last() + nextNumber)
                differences
            }.last().last()
        }

    }

    fun part2(input: List<String>): Int {
        val histories = parseInput(input)

        return histories.sumOf { history ->
            val predictions = getDifferencesFromHistory(history)

            // generate predictions
            predictions.drop(1).mapIndexed { index, differences ->
                val nextNumber = predictions.getOrNull(index)?.first() ?: 0
                differences.addFirst(differences.first() - nextNumber)
                differences
            }.last().first()
        }
    }

    val input = readInput("day09")
    part1(input).println()
    part2(input).println()
}
