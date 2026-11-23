package io.aparok.app.domain.auth.interactor

import io.aparok.app.domain.auth.repo.AuthRepository
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.base.ResultInteractor
import javax.inject.Inject

/**
 * Tells heart it may now load the spaces that were deferred by
 * AccountSelect.preferredSpaceId. Failures (e.g. ACCOUNT_IS_NOT_RUNNING)
 * are benign — heart has its own 10s timer fallback — so callers treat
 * a Resultat.Failure as a no-op.
 */
class PreloadRemainingSpaces @Inject constructor(
    dispatchers: AppCoroutineDispatchers,
    private val repository: AuthRepository
) : ResultInteractor<Unit, Unit>(dispatchers.io) {

    override suspend fun doWork(params: Unit) {
        repository.preloadRemainingSpaces()
    }
}
