package day03

import println
import readInput

fun main() {
    fun isSymbol(char: Char?): Boolean {
        return when {
            char == null -> false
            char.isDigit() -> false
            char == '.' -> false
            else -> true
        }
    }

    fun checkForSymbol(grid: List<List<Char>>, x: Int, y: Int): Boolean {
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
            var number = mutableListOf<Int>()
            var nextToSymbol = false
            row.forEachIndexed { columnIndex, column ->
                if (column.isDigit()) {
                    number.add(column.digitToInt())
                    if (checkForSymbol(grid, rowIndex, columnIndex)) nextToSymbol = true
                } else {
                    if (nextToSymbol) {
                        sum += number.joinToString(separator = "").toInt()
                        println(number.joinToString(separator = "").toInt())
                    }

                    number = mutableListOf()
                    nextToSymbol = false
                }
            }
        }


        return sum
    }

    fun part2(input: List<String>): Int {
        var sum = 0

        return sum
    }

    val input = readInput("day03")
    part1(input).println()
    part2(input).println()
}
