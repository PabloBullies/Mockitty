package core.intercept

import core.data.MethodInvocation
import core.data.MockInfoBase
import mu.KotlinLogging
import net.bytebuddy.implementation.bind.annotation.AllArguments
import net.bytebuddy.implementation.bind.annotation.Origin
import net.bytebuddy.implementation.bind.annotation.RuntimeType
import net.bytebuddy.implementation.bind.annotation.This
import java.lang.reflect.Method

class SpyInterceptor : Interceptor {

    companion object : Interceptor.InterceptorCompanion {
        private val logger = KotlinLogging.logger {}

        @RuntimeType
        @JvmStatic
        override fun intercept(
            @This mock: Any,
            @Origin invokedMethod: Method,
            @AllArguments arguments: Array<Any?>
        ): Any? {
            MockInfoBase.getInstance().invocationContainer.addFirst(MethodInvocation(mock, invokedMethod, arguments))
            logger.debug { "spy ${invokedMethod.name}([${arguments.size}])" }
            val objectForSpy = MockInfoBase.getInstance().spy[mock]
            return invokedMethod(objectForSpy!!, *arguments)
        }
    }
}