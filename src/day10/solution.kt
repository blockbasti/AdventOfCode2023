package day10

import println
import readInput
import java.util.*

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

private enum class Field(val symbol: Char) {
    UNVISITED(' '),
    LOOP('X'),
    GAP('G'),
    INSIDE('I'),
    OUTSIDE('.'),
}

fun main() {
    fun generateMap(input: List<String>): List<List<Pipe>> {
        return input.map { line -> line.map { Pipe.fromSymbol(it) } }
    }

    lateinit var map: List<List<Pipe>>
    var visitedMap: MutableList<MutableList<Field>> = MutableList(140) { MutableList(140) { Field.UNVISITED } }
    val gapsHorizontal: List<Pair<Pipe?, Pipe?>> = listOf(
        Pair(Pipe.fromSymbol('-'), Pipe.fromSymbol('-')),

        Pair(Pipe.fromSymbol('L'), Pipe.fromSymbol('7')),
        Pair(Pipe.fromSymbol('L'), Pipe.fromSymbol('F')),

        Pair(Pipe.fromSymbol('J'), Pipe.fromSymbol('7')),
        Pair(Pipe.fromSymbol('J'), Pipe.fromSymbol('F')),
    )

    val gapsVertical: List<Pair<Pipe?, Pipe?>> = listOf(
        Pair(Pipe.NS, Pipe.NS),

        Pair(Pipe.fromSymbol('J'), Pipe.fromSymbol('L')),
        Pair(Pipe.fromSymbol('7'), Pipe.fromSymbol('L')),

        Pair(Pipe.fromSymbol('J'), Pipe.fromSymbol('F')),
        Pair(Pipe.fromSymbol('7'), Pipe.fromSymbol('F')),
    )

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
                visitedMap[oldY][oldX] = Field.LOOP
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
            visitedMap[currentY][currentX] = Field.LOOP
        } while (lastSymbol != Pipe.START)
        return currentDistance / 2
    }

    val floodQueue: Queue<Pair<Pair<Int, Int>, Boolean>> = LinkedList()

    fun isGap(x: Int, y: Int, horizontal: Boolean): Boolean {
        if (horizontal) {
            var tiles = Pair(map.getOrNull(y)?.getOrNull(x), map.getOrNull(y + 1)?.getOrNull(x))
            if (gapsHorizontal.contains(tiles)) return true
            tiles = Pair(map.getOrNull(y - 1)?.getOrNull(x), map.getOrNull(y)?.getOrNull(x))
            if (gapsHorizontal.contains(tiles)) return true
        } else {
            var tiles = Pair(map.getOrNull(y)?.getOrNull(x), map.getOrNull(y)?.getOrNull(x + 1))
            if (gapsVertical.contains(tiles)) return true
            tiles = Pair(map.getOrNull(y)?.getOrNull(x - 1), map.getOrNull(y)?.getOrNull(x))
            if (gapsVertical.contains(tiles)) return true
        }
        return false
    }

    fun floodFill() {
        val (coords, horizontal) = floodQueue.poll()
        val (x, y) = coords
        if (x < 0 || x > 139 || y < 0 || y > 139) return
        val isGap = isGap(x, y, horizontal)
        if (visitedMap[y][x] != Field.OUTSIDE && visitedMap[y][x] != Field.LOOP && visitedMap[y][x] != Field.GAP) {
            floodQueue.add(Pair(Pair(x, y + 1), false))
            floodQueue.add(Pair(Pair(x, y - 1), false))
            floodQueue.add(Pair(Pair(x + 1, y), true))
            floodQueue.add(Pair(Pair(x - 1, y), true))
            //floodQueue.add(Pair(x + 1, y + 1))
            //floodQueue.add(Pair(x + 1, y - 1))
            //floodQueue.add(Pair(x - 1, y + 1))
            //floodQueue.add(Pair(x - 1, y + 1))
            visitedMap[y][x] = Field.OUTSIDE
        } else if (isGap && horizontal && visitedMap[y][x] != Field.GAP) {
            floodQueue.add(Pair(Pair(x + 1, y), true))
            floodQueue.add(Pair(Pair(x - 1, y), true))
            visitedMap[y][x] = Field.GAP
        } else if (isGap && visitedMap[y][x] != Field.GAP) {
            floodQueue.add(Pair(Pair(x, y + 1), false))
            floodQueue.add(Pair(Pair(x, y - 1), false))
            visitedMap[y][x] = Field.GAP
        }
    }

    fun part2(input: List<String>): Int {
        visitedMap.joinToString("\n") { row -> row.joinToString("") { it.symbol.toString() } }.println()
        println()
        println()
        //floodQueue.add(Pair(Pair(0, 0), false))
        //while (floodQueue.peek() != null) {
        //    floodFill()
        //}
        /*map = map.map { row ->
            row.map { pipe ->
                if (pipe == Pipe.fromSymbol('L')) Pipe.fromSymbol('7')
                else if (pipe == Pipe.fromSymbol('J')) Pipe.fromSymbol('F')
                else pipe
            }
        }*/

        visitedMap = visitedMap.mapIndexed { rowIndex, row ->
            var inside = false

            row.mapIndexed innerMap@{ column, field ->
                if (field == Field.LOOP && (map[rowIndex].getOrNull(column) != Pipe.EW || map[rowIndex].getOrNull(column) != Pipe.fromSymbol(
                        'F'
                    ) || map[rowIndex].getOrNull(column) != Pipe.fromSymbol('7'))
                ) {
                    inside = !inside
                    return@innerMap field
                }
                if (field != Field.LOOP) {
                    return@innerMap if (inside) Field.INSIDE else Field.OUTSIDE
                } else return@innerMap field
            }.toMutableList()
        }.toMutableList()

        visitedMap.joinToString("\n") { row -> row.joinToString("") { it.symbol.toString() } }.println()

        return visitedMap.sumOf { row -> row.filter { it == Field.INSIDE }.size }
    }

    val input = readInput("day10")
    part1(input).println()
    part2(input).println()
}
