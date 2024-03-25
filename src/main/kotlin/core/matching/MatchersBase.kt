package core.matching

import Mockitty
import core.data.MockInfoBase
import core.getDefaultValue


inline fun <reified T> match(noinline matcherFunction: (T?) -> Boolean): T {
    MockInfoBase.getInstance().logMatcher(matcherFunction)
    var result = getDefaultValue(T::class.java)
    if (result == null) {
        result = Mockitty.mock<T>()
    }
    return result as T
}
