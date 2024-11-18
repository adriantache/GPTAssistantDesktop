package data.dataSource.conversation.service

import data.conversation.dataSource.model.ChatMessageDto
import data.conversation.dataSource.service.StreamingApiCaller
import domain.util.model.Outcome
import domain.util.model.TestError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class StreamingApiCallerFake : StreamingApiCaller {
    var expectsSuccess = true

    override fun getReply(conversation: List<ChatMessageDto>): Flow<Outcome<String>> {
        val output = if (expectsSuccess) {
            Outcome.Success(conversation.first().content)
        } else {
            Outcome.Failure(TestError())
        }

        return flowOf(output)
    }
}
