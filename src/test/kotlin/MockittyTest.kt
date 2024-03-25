import Mockitty.Companion.returns
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class MockittyTest {

    @Test
    fun testEveryReturns() {
        val list = Mockitty.mock<ArrayList<String>>()
        Mockitty.every { list.isEmpty() } returns { false }
        assertEquals(false, list.isEmpty())

        Mockitty.every { list.hashCode() } returns { 2 }
        assertEquals(2, list.hashCode())

        val user = Mockitty.mock<User>()
        assertNull(user.getName())
        Mockitty.every { user.getName() } returns { "Jojo" }
        assertEquals("Jojo", user.getName())

        Mockitty.every { user.add(1, 1) } returns { 3 }
        assertEquals(3, user.add(1, 1))
        assertEquals(0, user.add(2, 2))
    }

    @Test
    fun testStatic() {
        assertEquals(User.staticMethod(), "STATIC")
        Mockitty.mock<User.Companion>("staticMethod")
        assertNull(User.staticMethod())
        Mockitty.every { User.staticMethod() } returns { "static" }
        assertEquals(User.staticMethod(), "static")


        assertEquals(User.countAge(2003),21)
        Mockitty.mock<User.Companion>("countAge")
        assertEquals(User.countAge(2003),0)
        Mockitty.every { User.countAge(any()) } returns { 1984 }
        assertEquals(User.countAge(2003), 1984)
        assertEquals(User.countAge(2024), 1984)
        assertEquals(User.countAge(1984), 1984)


    }

    @Test
    fun testMatchers() {
        val user = Mockitty.mock<User>()

        Mockitty.every { user.addFriend(any()) } returns { true }
        assertEquals(user.addFriend(User("Borya")), true)

        Mockitty.every { user.add(1, 1) } returns { 2 }
        assertEquals(user.add(1, 1), 2)
        assertEquals(user.add(1, 2), 0)

        Mockitty.every { user.add(any(), eq(2)) } returns { 10 }
        assertEquals(user.add(1, 2), 10)
        assertEquals(user.add(2, 2), 10)
        assertEquals(user.add(3, 2), 10)
        assertEquals(user.add(1, 1), 2)
        assertEquals(user.add(1, 0), 0)
    }
}