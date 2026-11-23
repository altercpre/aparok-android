package io.aparok.app.domain.multiplayer

import io.aparok.app.core_models.Id

interface SpaceInviteResolver {
    fun parseContentId(link: String) : Id?
    fun parseFileKey(link: String) : Id?
    fun createInviteLink(contentId: String, encryptionKey: String) : String
}