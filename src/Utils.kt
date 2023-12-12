import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readLines

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/$name/input.txt").readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

// https://www.reddit.com/r/Kotlin/comments/isg16h/comment/g5fvsw3/?utm_source=share&utm_medium=web3x&utm_name=web3xcss&utm_term=1&utm_content=share_button
fun <T> Iterable<T>.combinations(length: Int): Sequence<List<T>> =
    sequence {
        val pool = this@combinations as? List<T> ?: toList()
        val n = pool.size
        if (length > n) return@sequence
        val indices = IntArray(length) { it }
        while (true) {
            yield(indices.map { pool[it] })
            var i = length
            do {
                i--
                if (i == -1) return@sequence
            } while (indices[i] == i + n - length)
            indices[i]++
            for (j in i + 1 until length) indices[j] = indices[j - 1] + 1
        }
    }
