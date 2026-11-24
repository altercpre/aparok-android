package io.aparok.app.domain.publishing

import io.aparok.app.core_models.DVFilter
import io.aparok.app.core_models.DVFilterCondition
import io.aparok.app.core_models.Id
import io.aparok.app.core_models.ObjectType
import io.aparok.app.core_models.ObjectWrapper
import io.aparok.app.core_models.Relations
import io.aparok.app.core_models.primitives.SpaceId
import io.aparok.app.domain.auth.repo.AuthRepository
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.block.repo.BlockRepository
import javax.inject.Inject

class GetPublishingDomain @Inject constructor(
    private val auth: AuthRepository,
    private val repo: BlockRepository,
    dispatchers: AppCoroutineDispatchers,
) : ResultInteractor<GetPublishingDomain.Params, String?>(dispatchers.io) {

    override suspend fun doWork(params: Params): String? {
        val account = auth.getCurrentAccountId()
        val results = repo.searchObjects(
            space = params.space,
            limit = 1,
            keys = listOf(
                Relations.ID,
                Relations.NAME,
                Relations.GLOBAL_NAME
            ),
            filters = buildList {
                add(
                    DVFilter(
                        relation = Relations.LAYOUT,
                        value = ObjectType.Layout.PARTICIPANT.code.toDouble(),
                        condition = DVFilterCondition.EQUAL
                    )
                )
                add(
                    DVFilter(
                        relation = Relations.IDENTITY,
                        value = account,
                        condition = DVFilterCondition.EQUAL
                    )
                )
            },
        )
        if (results.isNotEmpty()) {
            val wrapper = ObjectWrapper.SpaceMember(results.first())
            val globalName = wrapper.globalName
            return if (globalName.isNullOrEmpty()) {
                "$FREE_DOMAIN/$account"
            } else {
                "$globalName.$PAID_DOMAIN"
            }
        } else {
            return "$FREE_DOMAIN/$account"
        }
    }

    data class Params(val space: SpaceId)

    companion object {
        private const val FREE_DOMAIN = "aparok.local"
        private const val PAID_DOMAIN = "org"
    }
}