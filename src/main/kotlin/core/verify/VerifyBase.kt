package core.verify

import core.data.MethodInvocation
import core.data.MockInfoBase
import mu.KotlinLogging

fun getVerifyInvocations(verifyBlock: () -> Any?): Pair<List<MethodInvocation>, List<MethodInvocation>> {
    val beforeVerifyBlockInvocations = ArrayList(MockInfoBase.getInstance().invocationContainer.toList())
    verifyBlock.invoke()
    val afterVerifyBlockInvocations = ArrayList(MockInfoBase.getInstance().invocationContainer.toList())

    var info = "\nBefore Invocation:\n"
    for ((i, invocation) in beforeVerifyBlockInvocations.reversed().withIndex())
        info += "${i}:  ${invocation.invokedMethod!!.declaringClass}().${invocation.invokedMethod.name}(${
            invocation.arguments!!.joinToString(
                separator = ", "
            )
        })\n"
    info += "After Invocation:\n"
    for ((i, invocation) in afterVerifyBlockInvocations.reversed().withIndex())
        info += "${i}:  ${invocation.invokedMethod!!.declaringClass}().${invocation.invokedMethod.name}(${
            invocation.arguments!!.joinToString(
                separator = ", "
            )
        })\n"
    KotlinLogging.logger {}.debug { info }
    val checkingPart = afterVerifyBlockInvocations.size - beforeVerifyBlockInvocations.size
    val verifyInvocations = afterVerifyBlockInvocations.subList(0, checkingPart)
    MockInfoBase.getInstance().invocationContainer.removeAll(verifyInvocations)
    return Pair(beforeVerifyBlockInvocations, afterVerifyBlockInvocations)
}