package com.keshen.alumni_directory_app.core.utils

data class UiMessage(
    val text: String,
    val type: MessageType
)

enum class MessageType {
    ERROR, SUCCESS
}