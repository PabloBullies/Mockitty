package core.data

import java.rmi.UnexpectedException
import java.util.concurrent.LinkedBlockingDeque

class InvocationContainer {
    private val invocationContainer: LinkedBlockingDeque<MethodInvocation> = LinkedBlockingDeque()

    fun addFirst(invocation: MethodInvocation) {
        invocationContainer.putFirst(invocation)
    }

    fun peekFirst(): MethodInvocation {
        return invocationContainer.peekFirst()
    }

    fun toList(): List<MethodInvocation> {
        return ArrayList(invocationContainer.toList())
    }

    fun removeAll(verifyInvocations: MutableList<MethodInvocation>) {
        for (invocation in verifyInvocations) {
            if (invocationContainer.removeFirst() != invocation) {
                throw UnexpectedException("unexpected container state")
            }
        }
    }
}