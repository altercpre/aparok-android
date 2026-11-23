package io.aparok.app.domain.download

import io.aparok.app.core_models.Id
import io.aparok.app.domain.base.BaseUseCase
import io.aparok.app.domain.base.Either
import io.aparok.app.core_models.Url
import io.aparok.app.domain.download.DownloadFile.Params
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/**
 * Use-case for starting downloading files.
 * @see Params
 */
class DownloadFile @Inject constructor(
    private val downloader: Downloader,
    context: CoroutineContext
) : BaseUseCase<Unit, Params>(context) {

    override suspend fun run(params: Params) = try {
        downloader.download(
            url = params.url,
            name = params.name
        ).let { Either.Right(it) }
    } catch (t: Throwable) {
        Either.Left(t)
    }

    /**
     * Params for downloading file.
     * @property name file name
     * @property url url of the file to download
     */
    data class Params(
        val name: String,
        val url: Url
    )
}