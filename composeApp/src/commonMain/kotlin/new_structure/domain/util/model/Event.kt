package new_structure.domain.util.model

class Event<T>(private val initialValue: T) {
    private var isConsumed = false

    val value: T? = initialValue
        get() {
            return if (isConsumed) {
                null
            } else {
                isConsumed = true
                field
            }
        }

    override fun toString(): String {
        return "Event: $initialValue (consumed = $isConsumed)"
    }
}
