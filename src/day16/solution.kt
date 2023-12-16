package day16

import day16.Symbol.*
import formattedString
import println
import readInput
import toInt

private var grid: List<MutableList<Pair<Symbol, Boolean>>> = listOf()

private enum class Symbol(val symbol: Char) {
    EMPTY('.'),
    MIRROR_LU('/'),
    MIRROR_RD('\\'),
    SPLITTER_UD('|'),
    SPLITTER_LR('-');

    companion object {
        fun fromSymbol(char: Char): Symbol {
            return Symbol.entries.first { it.symbol == char }
        }
    }

    override fun toString(): String {
        return "$symbol"
    }
}

private enum class Direction {
    UP, DOWN, LEFT, RIGHT
}

private class Beam(var x: Int, var y: Int, var direction: Direction) {

    fun setPosition(x: Int, y: Int) {
        this.x = x
        this.y = y
    }

    fun shouldSplitUpDown(): Boolean {
        if (isInvalidPosition()) return false
        return (direction == Direction.LEFT || direction == Direction.RIGHT) && grid[y][x].first == SPLITTER_UD
    }

    fun shouldSplitLeftRight(): Boolean {
        if (isInvalidPosition()) return false
        return (direction == Direction.UP || direction == Direction.DOWN) && grid[y][x].first == SPLITTER_LR
    }

    fun move(): Boolean {
        if (isInvalidPosition()) return false
        when (grid[y][x].first) {
            MIRROR_LU -> direction = when (direction) {
                Direction.UP -> Direction.LEFT
                Direction.DOWN -> Direction.RIGHT
                Direction.LEFT -> Direction.DOWN
                Direction.RIGHT -> Direction.UP
            }

            MIRROR_RD -> direction = when (direction) {
                Direction.UP -> Direction.RIGHT
                Direction.DOWN -> Direction.LEFT
                Direction.LEFT -> Direction.UP
                Direction.RIGHT -> Direction.DOWN
            }

            else -> {}
        }
        when (direction) {
            Direction.UP -> y--
            Direction.DOWN -> y++
            Direction.LEFT -> x--
            Direction.RIGHT -> x++
        }

        return true
    }

    fun isInvalidPosition(): Boolean {
        return y < 0 || y >= grid.size || x < 0 || x >= grid.first().size
    }

    override fun toString(): String {
        return "Beam(x=$x, y=$y, direction=$direction)"
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        grid = input.map { row -> row.map { Symbol.fromSymbol(it) to false }.toMutableList() }
        val beams = mutableListOf(Beam(0, 0, Direction.RIGHT))

        while (beams.isNotEmpty()) {
            val toRemove = mutableListOf<Beam>()
            val toAdd = mutableListOf<Beam>()
            for (beam in beams) {
                try {
                    grid[beam.y][beam.x] = grid[beam.y][beam.x].first to true
                } catch (e: Exception) {
                }

                val (origX, origY) = listOf(beam.x.toInt(), beam.y.toInt())

                if (beam.shouldSplitUpDown()) {
                    beam.setPosition(origX, origY - 1)
                    beam.direction = Direction.UP
                    toAdd.add(Beam(origX, origY + 1, Direction.DOWN))
                    continue
                }
                if (beam.shouldSplitLeftRight()) {
                    beam.setPosition(origX - 1, origY)
                    beam.direction = Direction.LEFT
                    toAdd.add(Beam(origX + 1, origY, Direction.RIGHT))
                    continue
                }

                if (!beam.move() || beam.isInvalidPosition()) {
                    toRemove.add(beam)
                    continue
                }


            }
            beams.removeAll(toRemove)
            beams.addAll(toAdd)
            println()
            grid.formattedString { it.second.toInt().toString() }.println()
        }

        return grid.sumOf { row -> row.sumOf { it.second.toInt() } }
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    val input = readInput("day16")
    part1(input).println()
    part2(input).println()
}
