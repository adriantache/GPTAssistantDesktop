package new_structure.domain.util.model

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map as flowMap

sealed class Outcome<T> {
    data class Failure<T>(val error: Error) : Outcome<T>()
    data class Success<T>(val data: T) : Outcome<T>()

    val isSuccess get() = this is Success
    val isFailure get() = this is Failure

    fun dataOrThrow() = (this as? Success)?.data ?: error("Invalid state: $this")
    fun errorOrThrow() = (this as? Failure)?.error ?: error("Invalid state: $this")

    fun <R> map(transform: (T) -> R): Outcome<R> {
        return when (this) {
            is Failure -> Failure(this.error)
            is Success -> Success(transform(this.data))
        }
    }

    fun onSuccess(callback: (T) -> Unit): Outcome<T> {
        if (this is Success) callback(this.data)

        return this
    }

    fun onFailure(callback: (Error) -> Unit): Outcome<T> {
        if (this is Failure) callback(this.error)

        return this
    }

    companion object {
        fun <T, R> Flow<Outcome<T>>.map(transform: (T) -> R): Flow<Outcome<R>> {
            return this.flowMap {
                it.map(transform)
            }
        }
    }
}
