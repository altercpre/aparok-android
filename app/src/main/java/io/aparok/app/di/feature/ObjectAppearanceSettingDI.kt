package io.aparok.app.di.feature

import io.aparok.app.core_models.Payload
import io.aparok.app.core_utils.di.scope.PerModal
import io.aparok.app.domain.block.interactor.SetLinkAppearance
import io.aparok.app.presentation.editor.Editor
import io.aparok.app.presentation.objects.appearance.ObjectAppearanceSettingViewModel
import io.aparok.app.presentation.objects.appearance.choose.ObjectAppearanceChooseDescriptionViewModel
import io.aparok.app.presentation.objects.appearance.choose.ObjectAppearanceChooseIconViewModel
import io.aparok.app.presentation.objects.appearance.choose.ObjectAppearanceChoosePreviewLayoutViewModel
import io.aparok.app.presentation.util.Dispatcher
import io.aparok.app.ui.objects.appearance.ObjectAppearanceSettingFragment
import io.aparok.app.ui.objects.appearance.choose.ObjectAppearanceChooseDescriptionFragment
import io.aparok.app.ui.objects.appearance.choose.ObjectAppearanceChooseIconFragment
import io.aparok.app.ui.objects.appearance.choose.ObjectAppearanceChoosePreviewLayoutFragment
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

//region SETTINGS
@Subcomponent(modules = [ObjectAppearanceSettingModule::class])
@PerModal
interface ObjectAppearanceSettingSubComponent {

    @Subcomponent.Builder
    interface Builder {
        fun module(module: ObjectAppearanceSettingModule): Builder
        fun build(): ObjectAppearanceSettingSubComponent
    }

    fun inject(fragment: ObjectAppearanceSettingFragment)
}

@Module
object ObjectAppearanceSettingModule {
    @JvmStatic
    @Provides
    @PerModal
    fun provideObjectAppearanceSettingViewModelFactory(
        storage: Editor.Storage,
        setLinkAppearance: SetLinkAppearance,
        dispatcher: Dispatcher<Payload>
    ): ObjectAppearanceSettingViewModel.Factory {
        return ObjectAppearanceSettingViewModel.Factory(
            storage = storage,
            setLinkAppearance = setLinkAppearance,
            dispatcher = dispatcher
        )
    }
}
//endregion

//region ICON
@Subcomponent(modules = [ObjectAppearanceIconModule::class])
@PerModal
interface ObjectAppearanceIconSubComponent {

    @Subcomponent.Builder
    interface Builder {
        fun module(module: ObjectAppearanceIconModule): Builder
        fun build(): ObjectAppearanceIconSubComponent
    }

    fun inject(fragment: ObjectAppearanceChooseIconFragment)
}

@Module
object ObjectAppearanceIconModule {

    @JvmStatic
    @Provides
    @PerModal
    fun provideObjectAppearanceIconViewModelFactory(
        storage: Editor.Storage,
        setLinkAppearance: SetLinkAppearance,
        dispatcher: Dispatcher<Payload>
    ): ObjectAppearanceChooseIconViewModel.Factory {
        return ObjectAppearanceChooseIconViewModel.Factory(
            storage = storage,
            setLinkAppearance = setLinkAppearance,
            dispatcher = dispatcher
        )
    }
}
//endregion

//region PREVIEW LAYOUT
@Subcomponent(modules = [ObjectAppearancePreviewLayoutModule::class])
@PerModal
interface ObjectAppearancePreviewLayoutSubComponent {

    @Subcomponent.Builder
    interface Builder {
        fun module(module: ObjectAppearancePreviewLayoutModule): Builder
        fun build(): ObjectAppearancePreviewLayoutSubComponent
    }

    fun inject(fragment: ObjectAppearanceChoosePreviewLayoutFragment)
}

@Module
object ObjectAppearancePreviewLayoutModule {

    @JvmStatic
    @Provides
    @PerModal
    fun provideObjectAppearancePreviewLayoutViewModelFactory(
        storage: Editor.Storage,
        setLinkAppearance: SetLinkAppearance,
        dispatcher: Dispatcher<Payload>
    ): ObjectAppearanceChoosePreviewLayoutViewModel.Factory {
        return ObjectAppearanceChoosePreviewLayoutViewModel.Factory(
            storage = storage,
            setLinkAppearance = setLinkAppearance,
            dispatcher = dispatcher
        )
    }
}
//endregion

@Subcomponent(modules = [ObjectAppearanceChooseDescriptionModule::class])
@PerModal
interface ObjectAppearanceChooseDescriptionSubComponent {

    @Subcomponent.Builder
    interface Builder {
        fun module(module: ObjectAppearanceChooseDescriptionModule): Builder
        fun build(): ObjectAppearanceChooseDescriptionSubComponent
    }

    fun inject(fragment: ObjectAppearanceChooseDescriptionFragment)
}

@Module
object ObjectAppearanceChooseDescriptionModule {

    @JvmStatic
    @Provides
    @PerModal
    fun provideVM(
        storage: Editor.Storage,
        setLinkAppearance: SetLinkAppearance,
        dispatcher: Dispatcher<Payload>
    ): ObjectAppearanceChooseDescriptionViewModel.Factory {
        return ObjectAppearanceChooseDescriptionViewModel.Factory(
            storage = storage,
            setLinkAppearance = setLinkAppearance,
            dispatcher = dispatcher
        )
    }
}