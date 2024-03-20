package core

import Term
import core.matching.Rule
import mu.KotlinLogging
import net.bytebuddy.ByteBuddy
import net.bytebuddy.agent.ByteBuddyAgent
import net.bytebuddy.asm.Advice
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy
import net.bytebuddy.implementation.MethodDelegation
import net.bytebuddy.matcher.ElementMatchers.any
import net.bytebuddy.matcher.ElementMatchers.named
import org.objenesis.ObjenesisStd


class MockittyCore {
    private val logger = KotlinLogging.logger {}
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
    fun <T> makeRule(term: Term<T>) {
        logger.debug { "Start: everyBlock" }
        MockInfoBase.getInstance().clearMatchers()
        term.everyBlock()
        logger.debug { "End: everyBlock. Getting invocation" }
        val invocation = MockInfoBase.getInstance().getLastInvocation()
        var matchers: List<(Any?) -> Boolean> = MockInfoBase.getInstance().getMatchers()

        if ((matchers.isNotEmpty()) and (matchers.size != invocation.arguments.size)) {
            throw IllegalArgumentException("Matcher length must be equal arguments length or 0")
        }
        if (matchers.isEmpty()) {
            val argMatchers: ArrayList<(Any?) -> Boolean> = ArrayList()
            for (argument in invocation.arguments) {
                argMatchers.add { it == argument }
            }
            matchers = argMatchers
        }
        logger.debug {
            "Invocation getted: ${invocation.invokedMethod.name}" +
                    "(${invocation.arguments.joinToString { it?.toString() ?: "null" }})\n" +
                    "Matchers: $matchers"
        }

        val rule = Rule( matchers, term.returnsBlock)
        MockInfoBase.getInstance().addRule(invocation.mock, invocation.invokedMethod, rule)
    }

    fun <T> mockStaticMethod(targetClass: Class<T>, method: String) {
        ByteBuddyAgent.install()
        ByteBuddy()
            .redefine(targetClass)
            .visit(Advice.to(StaticInterceptor::class.java).on(named(method)))
            .make()
            .load(javaClass.getClassLoader(), ClassReloadingStrategy.fromInstalledAgent())
    }

}