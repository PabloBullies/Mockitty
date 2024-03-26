package core.intercept

import net.bytebuddy.implementation.bind.annotation.AllArguments
import net.bytebuddy.implementation.bind.annotation.Origin
import net.bytebuddy.implementation.bind.annotation.SuperCall
import net.bytebuddy.implementation.bind.annotation.This
import java.lang.reflect.Method
import java.util.concurrent.Callable

interface Interceptor {
    companion object interface InterceptorCompanion {
        fun intercept(
            @This mock: Any,
            @Origin invokedMethod: Method,
            @AllArguments arguments: Array<Any?>,
            @SuperCall callable: Callable<*>
        ): Any?
    }
}