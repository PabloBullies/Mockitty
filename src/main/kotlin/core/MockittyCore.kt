package core

import net.bytebuddy.ByteBuddy
import net.bytebuddy.agent.ByteBuddyAgent
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy
import net.bytebuddy.implementation.MethodDelegation
import net.bytebuddy.matcher.ElementMatchers.any
import org.objenesis.ObjenesisStd


class MockittyCore {
    private var createdObject = ArrayList<String>()
    fun <T> mock(classToMock: Class<T>): T {
        ByteBuddyAgent.install()
        val mock = ByteBuddy()
            .subclass(classToMock)
            .method(any())
            .intercept(MethodDelegation.to(Interceptor::class.java))
            .make()
            .load(javaClass.getClassLoader(), ClassLoadingStrategy.Default.WRAPPER)
            .loaded
        val objenesis = ObjenesisStd()
        val result: T = objenesis.newInstance(mock)
        return result
    }

    fun every(block: Any) {
        //TODO: every
    }

    fun returns(block: () -> Any?) {
        //TODO: returns
        createdObject.add("1")
    }

}