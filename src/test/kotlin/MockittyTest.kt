
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
    fun testClassLoader() {
        val classLoader = URLClassLoader(arrayOf(File("test.jar").toURI().toURL()))
        val loadClass = classLoader.loadClass("Bean")
        val mock = Mockitty.mock(loadClass)
        assertNull(mock.toString())
    }

    @Test
    fun testStatic() {
        assertEquals(User.staticMethod(), "STATIC")
        Mockitty.mock<User.Companion>(User.Companion::staticMethod)
        assertNull(User.staticMethod())
        Mockitty.every { User.staticMethod() } returns { "static" }
        assertEquals(User.staticMethod(), "static")


        assertEquals(User.countAge(2003), 21)
        Mockitty.mock<User.Companion>(User.Companion::countAge)
        assertEquals(User.countAge(2003), 0)
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

        Mockitty.every { user.add(match{true}, eq(2)) } returns { 10 }
        assertEquals(user.add(1, 2), 10)
        assertEquals(user.add(2, 2), 10)
        assertEquals(user.add(3, 2), 10)
        assertEquals(user.add(1, 1), 2)
        assertEquals(user.add(1, 0), 0)
    }

    @Test
    fun testSpy() {
        val user1 = User("Ken")
        val user2 = User("Barbie")
        val spyUser = Mockitty.spy(user1)
        spyUser.addFriend(user2)
        spyUser.readFriendList()
        assertFailsWith<VerificationFailedException> {
            verify { spyUser.hello() }
        }
        spyUser.hello()
        assertDoesNotThrow {
            verify { spyUser.hello() }
        }
        assertDoesNotThrow {
            verify(exactly = 1) {
                spyUser.hello()
            }
        }
        assertDoesNotThrow {
            verify(1, 2) {
                spyUser.hello()
            }
        }
        assertFailsWith<VerificationFailedException> {
            verify(0, 0) {
                spyUser.hello()
            }
        }
        spyUser.hello()
        spyUser.readFriendList()
        assertDoesNotThrow {
            verifySeq {
                spyUser.hello()
                spyUser.readFriendList()
            }
        }
        assertFailsWith<VerificationFailedException> {
            verifySeq {
                spyUser.readFriendList()
                spyUser.hello()
            }
        }

    }
}