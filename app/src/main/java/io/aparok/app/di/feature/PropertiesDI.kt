package io.aparok.app.di.feature

import androidx.lifecycle.ViewModelProvider
import io.aparok.app.core_models.Payload
import io.aparok.app.core_utils.di.scope.PerModal
import io.aparok.app.core_utils.di.scope.PerScreen
import io.aparok.app.di.common.ComponentDependencies
import io.aparok.app.domain.base.AppCoroutineDispatchers
import io.aparok.app.domain.block.repo.BlockRepository
import io.aparok.app.domain.dataview.SetDataViewProperties
import io.aparok.app.core_models.UrlBuilder
import io.aparok.app.domain.`object`.SetObjectDetails
import io.aparok.app.domain.objects.StoreOfObjectTypes
import io.aparok.app.domain.objects.StoreOfRelations
import io.aparok.app.domain.primitives.SetObjectTypeRecommendedFields
import io.aparok.app.domain.relations.CreateRelation
import io.aparok.app.domain.resources.StringResourceProvider
import io.aparok.app.feature_properties.EditTypePropertiesViewModelFactory
import io.aparok.app.feature_properties.add.EditTypePropertiesVmParams
import io.aparok.app.presentation.util.Dispatcher
import io.aparok.app.ui.primitives.EditTypePropertiesFragment
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides

//region EDIT OBJECT TYPE PROPERTIES SCREEN
@PerModal
@Component(
    modules = [
        EditTypePropertiesModule::class,
        EditTypePropertiesModule.Declarations::class
    ],
    dependencies = [EditTypePropertiesDependencies::class]
)
interface EditTypePropertiesComponent {
    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance vmParams: EditTypePropertiesVmParams,
            dependencies: EditTypePropertiesDependencies
        ): EditTypePropertiesComponent
    }

    fun inject(fragment: EditTypePropertiesFragment)
}

@Module
object EditTypePropertiesModule {

    @JvmStatic
    @Provides
    @PerModal
    fun provideTypeSetRecommendedFields(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): SetObjectTypeRecommendedFields = SetObjectTypeRecommendedFields(repo, dispatchers)

    @JvmStatic
    @Provides
    @PerModal
    fun createRelation(
        repo: BlockRepository,
        storeOfRelations: StoreOfRelations,
    ) = CreateRelation(repo, storeOfRelations)

    @JvmStatic
    @Provides
    @PerModal
    fun provideSetObjectDetails(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): SetObjectDetails = SetObjectDetails(repo, dispatchers)

    @JvmStatic
    @Provides
    @PerModal
    fun provideSetDataViewProperties(
        repo: BlockRepository,
        dispatchers: AppCoroutineDispatchers
    ): SetDataViewProperties = SetDataViewProperties(repo, dispatchers)

    @Module
    interface Declarations {
        @PerModal
        @Binds
        fun bindViewModelFactory(
            factory: EditTypePropertiesViewModelFactory
        ): ViewModelProvider.Factory
    }
}

interface EditTypePropertiesDependencies : ComponentDependencies {
    fun provideStringResourceProvider(): StringResourceProvider
    fun provideStoreOfRelations(): StoreOfRelations
    fun provideStoreOfObjectTypes(): StoreOfObjectTypes
    fun provideBlockRepository(): BlockRepository
    fun provideAppCoroutineDispatchers(): AppCoroutineDispatchers
    fun urlBuilder(): UrlBuilder
    fun dispatcher(): Dispatcher<Payload>
}
//endregion