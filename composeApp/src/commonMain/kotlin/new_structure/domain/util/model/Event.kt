package new_structure.domain.util.model

class Event<T>(initialValue: T) {
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
}