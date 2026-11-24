package io.aparok.app.domain.multiplayer

import io.aparok.app.core_models.Id
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import javax.inject.Inject

/**
 * Generates a deep link for initiating a 1-1 chat with the current user.
 * The link format is: aparok://chat/{identity}#{metaDataKey}
 */
class GenerateOneToOneChatLink @Inject constructor(
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<GenerateOneToOneChatLink.Params, OneToOneLinkData>(dispatchers.io) {

    data class Params(
        val identity: Id,
        val metaDataKey: String,
        val name: String
    )

    override suspend fun doWork(params: Params): OneToOneLinkData {
        if (params.metaDataKey.isBlank()) {
            throw IllegalStateException("MetaData key not available")
        }
        if (params.identity.isBlank()) {
            throw IllegalStateException("Identity not available")
        }

        return OneToOneLinkData(
            link = "aparok://chat/${params.identity}#${params.metaDataKey}",
            name = params.name,
            identity = params.identity
        )
    }

    companion object {
        const val BASE_URL = "aparok://chat/"
    }
}

/**
 * Data class containing the generated 1-1 chat link and associated profile information.
 *
 * @property link The generated deep link URL
 * @property name The current user's profile name
 * @property identity The current user's identity ID
 */
data class OneToOneLinkData(
    val link: String,
    val name: String,
    val identity: Id
)
