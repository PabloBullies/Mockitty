package core

import Term
import core.data.MockInfoBase
import core.intercept.Interceptor
import core.intercept.MockInterceptor
import core.intercept.SpyInterceptor
import core.matching.Rule
import mu.KotlinLogging
import net.bytebuddy.ByteBuddy
import net.bytebuddy.agent.ByteBuddyAgent
import net.bytebuddy.asm.Advice
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy
import net.bytebuddy.implementation.MethodDelegation
import net.bytebuddy.matcher.ElementMatchers
import net.bytebuddy.matcher.ElementMatchers.any
import org.objenesis.ObjenesisStd


class MockittyCore {
    private val logger = KotlinLogging.logger {}
    fun <T> mock(classToMock: Class<T>): T {
        logger.info { "===Mocking===" }
        ByteBuddyAgent.install()
        val mock = makeInterceptableType(classToMock, MockInterceptor::class.java)
        val objenesis = ObjenesisStd()
        val result: T = objenesis.newInstance(mock)
        return result
    }

    fun <T> spy(objectForSpy: T, objectClass: Class<T>): T {
        logger.info { "===Spying===" }
        ByteBuddyAgent.install()
        val mock = makeInterceptableType(objectClass, SpyInterceptor::class.java)
        val spyObject: T = createObjectByType(mock)
        MockInfoBase.getInstance().spy[spyObject] = objectForSpy
        return spyObject
    }

    fun <T> mockStaticMethod(targetClass: Class<T>, method: String) {
        logger.info { "===StaticMocking===" }
        ByteBuddyAgent.install()
        ByteBuddy()
            .redefine(targetClass)
            .visit(Advice.to(StaticInterceptor::class.java).on(ElementMatchers.named(method)))
            .make()
            .load(javaClass.getClassLoader(), ClassReloadingStrategy.fromInstalledAgent())
    }


    fun <T> makeRule(term: Term<T>) {
        logger.info { "===Making Rule===" }
        logger.debug { "Start: everyBlock" }
        MockInfoBase.getInstance().clearMatchers()
        term.everyBlock()
        logger.debug { "End: everyBlock. Getting invocation" }
        val invocation = MockInfoBase.getInstance().getLastInvocation()
        var matchers: List<(Any?) -> Boolean> = MockInfoBase.getInstance().getMatchers()

        if ((matchers.isNotEmpty()) and (matchers.size != invocation.arguments.size)) {
            logger.error { "IllegalArgumentException: Matcher length must be equal arguments length or 0" }
            throw IllegalArgumentException("Matcher length must be equal arguments length or 0")
        }
        if (matchers.isEmpty()) {
            val argMatchers: ArrayList<(Any?) -> Boolean> = ArrayList()
            for (argument in invocation.arguments) {
                argMatchers.add { it == argument }
            }
            matchers = argMatchers
        }
        logger.info {
            "Invocation getted: ${invocation.invokedMethod.name}" +
                    "(${invocation.arguments.joinToString { it?.toString() ?: "null" }})\n" +
                    "Matchers: $matchers"
        }
        val rule = Rule(matchers, term.returnsBlock)
        MockInfoBase.getInstance().rules.addRule(invocation.mock, invocation.invokedMethod, rule)
    }

    private fun <T> createObjectByType(mock: Class<out T>): T {
        val objenesis = ObjenesisStd()
        val spyObject: T = objenesis.newInstance(mock)
        return spyObject
    }

    private fun <T> makeInterceptableType(
        objectClass: Class<T>,
        interceptorClass: Class<out Interceptor>
    ): Class<out T> = ByteBuddy()
        .subclass(objectClass)
        .method(any())
        .intercept(MethodDelegation.to(interceptorClass))
        .make()
        .load(javaClass.getClassLoader(), ClassLoadingStrategy.Default.WRAPPER)
        .loaded
}