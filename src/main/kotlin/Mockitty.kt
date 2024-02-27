class Mockitty {
    companion object {
        val MOCKITTY_CORE = MockittyCore()
        inline fun <reified T> mock(): T {
            return MOCKITTY_CORE.mock(T::class.java)
        }

    }
}
