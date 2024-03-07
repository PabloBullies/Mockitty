package core

import Term
import mu.KotlinLogging
import net.bytebuddy.ByteBuddy
import net.bytebuddy.agent.ByteBuddyAgent
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy
import net.bytebuddy.implementation.MethodDelegation
import net.bytebuddy.matcher.ElementMatchers.any
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
        term.everyBlock.invoke()
        logger.debug { "End: everyBlock. Getting invocation" }
        lateinit var invocation: MethodInvocation
        try {
            invocation = MockInfoBase.getInstance().getLastInvocation()
        }
        catch (e: InterruptedException){
            //TODO: что-то сделать с этим безобразием
            println(e.message)
        }
        logger.debug { "Invocation getted: ${invocation.invokedMethod.name}" +
                "(${invocation.arguments.joinToString { it?.toString() ?: "null" }})" }
        val rule = Rule({arguments -> invocation.arguments.contentEquals(arguments)}, term.returnsBlock)

        MockInfoBase.getInstance().addRule(invocation.mock, invocation.invokedMethod, rule)
        //createdObject.add("1")
    }

}