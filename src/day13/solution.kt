package day13

import println
import readInput
import transpose

private typealias Grid = List<String>

fun main() {
    fun transposeGrid(grid: Grid): Grid {
        return grid.map { it.toList() }.transpose().map { it.joinToString("") }
    }

    fun findSymmetry(grid: Grid, transpose: Boolean): Int {
        val checkGrid = if (transpose) transposeGrid(grid) else grid
        for (i in 0 until checkGrid.size - 1) {
            val (first, second) = listOf(checkGrid.slice(0..i), checkGrid.slice(i + 1 until checkGrid.size))

            val combined = if (first.size > second.size) {
                second.zip(first.reversed())
            } else {
                first.reversed().zip(second)
            }
            if (combined.all { it.first == it.second }) return i + 1
        }
        return 0
    }

    fun modifyGridAndGetSymmetry(grid: Grid): Int {
        val oldHorizontal = findSymmetry(grid, false)
        val oldVertical = findSymmetry(grid, true)

        for (x in 0 until grid.first().length) {
            for (y in grid.indices) {
                val newGrid = grid.toMutableList()

                newGrid[y] = newGrid[y].replaceRange(x, x + 1, if (newGrid[y][x] == '#') "." else "#")

                val newHorizontal = findSymmetry(newGrid, false)
                val newVertical = findSymmetry(newGrid, true)

                if (newHorizontal != 0 && newHorizontal != oldHorizontal) {
                    return newHorizontal * 100
                } else if ((newVertical != 0 && newVertical != oldVertical)) {
                    return newVertical
                }
            }
        }

        return (oldHorizontal * 100) + oldVertical
    }

    fun part1(input: List<String>): Int {
        val grids = mutableListOf<Grid>()
        var currentGrid: Grid = mutableListOf()
        input.forEach {
            if (it.isEmpty()) {
                grids.add(currentGrid)
                currentGrid = mutableListOf()
            } else {
                currentGrid.addLast(it)
            }
        }
        grids.add(currentGrid)

        return grids.sumOf {
            (findSymmetry(it, false) * 100) + findSymmetry(it, true)
        }
    }

    fun part2(input: List<String>): Int {
        val grids = mutableListOf<Grid>()
        var currentGrid: Grid = mutableListOf()
        input.forEach {
            if (it.isEmpty()) {
                grids.add(currentGrid)
                currentGrid = mutableListOf()
            } else {
                currentGrid.addLast(it)
            }
        }
        grids.add(currentGrid)

        return grids.sumOf(::modifyGridAndGetSymmetry)
    }

    val input = readInput("day13")
    part1(input).println()
    part2(input).println()
}
