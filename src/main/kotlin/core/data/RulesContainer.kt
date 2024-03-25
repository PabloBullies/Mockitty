package core.data

import core.matching.Rule
import java.lang.reflect.Method
import java.util.*

class RulesContainer {
    private val rulesContainer: IdentityHashMap<Any?, HashMap<Method, LinkedList<Rule<Any>>>> = IdentityHashMap()

    @Suppress("UNCHECKED_CAST")
    fun <T> addRule(mock: Any?, method: Method, rule: Rule<T>) {
        if (rulesContainer[mock] == null) rulesContainer[mock] = LinkedHashMap()
        if (rulesContainer[mock]!![method] == null) rulesContainer[mock]!![method] = LinkedList()
        (rulesContainer[mock]?.get(method) as LinkedList<Rule<T>>).addFirst(rule)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> getRules(mock: Any?, method: Method): List<Rule<T>> {
        if (rulesContainer[mock] == null) {
            return LinkedList()
        }
        if (rulesContainer[mock] == null ||
            rulesContainer[mock]!![method] == null
        )
            return LinkedList()
        return rulesContainer[mock]?.get(method) as List<Rule<T>>
    }
}