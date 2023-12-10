package day10

import println
import readInput

private enum class Pipe(val symbol: Char) {
    START('S'),
    NS('|'),
    EW('-'),
    NE('L'),
    NW('J'),
    SW('7'),
    SE('F'),
    GROUND('.');

    companion object {
        fun fromSymbol(char: Char): Pipe {
            return Pipe.entries.first { it.symbol == char }
        }
    }
}

fun main() {
    fun generateMap(input: List<String>): List<List<Pipe>> {
        return input.map { line -> line.map { Pipe.fromSymbol(it) } }
    }

    lateinit var map: List<List<Pipe>>
    val visitedMap: MutableList<MutableList<Int>> = MutableList(140) { MutableList(140) { 0 } }

    fun newPosition(oldX: Int, oldY: Int, currentX: Int, currentY: Int, pipe: Pipe): Pair<Int, Int> {
        return when (pipe) {
            Pipe.NS -> {
                if (oldY < currentY) Pair(currentX, currentY + 1)
                else Pair(currentX, currentY - 1)
            }

            Pipe.EW -> {
                if (oldX > currentX) Pair(currentX - 1, currentY)
                else Pair(currentX + 1, currentY)
            }

            Pipe.NE -> {
                if (oldY < currentY) Pair(currentX + 1, currentY)
                else Pair(currentX, currentY - 1)
            }

            Pipe.NW -> {
                if (oldY < currentY) Pair(currentX - 1, currentY)
                else Pair(currentX, currentY - 1)
            }

            Pipe.SW -> {
                if (oldY > currentY) Pair(currentX - 1, currentY)
                else Pair(currentX, currentY + 1)
            }

            Pipe.SE -> {
                if (oldY > currentY) Pair(currentX + 1, currentY)
                else Pair(currentX, currentY + 1)
            }

            else -> Pair(currentX, currentY)
        }
    }

    fun getNextPositionForStart(x: Int, y: Int): Pair<Int, Int> {
        // Above
        if (map.getOrNull(y - 1)?.getOrNull(x)?.name?.contains('S') == true) {
            return Pair(x, y - 1)
        }
        // Below
        if (map.getOrNull(y + 1)?.getOrNull(x)?.name?.contains('N') == true) {
            return Pair(x, y + 1)
        }
        // Left
        if (map.getOrNull(y)?.getOrNull(x - 1)?.name?.contains('E') == true) {
            return Pair(x - 1, y)
        }
        // Right
        if (map.getOrNull(y)?.getOrNull(x + 1)?.name?.contains('W') == true) {
            return Pair(x + 1, y)
        }

        return Pair(x, y)
    }

    fun part1(input: List<String>): Int {
        map = generateMap(input)
        var (currentX, currentY, oldX, oldY) = listOf(0, 0, 0, 0)
        var lastSymbol: Pipe
        // find START
        map.forEachIndexed { row, pipes ->
            if (pipes.contains(Pipe.START)) {
                oldY = row
                oldX = pipes.indexOf(Pipe.START)
            }
        }
        val newPos = getNextPositionForStart(oldX, oldY)
        currentX = newPos.first
        currentY = newPos.second

        var currentDistance = 0
        do {
            currentDistance++
            val newPosition = newPosition(oldX, oldY, currentX, currentY, map[currentY][currentX])
            lastSymbol = map[currentY][currentX]
            oldX = currentX.toInt()
            oldY = currentY.toInt()
            currentX = newPosition.first
            currentY = newPosition.second
            visitedMap[currentY][currentX] = currentDistance

        } while (lastSymbol != Pipe.START)
        return currentDistance /2
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    val input = readInput("day10")
    part1(input).println()
    part2(input).println()
}
