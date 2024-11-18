package domain.util.model

import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test

// In case I forget, run with CTRL-SHIFT-F10
class EventTest {
    @Test
    fun `getValue, not consumed`() {
        val event = Event(1)

        assertThat(event.toString()).isEqualTo("Event: 1 (consumed = false)")
        assertThat(event.value).isEqualTo(1)
    }

    @Test
    fun `getValue, consumed`() {
        val event = Event(1)

        event.value

        assertThat(event.toString()).isEqualTo("Event: 1 (consumed = true)")
        assertThat(event.value).isNull()
    }

    @Test
    fun `identical events shouldn't be equal`() {
        val event1 = Event(1)
        val event2 = Event(1)

        assertThat(event1).isNotEqualTo(event2)
    }
}
