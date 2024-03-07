
import Mockitty.Companion.returns
import kotlin.test.Test
import kotlin.test.assertEquals

class MockittyTest {

    @Test
    fun testMock() {
        val user = Mockitty.mock<User>()
        println(user.add(2, 3))
        println(user.hello())
    }
    @Test
    fun testEveryReturns(){
        val list = Mockitty.mock<ArrayList<String>>()
        Mockitty.every { list.isEmpty() } returns { false }
        list.add("1")
        assertEquals(false, list.isEmpty())

        Mockitty.every { list.hashCode() } returns { 2 }
        assertEquals(2, list.hashCode())

        val user = Mockitty.mock<User>()
        Mockitty.every { user.getName() } returns { "Jojo" }
        assertEquals("Jojo", user.getName())

        Mockitty.every { user.add(1,1) } returns { 3 }
        assertEquals(3, user.add(1,1))

        // переопределение родительских методов не работает?
        // Mockitty.every { list.getOrNull(0) } returns { "hello" }
        // assertEquals("hello", list.getOrNull(0))
    }
}