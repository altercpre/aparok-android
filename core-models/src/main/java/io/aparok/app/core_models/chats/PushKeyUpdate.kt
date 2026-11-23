package io.aparok.app.core_models.chats

data class PushKeyUpdate(
    val encryptionKeyId: String,
    val encryptionKey: String
) {
    companion object {
        val EMPTY = PushKeyUpdate(
            encryptionKeyId = "",
            encryptionKey = ""
        )
    }
}