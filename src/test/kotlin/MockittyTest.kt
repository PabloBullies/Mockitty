import kotlin.test.Test
import kotlin.test.assertEquals

class MockittyTest {

    @Test
    fun testMock() {
        val expected: List<String> = ArrayList()
        val list = Mockitty.mock<ArrayList<String>>()
        assertEquals(expected.isEmpty(), list.isEmpty())
    }
}