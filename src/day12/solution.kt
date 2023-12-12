package day12

import combinations
import day12.Record
import println
import readInput
import kotlin.time.measureTime


private typealias Record = Pair<String, List<Int>>

fun main() {
    val cache = HashMap<Record, Long>()

    fun satisfiesCounts(record: Record): Boolean {
        //if (cacheBool.get(record) != null) return cacheBool.get(record)!!
        val (springs, counts) = record
        val groups = springs
            .split(".")
            .filter { it.isNotBlank() }
            .map { it.length }

        if (groups.size != counts.size) {
            return false
        }

        val result = groups
            .zip(counts)
            .all { it.first == it.second }

        return result
    }

    fun couldSatisfyCount(springs: String, counts: List<Int>): Boolean {
        val result = springs
            .split(".")
            .filter { it.isNotBlank() }
            .takeWhile { !it.contains('?') }
            .map { it.length }
            .zip(counts)
            .all { it.first == it.second }

        return result
    }

    fun canInsertGroup(old: String, new: String): Boolean {
        return old.zip(new).all {
            when (it.first) {
                '?' -> it.second == '#' || it.second == '.'
                '#' -> it.second == '#'
                '.' -> it.second == '.'
                else -> false
            }
        }

    }

    fun generatePermutationsOptimized(record: Record): List<String> {
        val (springs, counts) = record

        val springGroups = counts.map { "#".repeat(it) }

        val opts =
            (0..(counts.size + (springs.length - counts.joinToString("").length - 1))).combinations(springGroups.size)

        val new = opts.map { opt ->
            var newSpring = springs
            opt.forEachIndexed { index, i ->
                newSpring = newSpring.replaceRange(
                    i,
                    (i + springGroups[index].length).coerceAtMost(springs.length),
                    springGroups[index]
                )
            }
            return@map newSpring.substring(springs.indices)
        }
            .map { it.replace('?', '.') }
            //.also { it.toList().println() }
            .filter { canInsertGroup(springs, it) && satisfiesCounts(it to record.second) }
            .distinct()
            .toList()

        return new
    }

    fun countPermutations(record: Record): Long {
        val (springs, counts) = record
        /*if (cache.contains(record)) {
            return cache[record]!!
        }*/

        if (springs.isBlank()) {
            return if (counts.isEmpty()) 1 else 0
        }

        val permutations: Long

        when (springs.first()) {
            '.' -> permutations = countPermutations(springs.substring(1) to counts)
            '?' -> permutations = countPermutations(".".plus(springs.substring(1)) to counts) +
                    countPermutations("#".plus(springs.substring(1)) to counts)

            else -> {
                if (counts.isEmpty()) permutations = 0
                else {
                    val damaged = counts.first()
                    if (damaged <= springs.length &&
                        springs.chars().limit(damaged.toLong()).allMatch { it.toChar() == '#' || it.toChar() == '?' }
                    ) {
                        val newCounts = counts.subList(1, counts.size)
                        permutations = when {
                            damaged == springs.length -> if (newCounts.isEmpty()) 1 else 0
                            springs[damaged] == '#' -> countPermutations(springs.substring(damaged + 1) to newCounts)
                            springs[damaged] == '?' -> countPermutations(".".plus(springs.substring(damaged + 1)) to newCounts)
                            else -> 0
                        }
                    } else {
                        permutations = 0
                    }
                }
            }

        }
        cache[record] = permutations

        return permutations
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

    fun countValidPermutations(record: Record): Long {
        val (springs, counts) = record
        if (cache.containsKey(record)) {
            return cache.get(record)!!
        }

        if (springs.isBlank()) {
            // We have processed the complete condition, so if there are no groups left we
            // have a valid permutation, otherwise a non-valid one.
            return if (springs.isEmpty()) 1 else 0
        }

        val firstChar: Char = springs.get(0)
        var permutations: Long = 0
        if (firstChar == '.') {
            // Working spring, so just skip over it and continue.
            permutations = countValidPermutations(springs.substring(1) to counts)
        } else if (firstChar == '?') {
            // Spring of unknown status, so count the permutations for both a working and a
            // damaged spring.
            permutations = (countPermutations("." + springs.substring(1) to counts)
                    + countPermutations("#" + springs.substring(1) to counts))
        } else {
            // First character is #
            if (counts.isEmpty()) {
                // No more groups but we still have a #, this is not valid.
                permutations = 0
            } else {
                val nrDamaged: Int = counts.get(0)
                // We want to include a group of nrDamaged springs, so the remaining length of
                // the condition should be at least the size of that group. Also, the next
                // nrDamaged springs should all be damaged (#) or unknown (?).
                if (nrDamaged <= springs.length
                    && springs.chars().limit(nrDamaged.toLong())
                        .allMatch { c -> c.toChar() === '#' || c.toChar() === '?' }
                ) {
                    val newGroups: List<Int> = counts.subList(1, counts.size)
                    permutations = if (nrDamaged == springs.length) {
                        // The remaining length of the condition is equal to the nr of damaged springs
                        // in the group, so we are done if there are no remaining groups.
                        (if (newGroups.isEmpty()) 1 else 0).toLong()
                    } else if (springs.get(nrDamaged) === '.') {
                        // We have just added a group of damaged springs (#) and the next spring is
                        // operational, this is valid, so skip over that operational spring.
                        countValidPermutations(springs.substring(nrDamaged + 1) to newGroups)
                    } else if (springs.get(nrDamaged) === '?') {
                        // We have just added a group of damaged springs (#), so the next spring can
                        // only be operational (.).
                        countValidPermutations("." + springs.substring(nrDamaged + 1) to newGroups)
                    } else {
                        // We have just added a group of damaged springs (#), but the next character is
                        // also a damaged spring, this is not valid.
                        0
                    }
                } else {
                    // Either size of the group is greater than the remaining length of the
                    // condition or the next nrDamaged springs are not either damaged (#) or unknown
                    // (?).
                    permutations = 0
                }
            }
        }
        cache.put(record, permutations)
        return permutations
    }


    fun part1(input: List<String>): Long {
        val records: List<Record> = input.map { line ->
            val (springs, counts) = line.split(" ")
            return@map springs to counts.split(",").map { it.toInt() }
        }

        return records.sumOf { record ->
            countValidPermutations(record)
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
                index++.println()
                countValidPermutations(record)
            }
        }.println()

        return sum
    }

    val input = readInput("day12")
    part1(input).println()
    println()
    //part2(input).println()
}
