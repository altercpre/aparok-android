package io.aparok.app.domain.search

import io.aparok.app.core_models.Command
import io.aparok.app.core_models.GlobalSearchHistory
import io.aparok.app.core_models.Id
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import io.aparok.app.domain.block.repo.BlockRepository
import io.aparok.app.domain.config.UserSettingsRepository
import javax.inject.Inject

class SearchWithMeta @Inject constructor(
    private val repo: BlockRepository,
    private val settings: UserSettingsRepository,
    dispatchers: AppCoroutineDispatchers
) : ResultInteractor<SearchWithMeta.Params, List<Command.SearchWithMeta.Result>>(dispatchers.io) {
    override suspend fun doWork(params: SearchWithMeta.Params): List<Command.SearchWithMeta.Result> {
        return repo.searchObjectWithMeta(command = params.command).also {
            if (params.saveSearch) saveSearch(params)
        }
    }

    private suspend fun saveSearch(params: Params) {
        val search = GlobalSearchHistory(
            query = params.command.query,
            relatedObject = params.relatedObjectId
        )
        settings.setGlobalSearchHistory(globalSearchHistory = search, space = params.command.space)
    }

    data class Params(
        val command: Command.SearchWithMeta,
        val relatedObjectId: Id?,
        val saveSearch: Boolean = false
    )
}