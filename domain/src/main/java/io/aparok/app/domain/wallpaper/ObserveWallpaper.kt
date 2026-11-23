package io.aparok.app.domain.wallpaper

import io.aparok.app.core_models.Wallpaper
import io.aparok.app.domain.base.BaseUseCase
import io.aparok.app.domain.base.FlowUseCase
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class ObserveWallpaper @Inject constructor () : FlowUseCase<Wallpaper, BaseUseCase.None>() {
    override fun build(params: BaseUseCase.None?): Flow<Wallpaper> {
        return WallpaperStore.Default.observe()
    }
}