import core.MockittyCore

class Mockitty {
    companion object {
        val MOCKITTY_CORE = MockittyCore()
        inline fun <reified T> mock(): T {
            return MOCKITTY_CORE.mock(T::class.java)
        }

        inline fun <reified T> staticMock(method: String) {
            MOCKITTY_CORE.mockStaticMethod(T::class.java, method)
        }

        fun <T> every(block: () -> T): Term.TermBuilder<T> {
            return Term.builder<T>().every(block)
        }

        infix fun <T> Term.TermBuilder<T>.returns(block: () -> T) {
            this.returnsBlock = block
            val term: Term<T> = this.build()
            MOCKITTY_CORE.makeRule(term)
        }
    }
}
