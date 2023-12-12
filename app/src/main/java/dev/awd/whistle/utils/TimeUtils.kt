package dev.awd.whistle.utils

fun formatTime(seconds: String, minutes: String): String {
    return "$minutes:$seconds"
}

fun formatTime(minutes: String, seconds: String, millis: String): String {
    return "$minutes:$seconds.$millis"
}

fun Int.pad(): String {
    return this.toString().padStart(2, '0')
}