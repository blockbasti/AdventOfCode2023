package day17

import println
import readInput
import java.util.*

private data class Node(
    val x: Int = 0,
    val y: Int = 0,
    var predecessor: Node? = null,
    var score: Int = Int.MAX_VALUE,
    var g: Int = Int.MAX_VALUE,
    val h: Int = x + y
) {
    override fun equals(other: Any?): Boolean {
        if (other is Node) {
            return this.x == other.x && this.y == other.y
        }
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return x * 1000 + y
    }
}

fun main() {
    fun isValidPosition(x: Int, y: Int, input: List<String>): Boolean {
        return x >= 0 && y >= 0 && x < input.first().length && y < input.size
    }

    fun part1(input: List<String>): Int {
        val endNode = Node(input.first().length - 1, input.size - 1)
        var lastNode: Node?
        val openQueue: PriorityQueue<Node> = PriorityQueue(Comparator.comparingInt { it.score })
        val closedList = mutableSetOf<Node>()

        openQueue.add(Node(0, 0, g = 0))

        do {
            val currentNode = openQueue.poll()

            if (currentNode == endNode) {
                lastNode = currentNode
                break
            }

            closedList.add(currentNode)

            // expand Node

            val successors = mutableListOf<Node>()
            if (isValidPosition(currentNode.x - 1, currentNode.y, input)) successors.add(
                Node(
                    currentNode.x - 1,
                    currentNode.y
                )
            )
            if (isValidPosition(currentNode.x + 1, currentNode.y, input)) successors.add(
                Node(
                    currentNode.x + 1,
                    currentNode.y
                )
            )
            if (isValidPosition(currentNode.x, currentNode.y - 1, input)) successors.add(
                Node(
                    currentNode.x,
                    currentNode.y - 1
                )
            )
            if (isValidPosition(currentNode.x, currentNode.y + 1, input)) successors.add(
                Node(
                    currentNode.x,
                    currentNode.y + 1
                )
            )

            successors.forEach { successor ->
                if (closedList.contains(successor)) return@forEach

                val tenative_g = currentNode.g + input[currentNode.y][currentNode.x].digitToInt()

                if (openQueue.contains(successor) && tenative_g >= successor.g) return@forEach

                successor.predecessor = currentNode
                successor.g = tenative_g
                successor.score = tenative_g + currentNode.g

                openQueue.remove(successor)
                openQueue.add(successor)
            }

        } while (openQueue.isNotEmpty())

        var path = 0
        return 0
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    val input = readInput("day17")
    part1(input).println()
    part2(input).println()
}
