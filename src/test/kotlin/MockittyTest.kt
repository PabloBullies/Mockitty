
import Mockitty.Companion.returns
import kotlin.test.Test
import kotlin.test.assertEquals

class MockittyTest {

    @Test
    fun testMock() {
        val user = Mockitty.mock<User>()
        user.hello()
    }
    @Test
    fun testEveryReturns(){
        val list = Mockitty.mock<ArrayList<String>>()
        Mockitty.every { list.isEmpty() } returns { false }
        assertEquals(false,list.isEmpty())

    }
}