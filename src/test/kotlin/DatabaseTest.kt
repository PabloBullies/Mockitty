import Mockitty.Companion.returns
import org.junit.jupiter.api.Test
import java.io.Closeable
import java.net.URL
import kotlin.test.assertEquals

abstract class Database<T>(url: URL) {
    abstract fun connect()
    abstract fun pull(): List<T>
    abstract fun push(collection: List<T>): Boolean
    abstract fun disconnect()
}

open class Repository<T>(private val database: Database<T>) : Closeable {
    init {
        database.connect()
    }

    open operator fun get(index: Int): T {
        return database.pull()[index]
    }

    open operator fun set(index: Int, value: T) {
        database.pull().toMutableList().also {
            it[index] = value
            database.push(it)
        }
    }

    override fun close() {
        database.disconnect()
    }
}

class ExampleTest {

    @Test
    fun simpleTest() {

        val database = Mockitty.mock<Database<Int>>()

        val repository = Mockitty.spy(Repository(database))

        Mockitty.every {
            database.pull()
        } returns { listOf(1, 2, 3, 4, 5) }

        assertEquals(1, repository.use { it[0] })

        verify(exactly = 1) {
            database.connect()
        }
        verify(exactly = 1) {
            repository.close()
        }
    }
}
