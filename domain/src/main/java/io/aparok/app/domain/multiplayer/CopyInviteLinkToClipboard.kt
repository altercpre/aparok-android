package io.aparok.app.domain.multiplayer

import io.aparok.app.core_models.Block
import io.aparok.app.domain.base.BaseUseCase
import io.aparok.app.domain.clipboard.Clipboard
import javax.inject.Inject

/**
 * Use case for copying invite link to clipboard
 */
class CopyInviteLinkToClipboard @Inject constructor(
    private val clipboard: Clipboard
) : BaseUseCase<Unit, CopyInviteLinkToClipboard.Params>() {

    override suspend fun run(params: Params) = safe {
        clipboard.put(
            text = params.link,
            html = null,
            blocks = emptyList<Block>()
        )
    }

    data class Params(
        val link: String
    )
}