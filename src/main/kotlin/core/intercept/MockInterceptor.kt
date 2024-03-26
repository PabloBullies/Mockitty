package core.intercept

import core.data.MethodInvocation
import core.data.MockInfoBase
import core.getDefaultValue
import core.matching.Rule
import mu.KotlinLogging
import net.bytebuddy.implementation.bind.annotation.*
import java.lang.reflect.Method
import java.util.concurrent.Callable

class MockInterceptor: Interceptor {

    companion object: Interceptor.InterceptorCompanion {

        private val logger = KotlinLogging.logger {}

        @RuntimeType
        @JvmStatic
        override fun intercept(
            @This mock: Any,
            @Origin invokedMethod: Method,
            @AllArguments arguments: Array<Any?>,
            @SuperCall callable: Callable<*>
        ): Any? {
            MockInfoBase.getInstance().logInvocation(MethodInvocation(mock, invokedMethod, arguments))
            val rules: List<Rule<Any>> = MockInfoBase.getInstance().rules.getRules(mock, invokedMethod)
            logger.debug {
                "Checking rules for: ${invokedMethod.name}(...)\n" +
                        "Rules: $rules"
            }
            for (rule in rules) {
                if (rule.apply(arguments)) {
                    logger.info { "Rule applied - returned ${rule.result()}" }
                    return rule.result()
                }
            }
            logger.info { "Valid rules not found - returned default value" }
            return getDefaultValue(invokedMethod.returnType)
        }

    }

}