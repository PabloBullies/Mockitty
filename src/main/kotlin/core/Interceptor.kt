package core

import mu.KotlinLogging
import net.bytebuddy.implementation.bind.annotation.AllArguments
import net.bytebuddy.implementation.bind.annotation.Origin
import net.bytebuddy.implementation.bind.annotation.RuntimeType
import net.bytebuddy.implementation.bind.annotation.This
import java.lang.reflect.Method

class Interceptor {

    companion object {

        private val logger = KotlinLogging.logger {}
        @RuntimeType
        @JvmStatic
        fun intercept(
            @This mock: Any,
            @Origin invokedMethod: Method,
            @AllArguments arguments: Array<Any?>
        ): Any? {
            MockInfoBase.getInstance().logInvocation(MethodInvocation(mock, invokedMethod, arguments))
            val rules: List<Rule<Any>> = MockInfoBase.getInstance().getRules(mock, invokedMethod)
            logger.debug {"${invokedMethod.name}(...) - ${rules.toString()}"}
            for (rule in rules){
                if (rule.isConditionMet(arguments)) {
                    logger.debug {"Rule applied - returned ${rule.result.invoke()}"}
                    return rule.result.invoke()
                }
            }
            return getDefaultValue(invokedMethod.returnType)
        }

        private fun getDefaultValue(clazz: Class<*>): Any? {
            if (!clazz.isPrimitive) {
                return null
            }
            return when (clazz.name) {
                "boolean" -> false
                "byte", "char", "short", "int", "long", "float", "double" -> 0
                else -> null
            }
        }
    }

}