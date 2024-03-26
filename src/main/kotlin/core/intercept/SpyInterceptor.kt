package core.intercept

import core.data.MockInfoBase
import mu.KotlinLogging
import net.bytebuddy.implementation.bind.annotation.*
import java.lang.reflect.Method
import java.util.concurrent.Callable

class SpyInterceptor: Interceptor{

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
            println( "===Spying method: ${invokedMethod.name}(..${arguments.size}..)===" )
            val objectForSpy = MockInfoBase.getInstance().spy[mock]
            return invokedMethod(objectForSpy!!, *arguments)
        }

    }
}