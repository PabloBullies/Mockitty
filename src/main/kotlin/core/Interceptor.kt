package core

import net.bytebuddy.implementation.bind.annotation.Argument
import net.bytebuddy.implementation.bind.annotation.RuntimeType

class Interceptor {

    companion object {

        @RuntimeType
        fun add(
            @Argument(0) x: Int,
            @Argument(1) y: Int
        ): Int {
            println("aboba1")
            return 10
        }
    }
}