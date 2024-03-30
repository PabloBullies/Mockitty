import core.verify.VerificationFailedException
import core.verify.getVerifyInvocations
import java.util.stream.IntStream.range

private fun countInvocations(verifyBlock: () -> Any?): Int {
    val invocations = getVerifyInvocations(verifyBlock)
    val checkingPart = invocations.second.size - invocations.first.size
    val beforeVerifyBlockInvocations = invocations.first
    val afterVerifyBlockInvocations = invocations.second.subList(0, checkingPart)
    var pivot = 0
    var result = 0
    for (i in range(0, beforeVerifyBlockInvocations.size)) {
        if (beforeVerifyBlockInvocations[i].hashCode() == afterVerifyBlockInvocations[pivot].hashCode()) {
            if (pivot + 1 == checkingPart){
                pivot=0
                result++
            }
            else pivot++
        }
    }
    return result
}

fun verify(atLeast: Int, atMost: Int, verifyBlock: () -> Any?) {
    val result = countInvocations(verifyBlock)
    if ((result < atLeast) or (atMost < result)) throw VerificationFailedException("Method invocations number must be in range($atLeast, $atMost). Counted: $result")
}

fun verify(verifyBlock: () -> Any?) {
    val result = countInvocations(verifyBlock)
    if (result < 1) throw VerificationFailedException("Method invocations number must be 1 or bigger. Counted: $result")
}

fun verify(exactly: Int, verifyBlock: () -> Any?) {
    val result = countInvocations(verifyBlock)
    if (result != exactly) throw VerificationFailedException("Method invocations number must be $exactly. Counted: $result")
}

fun verifySeq(verifyBlock: () -> Any?) {
    val invocations = getVerifyInvocations(verifyBlock)
    val checkingPart = invocations.second.size - invocations.first.size
    val beforeVerifyBlockInvocations = invocations.first.subList(0, checkingPart)
    val afterVerifyBlockInvocations = invocations.second.subList(0, checkingPart)
    for (i in range(0, checkingPart)) {
        if (afterVerifyBlockInvocations[i].hashCode() != beforeVerifyBlockInvocations[i].hashCode())
            throw VerificationFailedException("Unexpected sequence of invocations")
    }
}