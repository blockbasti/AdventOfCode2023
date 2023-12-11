package day11

import println
import readInput
import kotlin.math.abs

fun main() {
    fun extracted(space: MutableList<MutableList<Char>>): List<Pair<Int, Int>> {
        val galaxies: MutableList<Pair<Int, Int>> = mutableListOf()
        space.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { columnIndex, char ->
                if (char == '#') {
                    galaxies.add(Pair(columnIndex, rowIndex))
                }
            }
        }
        return galaxies
    }

    fun part1(input: List<String>): Long {
        val space = input.map { line -> line.toMutableList() }.toMutableList()

        var columnMax = space.first().size
        var column = 0
        while (column < columnMax) {
            if (space.map { row -> row[column] }.all { it == '.' }) {
                space.forEach { row -> row.add(column, '.') }
                columnMax++
                column++
            }
            column++
        }

        columnMax = space.size
        column = 0
        while (column < columnMax) {
            if (space[column].all { it == '.' }) {
                space.add(column, space[column])
                column++
                columnMax++
            }
            column++
        }

        val galaxies = extracted(space)

        //space.joinToString("\n") { it.joinToString("") }.println()

        return galaxies.mapIndexed { index, galaxy ->
            galaxies.drop(index + 1).sumOf { secondGalaxy ->
                val dist = abs(secondGalaxy.first - galaxy.first) + abs(secondGalaxy.second - galaxy.second)
                //println("Galaxy ${index + 1} $galaxy - $secondGalaxy = $dist")
                dist
            }
        }.sum().toLong()
    }

    fun isColumnEmpty(column: Int, space: MutableList<MutableList<Char>>): Boolean {
        return space.map { row -> row[column] }.all { it == '.' }
    }

    fun isRowEmpty(row: Int, space: MutableList<MutableList<Char>>): Boolean {
        return space[row].all { it == '.' }
    }

    fun part2(input: List<String>): Long {
        val space = input.map { line -> line.toMutableList() }.toMutableList()

        val galaxies = extracted(space)

        return galaxies.mapIndexed { index, galaxy ->
            galaxies.drop(index + 1).sumOf { secondGalaxy ->
                val (maxX, minX) = listOf(galaxy.first, secondGalaxy.first).sorted().reversed()
                val (maxY, minY) = listOf(galaxy.second, secondGalaxy.second).sorted().reversed()

                var dx = 0L
                for (column in minX until maxX) {
                    dx++
                    if (isColumnEmpty(column, space)) {
                        dx += 1_000_000 - 1
                    }

                }

                var dy = 0L
                for (row in minY until maxY) {
                    dx++
                    if (isRowEmpty(row, space)) {
                        dy += 1_000_000 - 1
                    }
                }
                return@sumOf dx + dy
            }
        }.sum()
    }

    val input = readInput("day11")
    part1(input).println()
    part2(input).println()
}
