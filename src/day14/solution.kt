package day14

import println
import readInput
import transpose

private enum class Rock(val symbol: Char) {
    MOVE('O'),
    LOCKED('#'),
    EMPTY('.');

    companion object {
        fun fromSymbol(symbol: Char): Rock {
            return Rock.entries.first { it.symbol == symbol }
        }
    }
}

fun main() {

    fun canMoveUp(grid: List<List<Rock>>, x: Int, y: Int): Boolean {
        if (y == 0) return false
        return grid[y - 1][x] == Rock.EMPTY
    }

    fun slideAllRocks(grid: MutableList<MutableList<Rock>>): MutableList<MutableList<Rock>> {
        grid.indices.forEach {
            for (y in it downTo 0) {
                for (x in grid.first().indices) {
                    if (grid[y][x] == Rock.MOVE && canMoveUp(grid, x, y)) {
                        val temp = grid[y - 1][x]
                        grid[y - 1][x] = grid[y][x]
                        grid[y][x] = temp
                    }
                }
            }
        }
        return grid
    }

    fun scoreOfGrid(grid: MutableList<MutableList<Rock>>) =
        grid.mapIndexed { index, rocks ->
            rocks.filter { it == Rock.MOVE }.size * (grid.size - index)
        }.sum()

    fun part1(input: List<String>): Int {
        val grid = input.map { it.map(Rock::fromSymbol).toMutableList() }.toMutableList()

        slideAllRocks(grid)

        //grid.joinToString("\n") { rocks -> rocks.joinToString("") { it.symbol.toString() } }.println()

        return scoreOfGrid(grid)
    }

    fun part2(input: List<String>): Int {
        var grid = input.map { it.map(Rock::fromSymbol).toMutableList() }.toMutableList()

        val prevScore = mutableListOf<Int>()

        for (cycle in 0 until 136) {
            for (round in 0 until 4) {
                grid = slideAllRocks(grid)
                grid = grid.transpose().map { it.reversed().toMutableList() }.toMutableList()
            }
            val scoreOfGrid = scoreOfGrid(grid)
            if (prevScore.contains(scoreOfGrid)) {
                println("#${cycle + 1} - $scoreOfGrid - ${cycle - prevScore.lastIndexOf(scoreOfGrid)}")
            }
            prevScore.add(scoreOfGrid)
        }

        return scoreOfGrid(grid)
    }

    val input = readInput("day14")
    part1(input).println()
    part2(input).println()
}
