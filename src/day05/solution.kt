package day05

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import println
import readInput
import java.util.concurrent.atomic.AtomicLong

fun main() {
    fun createMappings(
        input: List<String>,
        invert: Boolean
    ): List<List<Pair<ClosedRange<Long>, Long>>> {
        val mappings: List<List<Pair<ClosedRange<Long>, Long>>> = mutableListOf()
        var currentMapping: List<Pair<ClosedRange<Long>, Long>> = mutableListOf()
        input.drop(3).forEach { line ->
            when {
                line.isBlank() -> {}
                line.contains("-to-") -> {
                    mappings.addLast(currentMapping)
                    currentMapping = mutableListOf()
                }

                else -> {
                    val (dest, source, length) = line.split(" ").map { it.toLong() }
                    currentMapping = if (!invert) currentMapping.plus(Pair(source..source + length, dest - source))
                    else currentMapping.plus(Pair(dest..dest + length, source - dest))

                }
            }
        }
        mappings.addLast(currentMapping)
        return if (!invert) mappings else mappings.reversed()
    }

    fun seedToLocation(
        seed: Long,
        mappings: List<List<Pair<ClosedRange<Long>, Long>>>
    ): Long {
        var currentValue = seed
        mappings.forEach mapping@{ map ->
            val mapping = map.find { it.first.contains(currentValue) }
            if (mapping != null) {
                currentValue += mapping.second // apply offset
                return@mapping
            }
        }
        return currentValue
    }

    fun part1(input: List<String>): Long {
        val mappings = createMappings(input, false)
        val seeds = input[0].split(":")[1].split(" ").filter { it.isNotBlank() }.map { it.toLong() }

        return seeds.minOf { seed ->
            seedToLocation(seed, mappings)
        }
    }

    fun part2(input: List<String>): Long {
        val mappings = createMappings(input, false)
        val inverseMappings = createMappings(input, true)
        val seeds = input[0].split(":")[1].split(" ").filter { it.isNotBlank() }.map { it.toLong() }
            .chunked(2).map { it.first()..<it.first() + it.last() }

        val minLocation = AtomicLong(Long.MAX_VALUE)
        /*for (location in 46 .. inverseMappings.first().maxOf { it.first.endInclusive }) {
            val seed = seedToLocation(location, inverseMappings)
            if(seeds.any {it.contains(seed)} && seedToLocation(seed, mappings) == location) {
                minLocation.set(Math.min(location, minLocation.get()))
            }
        }*/

        val coroutineCount = 24
        val location = AtomicLong(0L)
        val result = runBlocking {
            (0 until coroutineCount).map {
                async(Dispatchers.Default) {
                    var current = location.getAndIncrement()
                    var seed = seedToLocation(current, inverseMappings)
                    while (!seeds.any { range -> range.contains(seed) && seedToLocation(seed, mappings) == current }) {
                        current = location.getAndIncrement()
                        seed = seedToLocation(current, inverseMappings)
                    }
                    return@async current
                }
            }.awaitAll().min()
        }
        return result
    }

    val input = readInput("day05")
    part1(input).println()
    part2(input).println()
}
