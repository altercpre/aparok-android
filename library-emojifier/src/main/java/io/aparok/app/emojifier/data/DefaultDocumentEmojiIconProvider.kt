package io.aparok.app.emojifier.data

import io.aparok.app.domain.icon.DocumentEmojiIconProvider

class DefaultDocumentEmojiIconProvider : DocumentEmojiIconProvider {

    companion object {
        val DOCUMENT_SET = listOf(
            "🌳", "⛳", "🧬", "🎈", "🎓",
            "💡", "🎒", "🚀", "🤖", "📚",
            "🍎", "🏡", "🤝", "😍", "☕",
            "🔥", "💥", "✍", "⏳", "📌",
            "🖍", "🦉", "📮", "📄", "🖌",
            "🗳", "⏰", "🔑", "🎉", "🗃",
            "🔖", "🧠", "👁", "🎗", "🎲",
            "🏙", "🚲", "⚙", "🔶", "🌍",
            "🏕", "🎡", "🌵", "🚗", "🚂",
            "🖼", "⭐", "🥁", "🛴", "🛫",
            "🏔", "🏗", "🛠", "🔍", "🕹",
            "🛋", "🎁", "🧮", "🏜", "🌋",
            "🎇", "🍏", "💫", "🌿", "🦊",
            "🍁", "🐎", "🍋", "🏍", "⛵"
        )
    }

    override fun random(): String = DOCUMENT_SET.random()
}