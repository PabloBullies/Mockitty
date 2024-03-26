package core.data

import java.lang.reflect.Method

data class MethodInvocation(val mock: Any?, val invokedMethod: Method?, val arguments: Array<Any?>?) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MethodInvocation

        if (mock != other.mock) return false
        if (invokedMethod != other.invokedMethod) return false
        if (!arguments.contentEquals(other.arguments)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = mock.hashCode()
        result = 31 * result + invokedMethod!!.hashCode()
        result = 31 * result + arguments!!.contentHashCode()
        return result
    }
}