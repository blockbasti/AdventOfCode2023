package day12

import day12.Record
import println
import readInput
import java.awt.SystemColor.window
import kotlin.time.measureTime

private typealias Record = Pair<String, List<Int>>

fun main() {

    fun satisfiesCounts(record: Record): Boolean {
        val (springs, counts) = record
        val groups = springs
            .split(".")
            .filter { it.isNotBlank() }
            .map { it.length }

        if (groups.size != counts.size)
            return false

        return groups
            .zip(counts)
            .all { it.first == it.second }
    }

    fun couldSatisfyCount(springs: String, counts: List<Int>): Boolean {
        val groups = springs
            .split(".")
            .filter { it.isNotBlank() }
            .takeWhile { !it.contains('?') }
            .map { it.length }

        return groups
            .zip(counts)
            .all { it.first == it.second }
    }

    fun canInsertGroup(old: String, new: String): Boolean {
        return old.zip(new).all {
            when (it.first) {
                '?' -> it.second == '#' || it.second =='.'
                '#' -> it.second == '#'
                '.' -> it.second == '.'
                else -> false
            }
        }

    }

    fun generatePermutationsOptimized(record:Record): List<String>{
        val (springs, counts) = record

        val springGroups = counts.map { "#".repeat(it).plus('.') }

        val result = springs.windowed(springGroups.first().length+1).filter { window -> canInsertGroup(window, springGroups.first().plus(window.substring(springGroups.first().length))) }

        return if (springs.contains('?')) {
            val first = generatePermutationsOptimized(springs.replaceFirst('?', '.') to counts)
            val second = generatePermutationsOptimized(springs.replaceFirst('?', '#') to counts)
            first.plus(second)
        } else listOf(springs)
    }

    fun generatePermutations(record: Record): List<String> {
        val (springs, counts) = record
        val couldSatisfyCount = couldSatisfyCount(springs, counts)
        return if (springs.contains('?') && couldSatisfyCount) {
            val first = generatePermutations(springs.replaceFirst('?', '.') to counts)
            val second = generatePermutations(springs.replaceFirst('?', '#') to counts)
            first.plus(second)
        } else listOf(springs)
    }

    fun countValidPermutations(record: Record): Int {
        val (springs, counts) = record

        return generatePermutationsOptimized(record)
            //.also { it.println() }
            .filter { !it.contains('?') && satisfiesCounts(it to counts) }
            .size
    }

    fun part1(input: List<String>): Long {
        val records: List<Record> = input.map { line ->
            val (springs, counts) = line.split(" ")
            return@map springs to counts.split(",").map { it.toInt() }
        }

        return records.sumOf { record ->
            countValidPermutations(record).toLong()
        }
    }

    fun part2(input: List<String>): Long {
        val records: List<Record> = input.map { line ->
            val (springs, counts) = line.split(" ")
            return@map springs.plus("?").repeat(5).removeSuffix("?") to List(5) {
                counts.split(",").map { it.toInt() }
            }.flatten()
        }
        var sum = 0L
        var index = 0
        measureTime {
            sum = records.sumOf { record ->
                println(index++)
                countValidPermutations(record).toLong()
            }
        }.println()

        return sum
    }

    val input = readInput("day12")
    part1(input).println()
    println()
    //part2(input).println()
}
