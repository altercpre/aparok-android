package io.aparok.app.core_models.ui

data class AttachmentPreview(
    val type: AttachmentType,
    val objectIcon: ObjectIcon,
    val title: String? = null
)

enum class AttachmentType {
    IMAGE, FILE, LINK
}