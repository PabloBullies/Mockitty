
import Mockitty.Companion.every
import Mockitty.Companion.mock
import Mockitty.Companion.returns
import ch.qos.logback.classic.Level
import ch.qos.logback.classic.LoggerContext
import core.matching.match
import core.verify.VerificationFailedException
import mu.KLogger
import mu.KotlinLogging
import org.junit.jupiter.api.assertDoesNotThrow
import org.slf4j.LoggerFactory
import java.io.File
import java.net.URLClassLoader
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class MockittyTest {
    companion object {
        private fun setLogger(logLevel: Level): KLogger {
            val context = LoggerFactory.getILoggerFactory() as LoggerContext

            val rootLogger = context.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME)
            rootLogger.level = logLevel
            val logger = KotlinLogging.logger {}
            logger.info { "========= Start logging =========" }
            return logger
        }

        val logger = setLogger(Level.ERROR)
    }


    @Test
    fun testEveryReturns() {

        val list = mock<List<String>>()
        every { list.isEmpty() } returns { false }
        assertEquals(false, list.isEmpty())

        every { list[5] } returns { "ABOBA" }
        assertEquals("ABOBA", list[5])

        every { list[match { it!! % 2 == 0}] } returns { "EVEN" }
        assertEquals("ABOBA", list[5])
        assertEquals("EVEN", list[4])
        assertNull(list[3])

        every { list.hashCode() } returns { 2 }
        assertEquals(2, list.hashCode())

        val user = mock<User>()
        assertNull(user.getName())
        every { user.getName() } returns { "Jojo" }
        assertEquals("Jojo", user.getName())

        every { user.add(1, 1) } returns { 3 }
        assertEquals(3, user.add(1, 1))
        assertEquals(0, user.add(2, 2))
    }

    @Test
    fun testClassLoader() {

        val classLoader = URLClassLoader(arrayOf(File("test.jar").toURI().toURL()))
        val loadClass = classLoader.loadClass("Bean")
        val mock = mock(loadClass)
        assertNull(mock.toString())

    }

    @Test
    fun testStatic() {
        assertEquals(User.staticMethod(), "STATIC")
        mock<User.Companion>(User.Companion::staticMethod)
        assertNull(User.staticMethod())
        every { User.staticMethod() } returns { "static" }
        assertEquals(User.staticMethod(), "static")


        assertEquals(User.countAge(2003), 21)
        mock<User.Companion>(User.Companion::countAge)
        assertEquals(User.countAge(2003), 0)
        every { User.countAge(any()) } returns { 1984 }
        assertEquals(User.countAge(2003), 1984)
        assertEquals(User.countAge(2024), 1984)
        assertEquals(User.countAge(1984), 1984)
    }

    @Test
    fun testMatchers() {
        val user = mock<User>()



        every { user.add(1, 1) } returns { 2 }
        assertEquals(user.add(1, 1), 2)
        assertEquals(user.add(1, 2), 0)

        every { user.add(match{true}, eq(2)) } returns { 10 }
        assertEquals(user.add(1, 2), 10)
        assertEquals(user.add(2, 2), 10)
        assertEquals(user.add(3, 2), 10)
        assertEquals(user.add(1, 1), 2)
        assertEquals(user.add(1, 0), 0)
    }

    @Test
    fun testSpy() {
        var ken = User("Ken")
        val barbie = User("Barbie")
        ken = Mockitty.spy(ken)
        barbie.addFriend(ken)
        barbie.showFriendList()

        verify { ken.getName() }
        verify(exactly = 1) {
            ken.getName()
        }
        verify(1, 2) { ken.getName() }

        barbie.makeFriendsSayHello()
        verifySeq {
            ken.getName()
            ken.hello()
        }

        ken.hello()
        assertDoesNotThrow {
            verify { ken.hello() }
        }
        assertDoesNotThrow {
            verify(exactly = 3) {
                ken.hello()
            }
        }
        assertDoesNotThrow {
            verify(1, 5) {
                ken.hello()
            }
        }
        assertFailsWith<VerificationFailedException> {
            verify(0, 0) {
                ken.hello()
            }
        }
        ken.hello()
        ken.showFriendList()
        assertDoesNotThrow {
            verifySeq {
                ken.hello()
                ken.showFriendList()
            }
        }
        assertFailsWith<VerificationFailedException> {
            verifySeq {
                ken.showFriendList()
                ken.hello()
            }
        }

    }
}