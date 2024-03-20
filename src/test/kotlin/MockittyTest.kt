import Mockitty.Companion.returns
import core.MockittyCore
import kotlin.test.Test
import kotlin.test.assertEquals

class MockittyTest {

    @Test
    fun testEveryReturns() {
        val list = Mockitty.mock<ArrayList<String>>()
        Mockitty.every { list.isEmpty() } returns { false }
        assertEquals(false, list.isEmpty())

        Mockitty.every { list.hashCode() } returns { 2 }
        assertEquals(2, list.hashCode())

        val user = Mockitty.mock<User>()
        Mockitty.every { user.getName() } returns { "Jojo" }
        assertEquals("Jojo", user.getName())

        Mockitty.every { user.add(1,1) } returns { 3 }
        assertEquals(3, user.add(1,1))
        assertEquals(0, user.add(2,2))
    }

    @Test
    fun testStatic() {
        val core = MockittyCore()
        core.mockStaticMethod(User.Companion::class.java, "staticMethod")
        println(User.staticMethod())
        //core.mockStaticMethod(User::class.java, "add")
        //var user = User("a")
        //println(user.add(1, 1))
        //println(user.add(1, 1))
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