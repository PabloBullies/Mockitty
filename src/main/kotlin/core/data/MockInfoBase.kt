package core.data

import java.util.*
import java.util.concurrent.LinkedBlockingDeque

class MockInfoBase {

    val rules: RulesContainer = RulesContainer()
    private val invocationContainer: LinkedBlockingDeque<MethodInvocation> = LinkedBlockingDeque()
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

    fun logInvocation(methodInvocation: MethodInvocation) {
        invocationContainer.addFirst(methodInvocation)
        if (invocationContainer.size > 10) {
            invocationContainer.removeLast()
        }
    }

    @Throws(InterruptedException::class)
    fun getLastInvocation(): MethodInvocation {
        return invocationContainer.takeFirst()
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