package core

import net.bytebuddy.implementation.bind.annotation.AllArguments
import net.bytebuddy.implementation.bind.annotation.Origin
import net.bytebuddy.implementation.bind.annotation.RuntimeType
import net.bytebuddy.implementation.bind.annotation.This
import java.lang.reflect.Method

class Interceptor {


    companion object {

        @RuntimeType
        @JvmStatic
        fun intercept(
            @This mock: Any?,
            @Origin invokedMethod: Method,
            @AllArguments arguments: Array<Any?>?
        ): Any? {
            //Работа с мапой
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