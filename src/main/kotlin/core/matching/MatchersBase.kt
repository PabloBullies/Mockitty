package core.matching

import Mockitty
import core.MockInfoBase

fun getDefaultValue(clazz: Class<*>): Any? {
    return when (clazz.simpleName) {
        "Boolean" -> false
        "Byte", "Character", "Short", "Integer", "Long", "Float", "Double" -> 0
        else -> null
    }
}

inline fun <reified T> match(noinline matcherFunction: (T?) -> Boolean): T {
    MockInfoBase.getInstance().logMatcher(matcherFunction)
    var result = getDefaultValue(T::class.java)
    if (result == null) {
        result = Mockitty.mock<T>()
    }
    return result as T
}
