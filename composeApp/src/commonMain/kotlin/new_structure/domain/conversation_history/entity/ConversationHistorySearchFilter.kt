package new_structure.domain.conversation_history.entity

import androidx.annotation.CheckResult
import java.time.LocalDateTime

data class ConversationHistorySearchFilter(
    val searchInput: String? = null,
    val startDate: LocalDateTime? = null,
    val endDate: LocalDateTime? = null,
    val error: ConversationHistorySearchFilterError? = null,
) {
    val isValid = (startDate == null && endDate == null) || (startDate != null && endDate != null)

    @CheckResult
    fun onSearchInput(input: String): ConversationHistorySearchFilter {
        return this.copy(searchInput = input)
    }

    @CheckResult
    fun onStartDateSelect(date: LocalDateTime): ConversationHistorySearchFilter {
        return this.copy(
            startDate = date,
            error = null,
        )
    }

    @CheckResult
    fun onEndDateSelect(date: LocalDateTime): ConversationHistorySearchFilter {
        return this.copy(
            endDate = date,
            error = null,
        )
    }

    @CheckResult
    fun onClearStartDate(): ConversationHistorySearchFilter {
        return this.copy(
            startDate = null,
            error = null,
        )
    }

    @CheckResult
    fun onClearEndDate(): ConversationHistorySearchFilter {
        return this.copy(
            endDate = null,
            error = null,
        )
    }

    @CheckResult
    fun onSearchSubmit(): ConversationHistorySearchFilter {
        if (!isValid) {
            if (startDate == null) return this.copy(error = ConversationHistorySearchFilterError.StartDateEmpty)
            if (endDate == null) return this.copy(error = ConversationHistorySearchFilterError.EndDateEmpty)
        }

        return this
    }

    sealed interface ConversationHistorySearchFilterError {
        data object StartDateEmpty : ConversationHistorySearchFilterError
        data object EndDateEmpty : ConversationHistorySearchFilterError
    }
}
