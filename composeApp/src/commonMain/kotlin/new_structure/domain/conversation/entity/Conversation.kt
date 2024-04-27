package new_structure.domain.conversation.entity

import new_structure.domain.conversation.entity.Role.USER
import java.util.UUID

data class Conversation(
    val id: String = UUID.randomUUID().toString(),
    val currentInput: String = "",
    val messages: List<Message> = emptyList(),
    val persona: Persona? = null,
) {
    val canResetConversation = messages.isNotEmpty()
    val canSubmit = currentInput.isNotBlank()

    fun onMessageInput(input: String): Conversation {
        return this.copy(currentInput = input)
    }

    fun onSubmit(): Conversation {
        if (!canSubmit) return this

        val newMessages = messages.toMutableList().apply {
            add(Message(content = currentInput, role = USER))
        }

        return this.copy(
            currentInput = "",
            messages = newMessages,
        )
    }

    fun onUpdateMessage(newMessage: Message): Conversation {
        val newMessages = messages.toMutableList().apply {
            replaceAll {
                if (it.id == newMessage.id) newMessage else it
            }
        }

        return this.copy(messages = newMessages)
    }
}
