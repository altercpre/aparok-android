package io.aparok.app.data.auth.model

import io.aparok.app.domain.clipboard.Clip

/**
 * @see Clip
 */
class ClipEntity(
    override val text: String,
    override val html: String?,
    override val uri: String?
) : Clip