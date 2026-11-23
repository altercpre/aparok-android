package io.aparok.app.di.feature

import io.aparok.app.analytics.base.Analytics
import io.aparok.app.core_models.Payload
import io.aparok.app.core_utils.di.scope.PerModal
import io.aparok.app.domain.block.repo.BlockRepository
import io.aparok.app.domain.icon.RemoveDocumentIcon
import io.aparok.app.domain.icon.RemoveTextBlockIcon
import io.aparok.app.domain.icon.SetDocumentEmojiIcon
import io.aparok.app.domain.icon.SetDocumentImageIcon
import io.aparok.app.domain.icon.SetTextBlockEmoji
import io.aparok.app.domain.icon.SetTextBlockImage
import io.aparok.app.emojifier.data.Emoji
import io.aparok.app.emojifier.suggest.EmojiSuggester
import io.aparok.app.presentation.editor.picker.ObjectIconPickerViewModelFactory
import io.aparok.app.presentation.editor.picker.ObjectSetIconPickerViewModelFactory
import io.aparok.app.presentation.editor.picker.TextBlockIconPickerViewModelFactory
import io.aparok.app.presentation.util.Dispatcher
import io.aparok.app.ui.editor.modals.ObjectIconPickerFragment
import io.aparok.app.ui.editor.modals.TextBlockIconPickerFragment
import io.aparok.app.ui.sets.ObjectSetIconPickerFragment
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

@Subcomponent(modules = [ObjectIconPickerBaseModule::class, ObjectIconPickerModule::class])
@PerModal
interface ObjectIconPickerComponent {
    @Subcomponent.Builder
    interface Builder {
        fun base(module: ObjectIconPickerBaseModule): Builder
        fun module(module: ObjectIconPickerModule): Builder
        fun build(): ObjectIconPickerComponent
    }

    fun inject(fragment: ObjectIconPickerFragment)
}

@Subcomponent(modules = [TextBlockIconPickerModule::class])
@PerModal
interface TextBlockIconPickerComponent {

    @Subcomponent.Builder
    interface Builder {
        fun module(module: TextBlockIconPickerModule): Builder
        fun build(): TextBlockIconPickerComponent
    }

    fun inject(fragment: TextBlockIconPickerFragment)
}

@Subcomponent(modules = [ObjectIconPickerBaseModule::class, ObjectSetIconPickerModule::class])
@PerModal
interface ObjectSetIconPickerComponent {
    @Subcomponent.Builder
    interface Builder {
        fun base(module: ObjectIconPickerBaseModule): Builder
        fun module(module: ObjectSetIconPickerModule): Builder
        fun build(): ObjectSetIconPickerComponent
    }

    fun inject(fragment: ObjectSetIconPickerFragment)
}

@Module
object ObjectIconPickerBaseModule {

    @Provides
    @PerModal
    fun provideSetDocumentEmojiIconUseCase(
        repo: BlockRepository
    ): SetDocumentEmojiIcon = SetDocumentEmojiIcon(
        repo = repo
    )

    @Provides
    @PerModal
    fun provideRemoveDocumentIconUseCase(
        repo: BlockRepository
    ): RemoveDocumentIcon = RemoveDocumentIcon(
        repo = repo
    )
}

@Module
object ObjectIconPickerModule {

    @Provides
    @PerModal
    fun provideViewModelFactory(
        setEmojiIcon: SetDocumentEmojiIcon,
        setImageIcon: SetDocumentImageIcon,
        removeDocumentIcon: RemoveDocumentIcon,
        emojiSuggester: EmojiSuggester,
        dispatcher: Dispatcher<Payload>,
        analytics: Analytics
    ): ObjectIconPickerViewModelFactory = ObjectIconPickerViewModelFactory(
        setEmojiIcon = setEmojiIcon,
        setImageIcon = setImageIcon,
        removeDocumentIcon = removeDocumentIcon,
        emojiSuggester = emojiSuggester,
        emojiProvider = Emoji,
        dispatcher = dispatcher,
        analytics = analytics
    )
}

@Module
object TextBlockIconPickerModule {

    @Provides
    @PerModal
    fun provideSetEmojiIcon(
        repo: BlockRepository
    ): SetTextBlockEmoji = SetTextBlockEmoji(
        repo = repo
    )

    @Provides
    @PerModal
    fun provideRemoveIcon(
        repo: BlockRepository
    ): RemoveTextBlockIcon = RemoveTextBlockIcon(
        repo = repo
    )

    @Provides
    @PerModal
    fun provideSetImage(
        repo: BlockRepository
    ): SetTextBlockImage = SetTextBlockImage(
        repo = repo
    )

    @Provides
    @PerModal
    fun provideViewModelFactory(
        setEmojiIcon: SetTextBlockEmoji,
        setImageIcon: SetTextBlockImage,
        removeDocumentIcon: RemoveTextBlockIcon,
        emojiSuggester: EmojiSuggester,
        dispatcher: Dispatcher<Payload>,
        analytics: Analytics
    ): TextBlockIconPickerViewModelFactory = TextBlockIconPickerViewModelFactory(
        setEmojiIcon = setEmojiIcon,
        setImageIcon = setImageIcon,
        removeDocumentIcon = removeDocumentIcon,
        emojiSuggester = emojiSuggester,
        emojiProvider = Emoji,
        dispatcher = dispatcher,
        analytics = analytics
    )
}

@Module
object ObjectSetIconPickerModule {
    @Provides
    @PerModal
    fun provideViewModelFactory(
        setEmojiIcon: SetDocumentEmojiIcon,
        setImageIcon: SetDocumentImageIcon,
        removeDocumentIcon: RemoveDocumentIcon,
        emojiSuggester: EmojiSuggester,
        dispatcher: Dispatcher<Payload>,
        analytics: Analytics
    ): ObjectSetIconPickerViewModelFactory = ObjectSetIconPickerViewModelFactory(
        setEmojiIcon = setEmojiIcon,
        setImageIcon = setImageIcon,
        removeDocumentIcon = removeDocumentIcon,
        emojiSuggester = emojiSuggester,
        emojiProvider = Emoji,
        dispatcher = dispatcher,
        analytics = analytics
    )
}