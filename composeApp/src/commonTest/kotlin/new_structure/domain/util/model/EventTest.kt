package new_structure.domain.util.model

import kotlin.test.Test
import kotlin.test.assertEquals

// In case I forget, run with CTRL-SHIFT-F10
@Suppress("unused")
class EventTest {
    @Test
    fun `getValue, not consumed`() {
        val event = Event(1)

        assertEquals(1, event.value)
    }

    @Test
    fun `getValue, consumed`() {
        val event = Event(1)

        event.value

        assertEquals(null, event.value)
    }
}
