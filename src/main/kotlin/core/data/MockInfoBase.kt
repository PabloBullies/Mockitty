package core.data

import java.util.*
import java.util.concurrent.LinkedBlockingDeque

class MockInfoBase {

    val rules: RulesContainer = RulesContainer()
    val spy: IdentityHashMap<Any, Any> = IdentityHashMap()
    val invocationContainer: InvocationContainer = InvocationContainer()
    private val matchersInvocationContainer: LinkedBlockingDeque<(Any?) -> Boolean> = LinkedBlockingDeque()

    companion object {
        @Volatile
        @JvmStatic
        private var INSTANCE: MockInfoBase? = null

        @JvmStatic
        fun getInstance(): MockInfoBase {
            if (INSTANCE == null) {
                INSTANCE = MockInfoBase()
            }
            return INSTANCE!!
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> logMatcher(matcherFunction: (T?) -> Boolean) {
        (matchersInvocationContainer as LinkedBlockingDeque<(T?) -> Boolean>).addLast(matcherFunction)
    }

    fun getMatchers(): List<(Any?) -> Boolean> {
        return matchersInvocationContainer.toList()
    }

    fun clearMatchers() {
        matchersInvocationContainer.clear()
    }
}