package core

data class Rule<T>(val condition:(arguments: Array<Any?>)->Boolean, val result: () -> T) {
    fun isConditionMet(arguments: Array<Any?>): Boolean = condition.invoke(arguments)
}