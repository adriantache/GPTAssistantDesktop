package domain.conversation.entity

import androidx.annotation.CheckResult
import domain.conversation.entity.Role.SYSTEM
import domain.conversation.entity.Role.USER
import java.util.*

data class Conversation(
    val id: String = UUID.randomUUID().toString(),
    val title: String? = null,
    val currentInput: String = "",
    val messages: Map<String, Message> = emptyMap(), // Ok to use, as long as messages are added in the correct order.
    val persona: Persona? = null,
) {
    val canResetConversation = messages.isNotEmpty()
    val canSubmit = currentInput.isNotBlank()

    // Used to indicate when a title should be generated for this conversation. The first reply is usually lacking in
    //  enough context to generate a good title, so we do it twice, if the conversation goes on long enough.
    val shouldGenerateTitle = messages.size < 5

    @CheckResult
    fun onMessageInput(input: String): Conversation {
        return this.copy(currentInput = input)
    }

    @CheckResult
    fun onSubmit(): Conversation {
        if (!canSubmit) return this

        val personaMessage = persona?.let {
            Message(
                id = it.id,
                content = it.instructions,
                role = SYSTEM,
            )
        }

        val message = Message(content = currentInput, role = USER)
        val newMessages = messages.toMutableMap().apply {
            personaMessage?.let { this[it.id] = it }
            this[message.id] = message
        }

        return this.copy(
            currentInput = "",
            messages = newMessages,
        )
    }

    @CheckResult
    fun onUpdateMessage(newMessage: Message): Conversation {
        val newMessages = messages.toMutableMap().apply {
            this[newMessage.id] = newMessage
        }

        return this.copy(messages = newMessages)
    }

    @CheckResult
    fun onSetPersona(persona: Persona?): Conversation {
        return this.copy(persona = persona)
    }

    @CheckResult
    fun onSetTitle(title: String): Conversation {
        return this.copy(title = title)
    }
}
