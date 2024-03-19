class Term<T> private constructor(val everyBlock: () -> T, val returnsBlock: () -> T) {
    companion object {
        fun <T> builder() = TermBuilder<T>()
    }

    class TermBuilder<T> {
        private lateinit var everyBlock: () -> T
        internal lateinit var returnsBlock: () -> T
        fun build() = Term(everyBlock, returnsBlock)
        fun every(everyBlock: () -> T): TermBuilder<T> {
            this.everyBlock = everyBlock
            return this
        }
    }
}