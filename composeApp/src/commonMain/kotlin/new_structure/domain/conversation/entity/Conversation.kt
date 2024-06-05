package new_structure.domain.conversation.entity

import new_structure.domain.conversation.entity.Role.USER
import java.util.*

data class Conversation(
    val id: String = UUID.randomUUID().toString(),
    val currentInput: String = "",
    val messages: Map<String, Message> = emptyMap(), // Ok to use, as long as messages are added in the correct order.
    val persona: Persona? = null,
) {
    val canResetConversation = messages.isNotEmpty()
    val canSubmit = currentInput.isNotBlank()

    fun onMessageInput(input: String): Conversation {
        return this.copy(currentInput = input)
    }

    fun onSubmit(persona: Persona?): Conversation {
        if (!canSubmit) return this

        val personaMessage = persona?.let { Message(content = it.instructions, role = USER) }

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

    fun onUpdateMessage(newMessage: Message): Conversation {
        val newMessages = messages.toMutableMap().apply {
            this[newMessage.id] = newMessage
        }

        return this.copy(messages = newMessages)
    }
}
