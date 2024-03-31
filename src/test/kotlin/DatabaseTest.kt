import Mockitty.Companion.returns
import org.junit.jupiter.api.Test
import java.io.Closeable
import java.net.URL
import kotlin.test.assertEquals

abstract class RemoteList<T>(url: URL) {
    abstract fun connect()
    abstract fun pull(): List<T>
    abstract fun push(collection: List<T>): Boolean
    abstract fun disconnect()
}

open class Database<DataT>(private val connector: RemoteList<DataT>) : Closeable {
    init {
        connector.connect()
    }

    open operator fun get(index: Int): DataT {
        return connector.pull()[index]
    }

    open operator fun set(index: Int, value: DataT) {
        connector.pull().toMutableList().also {
            it[index] = value
            connector.push(it)
        }
    }

    override fun close() {
        connector.disconnect()
    }
}

class ExampleTest {

    @Test
    fun simpleTest() {

        val remoteList = Mockitty.mock<RemoteList<Int>>()
        val database = Mockitty.spy(Database(remoteList))

        Mockitty.every {
            remoteList.pull()
        } returns { listOf(1, 2, 3, 4, 5) }

        assertEquals(1, database[0])
        verify(exactly = 1) {
            database[0]
        }

        verify(exactly = 1) {
            remoteList.connect()
        }
    }
}
