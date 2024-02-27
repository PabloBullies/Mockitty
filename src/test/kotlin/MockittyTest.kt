
import Mockitty.Companion.returns
import kotlin.test.Test
import kotlin.test.assertEquals

class MockittyTest {

    @Test
    fun testMock() {
        val expected: List<String> = ArrayList()
        val list = Mockitty.mock<ArrayList<String>>()
        assertEquals(expected.isEmpty(), list.isEmpty())
    }
    @Test
    fun testEveryReturns(){
        val list = Mockitty.mock<ArrayList<String>>()
        Mockitty.every { list.isEmpty() } returns { 1 }

    }
}