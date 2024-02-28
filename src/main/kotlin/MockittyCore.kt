import net.bytebuddy.ByteBuddy
import net.bytebuddy.implementation.FixedValue
import net.bytebuddy.matcher.ElementMatchers
import net.bytebuddy.matcher.ElementMatchers.any


class MockittyCore {
    private var createdObject = ArrayList<String>()
    fun <T> mock(classToMock: Class<T>): T {
        val mock = ByteBuddy()
            .subclass(classToMock)
            .method(ElementMatchers.returns(Any::class.java))
            .intercept(FixedValue.nullValue())
            .make()
            .load(javaClass.getClassLoader())
            .loaded
            .getDeclaredConstructor()
            .newInstance();
        return mock as T
    }

    fun every(block: Any) {
        //TODO: every
    }

    fun returns(block: () -> Any?) {
        //TODO: returns
        createdObject.add("1")
    }

}