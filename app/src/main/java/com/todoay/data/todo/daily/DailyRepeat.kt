package com.todoay.data.todo.daily

data class DailyRepeat(
    val type : RepeatType,
    val duration : RepeatDuration,
    /** Repeat Count for Custom */
    val repeat : Int
) {
    enum class RepeatType(val type : String) {
        DAILY("daily"),
        WEEKS("weeks"),
        MONTHS("months"),
        YEARS("years")
    }
    enum class RepeatDuration(val duration : String) {
        ONE_MONTH("one_month"),
        NO_END_DATE("no_end_date"),
        CUSTOM_WEEKS("custom_weeks"),
        CUSTOM_MONTH("custom_month"),
        CUSTOM_NUMBER("custom_number")
    }
}
