package day04

import println
import readInput
import kotlin.math.pow

fun main() {
    fun part1(input: List<String>): Int {
        var sum = 0

        input.forEach { line ->
            val winningNumbers =
                line.substring(9).split("|")[0].split(" ").filter { it.isNotBlank() }.map { it.toInt() }
            val myNumbers = line.substring(9).split("|")[1].split(" ").filter { it.isNotBlank() }.map { it.toInt() }

            val combined = winningNumbers.plus(myNumbers).groupBy { it }.filter { it.value.size >= 2 }.map { it.key }
            sum += 2.0.pow(combined.size.toDouble() - 1).toInt()
        }

        return sum
    }

    fun calculateCardScore(cardNumber: Int, input: List<String>): Int {
        val card = input[cardNumber]
        val winningNumbers = card.substring(9).split("|")[0].split(" ").filter { it.isNotBlank() }.map { it.toInt() }
        val myNumbers = card.substring(9).split("|")[1].split(" ").filter { it.isNotBlank() }.map { it.toInt() }

        return winningNumbers.plus(myNumbers).groupBy { it }.filter { it.value.size >= 2 }.map { it.key }.size
    }

    fun calculateCardMap(currentMap: Map<Int, Int>, input: List<String>): MutableMap<Int, Int> {
        val cardMap = currentMap.toMutableMap()

        for (cardNumber in cardMap.keys) {
            val score = calculateCardScore(cardNumber, input)
            val cardCount = cardMap[cardNumber]!!

            println("Card ${cardNumber+1} x $cardCount with score $score")
            for (i in cardNumber + 1..(cardNumber + score).coerceAtMost(input.size - 1)) {
                cardMap[i] = cardMap[i]!! + (cardCount).coerceAtLeast(1)
                println("Card ${i+1} set to count ${cardMap[i]}")
            }

        }

        return cardMap
    }

    fun part2(input: List<String>): Int {
        var cardMap: MutableMap<Int, Int> = mutableMapOf()

        for (i in input.indices) {
            cardMap[i] = 1
        }

        cardMap = calculateCardMap(cardMap, input)
        println()
        //cardMap = calculateCardMap(cardMap.mapValues { it.value-1 }, input)
        cardMap.println()
        return cardMap.values.sum()
    }

    val input = readInput("day04")
    part1(input).println()
    part2(input).println()
}
