package day07

import println
import readInput

fun main() {

    class CamelCardHand(val cardString: String, val bidAmount: Int) : Comparable<CamelCardHand> {
        val handType: Int = cardString.groupBy { it }.let { map ->
            when (map.size) {
                1 -> 7// Five of a kind
                2 -> if (map.any { it.value.size == 4 }) 6 // Four of a kind
                else 5// Full house
                3 -> if (map.any { it.value.size == 3 }) 4 // Three of a kind
                else 3 // Two pair
                4 -> 2 // One pair
                5 -> 1 // High card
                else -> {
                    0
                } // Kann nicht vorkommen
            }
        }

        override fun compareTo(other: CamelCardHand): Int {
            if (this.handType > other.handType) return 1
            else if (this.handType < other.handType) return -1
            else {
                this.cardString.forEachIndexed { index, char ->
                    run {
                        if (getValueForChar(char) > getValueForChar(other.cardString[index])) {
                            return 1
                        } else if (getValueForChar(char) < getValueForChar(other.cardString[index])) {
                            return -1
                        }
                    }
                }
                return 0
            }
        }

        private fun getValueForChar(char: Char): Int {
            return when (char) {
                'A' -> 14
                'K' -> 13
                'Q' -> 12
                'J' -> 11
                'T' -> 10
                else -> char.digitToInt()
            }
        }
    }

    class CamelCardHand2(val cardString: String, val bidAmount: Int) : Comparable<CamelCardHand2> {
        val handType: Int = cardString.groupBy { it }.let { map ->
            when (map.size) {
                1 -> 7// Five of a kind
                2 -> if (map.any { it.value.size == 4 }) 6 // Four of a kind
                else 5// Full house
                3 -> if (map.any { it.value.size == 3 }) 4 // Three of a kind
                else 3 // Two pair
                4 -> 2 // One pair
                5 -> 1 // High card
                else -> {
                    0
                } // Kann nicht vorkommen
            }
        }

        override fun compareTo(other: CamelCardHand2): Int {
            if (this.handType > other.handType) return 1
            else if (this.handType < other.handType) return -1
            else {
                this.cardString.forEachIndexed { index, char ->
                    run {
                        if (getValueForChar(char) > getValueForChar(other.cardString[index])) {
                            return 1
                        } else if (getValueForChar(char) < getValueForChar(other.cardString[index])) {
                            return -1
                        }
                    }
                }
                return 0
            }
        }

        private fun getValueForChar(char: Char): Int {
            return when (char) {
                'A' -> 14
                'K' -> 13
                'Q' -> 12
                'J' -> 1
                'T' -> 10
                else -> char.digitToInt()
            }
        }
    }

    fun part1(input: List<String>): Int {
        val cardList = ArrayList<CamelCardHand>()

        for (line in input) {
            cardList.add(CamelCardHand(line.split(" ")[0], line.split(" ")[1].toInt()))
        }

        var sum = 0
        cardList.sorted().forEachIndexed { index, camelCardHand -> sum += (index + 1) * camelCardHand.bidAmount }
        return sum

    }

    fun part2(input: List<String>): Int {
        var cardList = mutableListOf<CamelCardHand2>()

        for (line in input) {
            cardList.add(CamelCardHand2(line.split(" ")[0], line.split(" ")[1].toInt()))
        }

        cardList = cardList.map { card ->
            var hightestVersion = card
            for (jokerReplace in "A,K,Q,T,9,8,7,6,5,4,3,2".split(",")) {
                val newCard = CamelCardHand2(card.cardString.replace("J", jokerReplace), card.bidAmount)
                if (newCard.compareTo(hightestVersion) == 1) {
                    hightestVersion = newCard
                }
            }
            hightestVersion
        }.toMutableList()

        var sum = 0
        cardList.sorted().forEachIndexed { index, camelCardHand -> sum += (index + 1) * camelCardHand.bidAmount }
        return sum
    }

    val input = readInput("day07")
    part1(input).println()
    part2(input).println()
}
