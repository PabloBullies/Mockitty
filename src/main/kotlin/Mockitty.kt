import core.MockittyCore

class Mockitty {
    companion object {
        val MOCKITTY_CORE = MockittyCore()
        inline fun <reified T> mock(): T {
            return MOCKITTY_CORE.mock(T::class.java)
        }
        fun <T> every(block: Mock<T>.() -> T) {
            MOCKITTY_CORE.every(block)
        }

        infix fun Unit.returns(block: () -> Any?) {
            MOCKITTY_CORE.returns(block)
        }

    }

}
