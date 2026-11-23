package io.aparok.app.di.feature.templates

import androidx.lifecycle.ViewModelProvider
import io.aparok.app.di.common.ComponentDependencies
import io.aparok.app.domain.misc.DateProvider
import io.aparok.app.core_models.UrlBuilder
import io.aparok.app.domain.objects.StoreOfObjectTypes
import io.aparok.app.domain.objects.StoreOfRelations
import io.aparok.app.domain.primitives.FieldParser
import io.aparok.app.presentation.editor.cover.CoverImageHashProvider
import io.aparok.app.presentation.editor.render.DefaultBlockViewRenderer
import io.aparok.app.presentation.editor.toggle.ToggleStateHolder
import io.aparok.app.presentation.templates.TemplateBlankViewModelFactory
import io.aparok.app.presentation.widgets.collection.ResourceProvider
import io.aparok.app.providers.DefaultCoverImageHashProvider
import io.aparok.app.ui.templates.TemplateBlankFragment
import dagger.Binds
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Scope

@Component(
    modules = [TemplateBlankModule::class],
    dependencies = [TemplateBlankDependencies::class]
)
@TemplateBlankScope
interface TemplateBlankComponent {

    @Component.Factory
    interface Factory {
        fun create(dependencies: TemplateBlankDependencies): TemplateBlankComponent
    }

    fun inject(fragment: TemplateBlankFragment)
}

@Module
object TemplateBlankModule {

    @JvmStatic
    @TemplateBlankScope
    @Provides
    fun provideToggleHolder(): ToggleStateHolder = ToggleStateHolder.Default()

    @JvmStatic
    @TemplateBlankScope
    @Provides
    fun provideCoverImageHashProvider(): CoverImageHashProvider = DefaultCoverImageHashProvider()

    @JvmStatic
    @TemplateBlankScope
    @Provides
    fun provideViewModelFactory(
        renderer: DefaultBlockViewRenderer,
        storeOfRelations: StoreOfRelations,
        storeOfObjectTypes: StoreOfObjectTypes,
        fieldParser: FieldParser
    ): ViewModelProvider.Factory = TemplateBlankViewModelFactory(
        renderer = renderer,
        storeOfRelations = storeOfRelations,
        storeOfObjectTypes = storeOfObjectTypes,
        fieldParser = fieldParser
    )

    @Module
    interface Declarations {

        @TemplateBlankScope
        @Binds
        fun bindRenderer(
            defaultRenderer: DefaultBlockViewRenderer
        ): DefaultBlockViewRenderer
    }
}

interface TemplateBlankDependencies : ComponentDependencies {
    fun urlBuilder(): UrlBuilder
    fun storeOfRelations(): StoreOfRelations
    fun storeOfObjectTypes(): StoreOfObjectTypes
    fun dateProvider(): DateProvider
    fun fieldsProvider(): FieldParser
    fun resourceProvider(): ResourceProvider
}

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class TemplateBlankScope