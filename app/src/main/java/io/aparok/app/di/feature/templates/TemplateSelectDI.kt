package io.aparok.app.di.feature.templates

import androidx.lifecycle.ViewModelProvider
import io.aparok.app.analytics.base.Analytics
import io.aparok.app.di.common.ComponentDependencies
import io.aparok.app.di.feature.onboarding.AuthScreenScope
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.block.repo.BlockRepository
import io.aparok.app.core_models.UrlBuilder
import io.aparok.app.domain.objects.StoreOfObjectTypes
import io.aparok.app.domain.objects.StoreOfRelations
import io.aparok.app.domain.templates.ApplyTemplate
import io.aparok.app.domain.templates.GetTemplates
import io.aparok.app.domain.workspace.SpaceManager
import io.aparok.app.presentation.analytics.AnalyticSpaceHelperDelegate
import io.aparok.app.presentation.editor.cover.CoverImageHashProvider
import io.aparok.app.presentation.onboarding.OnboardingViewModel
import io.aparok.app.providers.DefaultCoverImageHashProvider
import io.aparok.app.ui.templates.TemplateSelectFragment
import dagger.Binds
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Scope

@Component(
    modules = [TemplateSelectModule::class],
    dependencies = [TemplateSelectDependencies::class]
)
@TemplateSelectScope
interface TemplateSelectComponent {

    @Component.Factory
    interface Factory {
        fun create(dependencies: TemplateSelectDependencies): TemplateSelectComponent
    }

    fun inject(fragment: TemplateSelectFragment)
}

@Module
object TemplateSelectModule {
    @JvmStatic
    @Provides
    @TemplateSelectScope
    fun applyTemplate(repo: BlockRepository, dispatchers: AppCoroutineDispatchers): ApplyTemplate =
        ApplyTemplate(
            repo = repo,
            dispatchers = dispatchers
        )

    @JvmStatic
    @Provides
    @TemplateSelectScope
    fun getTemplates(
        repo: BlockRepository,
        spaceManager: SpaceManager,
        dispatchers: AppCoroutineDispatchers
    ): GetTemplates =
        GetTemplates(
            repo = repo,
            spaceManager = spaceManager,
            dispatchers = dispatchers
        )

    @JvmStatic
    @TemplateSelectScope
    @Provides
    fun provideCoverImageHashProvider(): CoverImageHashProvider = DefaultCoverImageHashProvider()

    @Module
    interface Declarations {
        @Binds
        @AuthScreenScope
        fun bindViewModelFactory(factory: OnboardingViewModel.Factory): ViewModelProvider.Factory
    }
}

interface TemplateSelectDependencies : ComponentDependencies {
    fun urlBuilder(): UrlBuilder
    fun storeOfRelations(): StoreOfRelations
    fun storeOfObjectTypes(): StoreOfObjectTypes
    fun analytics() : Analytics
    fun blockRepository(): BlockRepository
    fun dispatchers(): AppCoroutineDispatchers
    fun spaceManager(): SpaceManager
    fun analyticSpaceHelper(): AnalyticSpaceHelperDelegate
}

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class TemplateSelectScope