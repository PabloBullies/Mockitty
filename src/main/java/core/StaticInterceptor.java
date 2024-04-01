package core;

import core.data.MethodInvocation;
import core.data.MockInfoBase;
import core.matching.Rule;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.implementation.bytecode.assign.Assigner;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static core.UtilsKt.getDefaultValue;

public class StaticInterceptor {

    @Advice.OnMethodEnter(skipOn = Object.class)
    private static Object enter(
            @Advice.Origin Method invokedMethod,
            @Advice.AllArguments Object[] arguments
    ) {
        return 0;
    }

    @Advice.OnMethodExit
    private static void exit(
            @Advice.Origin Method invokedMethod,
            @Advice.AllArguments Object[] arguments,
            @Advice.Return(readOnly = false, typing = Assigner.Typing.DYNAMIC) Object value
    ) {
        MockInfoBase.getInstance().getInvocationContainer().addFirst(new MethodInvocation(null, invokedMethod, arguments));
        List<Rule<Object>> rules = MockInfoBase.getInstance().getRules().getRules(null, invokedMethod);
        for (Rule<Object> rule : rules) {
            if (rule.apply(Arrays.stream(arguments).toArray())) {
                value = rule.getResult().invoke();
                return;
            }
        }
        value = getDefaultValue(invokedMethod.getReturnType());
    }
}
