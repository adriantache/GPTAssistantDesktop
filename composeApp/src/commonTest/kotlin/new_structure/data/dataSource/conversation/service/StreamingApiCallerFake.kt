package new_structure.data.dataSource.conversation.service

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import new_structure.data.conversation.dataSource.conversation.model.ChatMessageDto
import new_structure.data.conversation.dataSource.conversation.service.StreamingApiCaller
import new_structure.domain.util.model.Outcome
import new_structure.domain.util.model.TestError

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
