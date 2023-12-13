package day13

import println
import readInput

private typealias Grid = List<String>

fun main() {

    fun findHorizontalSymmetry(grid: Grid): Int {
        for (i in 0 until grid.size - 1) {
            val (first, second) = listOf(grid.slice(0..i), grid.slice(i + 1 until grid.size))

            val combined = if (first.size > second.size) {
                second.zip(first.reversed())
            } else {
                first.reversed().zip(second)
            }
            if (combined.all { it.first == it.second }) return i + 1
        }
        return 0
    }

    fun findVerticalSymmetry(grid: Grid): Int {
        for (i in 0 until grid.first().length - 1) {
            val first: Grid = mutableListOf()
            val second: Grid = mutableListOf()
            grid.forEach {
                var newFirst = it.substring(0, i + 1)
                var newSecond = it.substring(i + 1, grid.first().length)

                if (newFirst.length > newSecond.length) {
                    newFirst = newFirst.reversed().substring(0, newSecond.length).reversed()
                } else {
                    newSecond = newSecond.substring(0, newFirst.length)
                }
                first.addLast(newFirst)
                second.addLast(newSecond.reversed())
            }

            if (first.zip(second).all { it.first == it.second }) return i + 1
        }

        return 0
    }

    fun part1(input: List<String>): Int {
        val grids = mutableListOf<Grid>()
        var currentGrid: Grid = mutableListOf()
        input.forEach {
            if (it.isBlank()) {
                grids.add(currentGrid)
                currentGrid = mutableListOf()
            } else {
                currentGrid.addLast(it)
            }
        }
        grids.add(currentGrid)

        return grids.sumOf {
            (findHorizontalSymmetry(it) * 100) + findVerticalSymmetry(it)
        }
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    val input = readInput("day13")
    part1(input).println()
    part2(input).println()
}
