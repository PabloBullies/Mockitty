package core

import core.matching.Rule
import java.lang.reflect.Method
import java.util.*
import java.util.concurrent.LinkedBlockingDeque

class MockInfoBase{

    private val rulesContainer: IdentityHashMap<Any, HashMap<Method, LinkedList<Rule<Any>>>>  = IdentityHashMap()
    private val invocationContainer: LinkedBlockingDeque<MethodInvocation> = LinkedBlockingDeque()
    private val matchersInvocationContainer: LinkedBlockingDeque<(Any?) -> Boolean> = LinkedBlockingDeque()
    companion object {
        @Volatile
        @JvmStatic
        private var INSTANCE: MockInfoBase? = null

        @JvmStatic
        fun getInstance(): MockInfoBase {
            if (this.INSTANCE == null){
                this.INSTANCE = MockInfoBase()
            }
            return this.INSTANCE!!
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> addRule(mock: Any, method: Method, rule: Rule<T>){
        if (rulesContainer[mock] == null) rulesContainer[mock] = LinkedHashMap()
        if (rulesContainer[mock]!![method] == null) rulesContainer[mock]!![method] = LinkedList()
        (rulesContainer[mock]?.get(method) as LinkedList<Rule<T>>).addFirst(rule)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> getRules(mock: Any, method: Method): List<Rule<T>>{
        if (rulesContainer[mock] == null){
            return LinkedList()
        }
        if (rulesContainer[mock] == null ||
            rulesContainer[mock]!![method] == null)
            return LinkedList()
        return rulesContainer[mock]?.get(method) as List<Rule<T>>
    }

    fun logInvocation(methodInvocation: MethodInvocation){
        invocationContainer.addFirst(methodInvocation)
        if (invocationContainer.size > 10){
            // TODO: поправить
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
    fun getMatchers(): List<(Any?) -> Boolean>{
        return matchersInvocationContainer.toList()
    }
    fun clearMatchers(){
        matchersInvocationContainer.clear()
    }
}