import net.iceyleagons.klarity.DefaultValueSource
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class JavaScriptTest {

    @BeforeTest
    fun initKlarity() {
        KlarityJS.configure(JSON.parse("{\"globals\": {\"prefix\": \"[PREFIX] \"}}"))
        KlarityJS.registerSource("en", DefaultValueSource())
    }

    @Test
    fun test() {
        val expected = "[PREFIX] Something"
        val actual = KlarityJS.translate("test", "Something")
        assertEquals(expected, actual)
    }
}