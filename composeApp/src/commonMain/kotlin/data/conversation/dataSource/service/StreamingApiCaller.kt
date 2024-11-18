package data.conversation.dataSource.service

import data.conversation.dataSource.model.ChatMessageDto
import domain.util.model.Outcome
import kotlinx.coroutines.flow.Flow

interface StreamingApiCaller {
    fun getReply(conversation: List<ChatMessageDto>): Flow<Outcome<String>>
}
