package core.matching

import mu.KotlinLogging
import java.util.stream.IntStream

data class Rule<T>(val matchers: List<(Any?) -> Boolean>, val result: () -> T) {
    private val logger = KotlinLogging.logger {}
    fun apply(arguments: Array<Any?>): Boolean {
        for (i: Int in IntStream.range(0, arguments.size)) {
            val matchingResult = matchers[i](arguments[i])
            logger.debug { "arg $i: $matchingResult" }
            if (!matchingResult) return false
        }
        logger.debug { "result: $result " }
        return true
    }
}