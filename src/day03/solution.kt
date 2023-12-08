package day03

import println
import readInput

fun main() {
    fun isSymbol(char: Char?): Boolean {
        return char != null && char.isDigit() && char != '.'
    }

    fun checkForAdjacentSymbol(grid: List<List<Char>>, x: Int, y: Int): Boolean {
        for (row in -1..1) {
            for (column in -1..1) {
                if (row == 0 && column == 0) continue

                if (isSymbol(grid.getOrNull(x + row)?.getOrNull(y + column))) {
                    return true
                }
            }
        }

        return false
    }

    fun part1(input: List<String>): Int {
        var sum = 0

        val grid = input.map { it.toCharArray().toList() }

        grid.forEachIndexed { rowIndex, row ->
            var number = 0
            var nextToSymbol = false
            row.forEachIndexed { columnIndex, column ->
                if (column.isDigit()) {
                    number = number * 10 + column.digitToInt()
                    if (checkForAdjacentSymbol(grid, rowIndex, columnIndex)) nextToSymbol = true
                } else {
                    if (nextToSymbol) {
                        sum += number
                        //println("Valid: $number")
                    } //else if (number != 0) println("Invalid: $number")

                    number = 0
                    nextToSymbol = false
                }
            }
        }

        return sum
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    val input = readInput("day03")
    part1(input).println()
    part2(input).println()
}
